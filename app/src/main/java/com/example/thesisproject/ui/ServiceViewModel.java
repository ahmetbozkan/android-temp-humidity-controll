package com.example.thesisproject.ui;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.thesisproject.data.BtServiceRepository;
import com.example.thesisproject.data.WarningRepository;

import java.util.UUID;

public class ServiceViewModel extends AndroidViewModel {

    private final BtServiceRepository btServiceRepository;

    private final MutableLiveData<String> mIncomingMessage;
    private final MutableLiveData<Boolean> mIsConnecting;
    private final MutableLiveData<Boolean> mIsConnected;

    private final WarningRepository warningRepository;

    public ServiceViewModel(@NonNull Application application) {
        super(application);
        btServiceRepository = BtServiceRepository.getRepositoryInstance(application);
        warningRepository = WarningRepository.getRepositoryInstance(application);

        mIncomingMessage = btServiceRepository.getmIncomingData();
        mIsConnecting = btServiceRepository.getmIsConnecting();
        mIsConnected = btServiceRepository.getmIsConnected();
    }

    public void connect() {
        btServiceRepository.start();
    }

    public void startClient(BluetoothDevice device, UUID uuid) {
        btServiceRepository.startClient(device, uuid);
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
        btServiceRepository.setIsConnecting(value);
    }

    public void setTempWarning(float temp) {
        warningRepository.setTempWarning(temp);
    }

    public float getTempWarning() {
        return warningRepository.getTempWarning();
    }

    public void setHumWarning(float temp) {
        warningRepository.setHumWarning(temp);
    }

    public float getHumWarning() {
        return warningRepository.getHumWarning();
    }
}
