package com.example.thesisproject.data;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

public class WarningRepository {
    private static final String TAG = "WarningRepository";
    private final Context context;

    private static WarningRepository mInstance;
    private final WarningSharedPreferences warningSharedPreferences;

    private WarningRepository(Application application) {
        warningSharedPreferences =
                new WarningSharedPreferences(application.getApplicationContext());

        context = application.getApplicationContext();
    }

    public static synchronized WarningRepository getRepositoryInstance(Application application) {
        if (mInstance == null) {
            mInstance = new WarningRepository(application);
        }

        return mInstance;
    }

    public void setTempWarning(float temp) {
        warningSharedPreferences.setTempWarning(temp);
        Toast.makeText(context, "Temperature warning set to: " + temp, Toast.LENGTH_SHORT).show();
    }

    public float getTempWarning() {
        return warningSharedPreferences.getTempWarning();
    }

    public void setHumWarning(float hum) {
        warningSharedPreferences.setHumWarning(hum);
        Toast.makeText(context, "Humidity warning set to: " + hum, Toast.LENGTH_SHORT).show();
    }

    public float getHumWarning() {
        return warningSharedPreferences.getHumWarning();
    }

}
