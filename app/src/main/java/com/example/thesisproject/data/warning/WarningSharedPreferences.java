package com.example.thesisproject.data.warning;

import android.content.Context;
import android.content.SharedPreferences;

public class WarningSharedPreferences implements WarningService{

    public static final String WARNING_PREFS_NAME = "warning_shared_prefs";
    public static final String TEMPERATURE_WARNING_KEY = "temperature_warning";
    private static final String HUMIDITY_WARNING_KEY = "humidity_warning";

    private static final String TEMPERATURE_WARNING_TYPE = "temperature_warning_type";
    private static final String HUMIDITY_WARNING_TYPE = "humidity_warning_type";

    private final SharedPreferences mInstance;

    public WarningSharedPreferences(Context context) {
        mInstance = context.getSharedPreferences(WARNING_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setTempWarning(float temp) {
        SharedPreferences.Editor editor = mInstance.edit();
        editor.putFloat(TEMPERATURE_WARNING_KEY, temp);
        editor.apply();
    }

    public float getTempWarning() {
        return mInstance.getFloat(TEMPERATURE_WARNING_KEY, 0);
    }

    public void setHumWarning(float hum) {
        SharedPreferences.Editor editor = mInstance.edit();
        editor.putFloat(HUMIDITY_WARNING_KEY, hum);
        editor.apply();
    }

    public float getHumWarning() {
        return mInstance.getFloat(HUMIDITY_WARNING_KEY, 0);
    }

    public void setTempWarningType(String warningType) {
        SharedPreferences.Editor editor = mInstance.edit();
        editor.putString(TEMPERATURE_WARNING_TYPE, warningType);
        editor.apply();
    }

    public String getTempWarningType() {
        return mInstance.getString(TEMPERATURE_WARNING_TYPE, "up");
    }

    public void setHumidityWarningType(String warningType) {
        SharedPreferences.Editor editor = mInstance.edit();
        editor.putString(HUMIDITY_WARNING_TYPE, warningType);
        editor.apply();
    }

    public String getHumidityWarningType() {
        return mInstance.getString(HUMIDITY_WARNING_TYPE, "up");
    }

}
