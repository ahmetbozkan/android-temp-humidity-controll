package com.example.thesisproject.data.notification;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

public class NotificationRepository {

    private static NotificationRepository mInstance;

    private final MutableLiveData<Boolean> shouldShowTempNotification;
    private final MutableLiveData<Boolean> shouldShowHumNotification;

    private NotificationRepository(Application application){
        shouldShowTempNotification = new MutableLiveData<>();

        shouldShowHumNotification = new MutableLiveData<>();
    }

    public static synchronized NotificationRepository getRepositoryInstance(Application application) {
        if(mInstance == null) {
            mInstance = new NotificationRepository(application);
        }

        return mInstance;
    }

    public void setShouldShowTempNotification(boolean shouldShow) {
        shouldShowTempNotification.setValue(shouldShow);
    }

    public void setShouldShowHumNotification(boolean shouldShow) {
        shouldShowHumNotification.setValue(shouldShow);
    }

    public MutableLiveData<Boolean> getShouldShowTempNotification() {
        return shouldShowTempNotification;
    }

    public MutableLiveData<Boolean> getShouldShowHumNotification() {
        return shouldShowHumNotification;
    }
}
