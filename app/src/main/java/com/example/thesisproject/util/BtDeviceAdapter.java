package com.example.thesisproject.util;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesisproject.R;

import java.util.ArrayList;
import java.util.List;

public class BtDeviceAdapter extends RecyclerView.Adapter<BtDeviceAdapter.BtViewHolder> {

    private final ArrayList<BluetoothDevice> deviceList;
    private final OnDeviceClickListener onDeviceClickListener;

    public BtDeviceAdapter(ArrayList<BluetoothDevice> deviceList, OnDeviceClickListener onDeviceClickListener) {
        this.deviceList = deviceList;
        this.onDeviceClickListener = onDeviceClickListener;
    }


    @NonNull
    @Override
    public BtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_bluetooth_device_item, parent, false);

        return new BtViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BtViewHolder holder, int position) {
        BluetoothDevice currentDevice = deviceList.get(position);

        holder.tv_deviceName.setText(currentDevice.getName());
        holder.tv_deviceAddress.setText(currentDevice.getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class BtViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView tv_deviceName;
        private final TextView tv_deviceAddress;

        public BtViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_deviceName = itemView.findViewById(R.id.tv_device_name);
            tv_deviceAddress = itemView.findViewById(R.id.tv_device_address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == itemView.getId()) {
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION) {
                    onDeviceClickListener.onDeviceClick(position);
                }
            }
        }
    }

    public interface OnDeviceClickListener {
        void onDeviceClick(int position);
    }
}
