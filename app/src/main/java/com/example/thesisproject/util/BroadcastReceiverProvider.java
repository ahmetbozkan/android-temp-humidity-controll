package com.example.thesisproject.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesisproject.R;

import java.util.ArrayList;

public class BroadcastReceiverProvider {
    private static final String TAG = "BroadcastReceiverProvid";

    private BluetoothDevice mBtDevice;
    private BtDeviceAdapter mBtDeviceAdapter;

    public BroadcastReceiverProvider(BluetoothDevice mBtDevice, BtDeviceAdapter mBtDeviceAdapter) {
        this.mBtDevice = mBtDevice;
        this.mBtDeviceAdapter = mBtDeviceAdapter;
    }

    public static BroadcastReceiver provideEnableBtReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            Log.d(TAG, "onReceive: STATE OFF");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                            Toast.makeText(context, "Bluetooth turning off.", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                            Toast.makeText(context, "Bluetooth turning on.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        };
    }

    public BroadcastReceiver provideDeviceListReceiver(ArrayList<BluetoothDevice> deviceList, RecyclerView recyclerView, BtDeviceAdapter.OnDeviceClickListener deviceClickListener) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                Log.d(TAG, "onReceive: ACTION FOUND.");

                if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    deviceList.add(device);
                    Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                    mBtDeviceAdapter = new BtDeviceAdapter(deviceList, deviceClickListener);
                    recyclerView.setAdapter(mBtDeviceAdapter);
                }
            }
        };
    }

    public BroadcastReceiver provideDetectStateChangeReceiver(MutableLiveData<Boolean> isConnecting) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                    BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                        Toast.makeText(context, "Connection established.", Toast.LENGTH_SHORT).show();
                        mBtDevice = mDevice;
                    }
                    if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                        Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                        isConnecting.postValue(true);
                    }
                    if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                        Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                        Toast.makeText(context, "Connection failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }


}
