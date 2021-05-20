package com.example.thesisproject.ui;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.thesisproject.data.connection.BtServiceRepository;
import com.example.thesisproject.data.model.Humidity;
import com.example.thesisproject.data.model.Temperature;
import com.example.thesisproject.data.notification.NotificationRepository;
import com.example.thesisproject.data.warning.WarningRepository;

import java.util.List;
import java.util.UUID;

public class ServiceViewModel extends AndroidViewModel {

    private final BtServiceRepository btServiceRepository;
    private final MutableLiveData<String> mIncomingMessage;
    private final MutableLiveData<Boolean> mIsConnecting;
    private final MutableLiveData<Boolean> mIsConnected;

    private final WarningRepository warningRepository;

    private final NotificationRepository notificationRepository;
    private final MutableLiveData<Boolean> shouldShowTempNotification;
    private final MutableLiveData<Boolean> shouldShowHumNotification;

    private final LiveData<List<Temperature>> allWarningTemperatures;
    private final LiveData<List<Humidity>> allWarningHumidities;

    public ServiceViewModel(@NonNull Application application) {
        super(application);
        btServiceRepository = BtServiceRepository.getRepositoryInstance(application);

        mIncomingMessage = btServiceRepository.getmIncomingData();
        mIsConnecting = btServiceRepository.getmIsConnecting();
        mIsConnected = btServiceRepository.getmIsConnected();

        warningRepository = WarningRepository.getRepositoryInstance(application);

        notificationRepository = NotificationRepository.getRepositoryInstance(application);
        shouldShowTempNotification = notificationRepository.getShouldShowTempNotification();
        shouldShowHumNotification = notificationRepository.getShouldShowHumNotification();

        allWarningTemperatures = warningRepository.getAllWarningTemperatures();
        allWarningHumidities = warningRepository.getAllWarningHumidities();
    }

    public void insertWarningTemperature(Temperature temperature) {
        warningRepository.insertWarningTemperature(temperature);
    }

    public void insertWarningHumidity(Humidity humidity) {
        warningRepository.insertWarningHumidity(humidity);
    }

    public LiveData<List<Temperature>> getAllWarningTemperatures() {
        return allWarningTemperatures;
    }

    public LiveData<List<Humidity>> getAllWarningHumidities() {
        return allWarningHumidities;
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

    public void setTempWarningType(String warningType) {
        warningRepository.setTempWarningType(warningType);
    }

    public String getTempWarningType() {
        return warningRepository.getTempWarningType();
    }

    public void setHumidityWarningType(String warningType) {
        warningRepository.setHumidityWarningType(warningType);
    }

    public String getHumidityWarningType() {
        return warningRepository.getHumidityWarningType();
    }

    /*public void setShouldShowTempNotification(boolean shouldShow) {
        notificationRepository.setShouldShowTempNotification(shouldShow);
    }

    public void setShouldShowHumNotification(boolean shouldShow) {
        notificationRepository.setShouldShowHumNotification(shouldShow);
    }

    public MutableLiveData<Boolean> getShouldShowTempNotification() {
        return shouldShowTempNotification;
    }

    public MutableLiveData<Boolean> getShouldShowHumNotification() {
        return shouldShowHumNotification;
    }*/

    public void connect() {
        btServiceRepository.start();
    }

    /* public void startClient(BluetoothDevice device, UUID uuid) {
        btServiceRepository.startClient(device, uuid);
    } */
}
