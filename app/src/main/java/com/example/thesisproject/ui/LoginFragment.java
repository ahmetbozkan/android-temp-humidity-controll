package com.example.thesisproject.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.thesisproject.R;
import com.example.thesisproject.service.BtConnectionService;
import com.example.thesisproject.util.BroadcastReceiverProvider;
import com.example.thesisproject.util.BtDeviceAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class LoginFragment extends Fragment implements BtDeviceAdapter.OnDeviceClickListener {
    private static final String TAG = "LoginFragment";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private ServiceViewModel serviceViewModel;

    private BluetoothAdapter mBtAdapter;
    private BluetoothDevice mBtDevice;

    private BtDeviceAdapter mBtDevicesAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;

    private BroadcastReceiver enableBtReceiver;
    private BroadcastReceiver discoverReceiver;
    private BroadcastReceiver connectReceiver;

    private ProgressDialog progressDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceViewModel.getmIsConnected().observe(getViewLifecycleOwner(), isConnected -> {
            if(isConnected) {
                NavDirections action = LoginFragmentDirections
                        .actionLoginFragmentToMonitoringFragment();

                Navigation
                        .findNavController(view)
                        .navigate(action);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        serviceViewModel = new ViewModelProvider(requireActivity())
                .get(ServiceViewModel.class);

        init(v);

        return v;
    }

    private void init(View v) {
        mDeviceList = new ArrayList<>();
        mBtDevicesAdapter = new BtDeviceAdapter(mDeviceList, this);

        RecyclerView recyclerView = v.findViewById(R.id.rv_device_list);
        Button btn_discover = v.findViewById(R.id.btn_discover);
        Button btn_connect = v.findViewById(R.id.btn_connect);

        BroadcastReceiverProvider broadcastReceiverProvider =
                new BroadcastReceiverProvider(mBtDevice, mBtDevicesAdapter);

        enableBtReceiver = BroadcastReceiverProvider.provideEnableBtReceiver();
        discoverReceiver = broadcastReceiverProvider
                .provideDeviceListReceiver(mDeviceList, recyclerView, this);
        connectReceiver = broadcastReceiverProvider
                .provideDetectStateChangeReceiver(serviceViewModel.getmIsConnecting());

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        btn_discover.setOnClickListener(view -> {
            Log.d(TAG, "onClick: enabling/disabling bluetooth.");
            enableBt();
        });

        btn_connect.setOnClickListener(view -> {
            connect();
        });
    }

    private void connect() {
        progressDialog = ProgressDialog.show(requireContext(),"Connecting Bluetooth"
                ,"Please Wait...",true);

        if(mBtDevice != null) {
            Intent intent = new Intent(requireContext(), BtConnectionService.class);
            intent.putExtra("BluetoothDevice", mBtDevice);
            requireActivity().startService(intent);

            serviceViewModel.setIsConnecting(true);
            serviceViewModel.getmIsConnecting().observe(getViewLifecycleOwner(), isConnecting -> {
                if(isConnecting) {
                    if(!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }
                else {
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

            // Connection will be established in service.
            //serviceViewModel.startClient(mBtDevice, MY_UUID_INSECURE);
        }
        else {
            Toast.makeText(requireContext(), "You have to select a device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableBt() {
        if (mBtAdapter == null) {
            Toast.makeText(requireContext(), "Your phone doesn't have bluetooth capability.", Toast.LENGTH_SHORT).show();
        }
        if (!mBtAdapter.isEnabled()) {
            Toast.makeText(requireContext(), "Enabling bluetooth.", Toast.LENGTH_SHORT).show();
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            requireActivity()
                    .registerReceiver(enableBtReceiver, BTIntent);

            discover();
        }
        if (mBtAdapter.isEnabled()) {
            discover();
        }
    }

    private void discover() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
        Toast.makeText(requireContext(), "Looking for unpaired devices.", Toast.LENGTH_SHORT).show();

        if (mDeviceList.size() != 0) {
            mDeviceList.clear();
        }

        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            checkBtPermissions();

            mBtAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            requireActivity()
                    .registerReceiver(discoverReceiver, discoverDevicesIntent);
        }
        if (!mBtAdapter.isDiscovering()) {
            checkBtPermissions();

            mBtAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            requireActivity().
                    registerReceiver(discoverReceiver, discoverDevicesIntent);
        }
    }

    private void checkBtPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = 0;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                permissionCheck = requireActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
                permissionCheck += requireActivity().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            }
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(enableBtReceiver);
        requireActivity().unregisterReceiver(discoverReceiver);
        requireActivity().unregisterReceiver(connectReceiver);
    }

    @Override
    public void onDeviceClick(int position) {
        mBtAdapter.cancelDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        requireActivity().
                registerReceiver(connectReceiver, filter);

        Log.d(TAG, "onDeviceClick: clicked");

        BluetoothDevice selectedDevice = mDeviceList.get(position);

        String deviceName = selectedDevice.getName();
        String deviceAddress = selectedDevice.getAddress();
        Log.d(TAG, "onDeviceClick: Selected Device\n" +
                "Device Name: " + deviceName + "\n" +
                "Device Address: " + deviceAddress);

        selectedDevice.createBond();
        mBtDevice = selectedDevice;
        serviceViewModel.connect();
    }
}