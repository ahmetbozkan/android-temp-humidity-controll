package com.example.thesisproject.ui;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.thesisproject.data.BtServiceRepository;

import java.util.UUID;

public class ServiceViewModel extends AndroidViewModel {

    private final BtServiceRepository repository;

    private final MutableLiveData<String> mIncomingMessage;
    private final MutableLiveData<Boolean> mIsConnecting;
    private final MutableLiveData<Boolean> mIsConnected;


    public ServiceViewModel(@NonNull Application application) {
        super(application);
        repository = BtServiceRepository.getRepositoryInstance(application);

        mIncomingMessage = repository.getmIncomingData();
        mIsConnecting = repository.getmIsConnecting();
        mIsConnected = repository.getmIsConnected();
    }

    public void connect() {
        repository.start();
    }

    public void startClient(BluetoothDevice device, UUID uuid) {
        repository.startClient(device, uuid);
    }

    public MutableLiveData<String> getmIncomingMessage() {
        return mIncomingMessage;
    }

    public MutableLiveData<Boolean> getmIsConnecting() {
        return mIsConnecting;
    }

    public MutableLiveData<Boolean> getmIsConnected() {
        return mIsConnected;
    }

    public void setIsConnecting(Boolean value) {
        repository.setIsConnecting(value);
    }
}
