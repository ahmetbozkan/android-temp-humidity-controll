package com.example.thesisproject.data.warning;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.thesisproject.data.model.Humidity;
import com.example.thesisproject.data.model.Temperature;
import com.example.thesisproject.db.WarningDao;
import com.example.thesisproject.db.WarningDatabase;

import java.util.List;

public class WarningRepository {
    private static final String TAG = "WarningRepository";
    private final Context context;

    private static WarningRepository mInstance;
    private final WarningSharedPreferences warningSharedPreferences;

    private final WarningDao warningDao;

    private final LiveData<List<Temperature>> allWarningTemperatures;
    private final LiveData<List<Humidity>> allWarningHumidities;

    private WarningRepository(Application application) {
        warningSharedPreferences =
                new WarningSharedPreferences(application.getApplicationContext());

        context = application.getApplicationContext();

        WarningDatabase warningDatabase = WarningDatabase.getDatabaseInstance(
                application.getApplicationContext()
        );

        warningDao = warningDatabase.getWarningDao();

        allWarningTemperatures = warningDao.getAllTemperatureOverflows();
        allWarningHumidities = warningDao.getAllHumidityOverflows();
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


    public void setTempWarningType(String warningType) {
        warningSharedPreferences.setTempWarningType(warningType);
    }

    public String getTempWarningType() {
        return warningSharedPreferences.getTempWarningType();
    }

    public void setHumidityWarningType(String warningType) {
        warningSharedPreferences.setHumidityWarningType(warningType);
    }

    public String getHumidityWarningType() {
        return warningSharedPreferences.getHumidityWarningType();
    }


    public LiveData<List<Temperature>> getAllWarningTemperatures() {
        return allWarningTemperatures;
    }

    public LiveData<List<Humidity>> getAllWarningHumidities() {
        return allWarningHumidities;
    }

    public void insertWarningTemperature(Temperature temperature) {
        new InsertTemperatureAsyncTask(warningDao)
                .execute(temperature);
    }

    public void insertWarningHumidity(Humidity humidity) {
        new InsertHumidityAsyncTask(warningDao)
                .execute(humidity);
    }

    private static class InsertTemperatureAsyncTask extends AsyncTask<Temperature, Void, Void> {

        private final WarningDao warningDao;

        public InsertTemperatureAsyncTask(WarningDao warningDao) {
            this.warningDao = warningDao;
        }

        @Override
        protected Void doInBackground(Temperature... temperatures) {
            warningDao.insertTemperature(temperatures[0]);
            return null;
        }
    }

    private static class InsertHumidityAsyncTask extends AsyncTask<Humidity, Void, Void> {

        private final WarningDao warningDao;

        public InsertHumidityAsyncTask(WarningDao warningDao) {
            this.warningDao = warningDao;
        }

        @Override
        protected Void doInBackground(Humidity... humidities) {
            warningDao.insertHumidity(humidities[0]);
            return null;
        }
    }

}
