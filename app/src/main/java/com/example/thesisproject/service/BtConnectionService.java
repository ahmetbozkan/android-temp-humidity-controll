package com.example.thesisproject.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.thesisproject.data.BtServiceRepository;
import com.example.thesisproject.ui.ServiceViewModel;

import java.util.UUID;

public class BtConnectionService extends Service {

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BtServiceRepository repository;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        repository = BtServiceRepository
                .getRepositoryInstance(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BluetoothDevice device = intent.getParcelableExtra("BluetoothDevice");
        repository.startClient(device, MY_UUID_INSECURE);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
