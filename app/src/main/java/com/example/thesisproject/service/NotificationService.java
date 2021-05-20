package com.example.thesisproject.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.thesisproject.R;
import com.example.thesisproject.data.connection.BtServiceRepository;
import com.example.thesisproject.data.model.Humidity;
import com.example.thesisproject.data.model.Temperature;
import com.example.thesisproject.data.notification.NotificationRepository;
import com.example.thesisproject.data.warning.WarningRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationService extends LifecycleService {
    private static final String TAG = "NotificationService";

    private static final String TEMPERATURE_CHANNEL_ID = "warning_temp_notification_id";
    private static final int TEMPERATURE_NOTIFICATION_ID = 0;

    private static final String HUMIDITY_CHANNEL_ID = "warning_hum_notification_id";
    private static final int HUMIDITY_NOTIFICATION_ID = 1;

    private MutableLiveData<Boolean> shouldShowTempNotification;
    private MutableLiveData<Boolean> shouldShowHumNotification;

    private WarningRepository warningRepository;
    private BtServiceRepository btServiceRepository;

    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: Notification service started.");

        NotificationRepository notificationRepository = NotificationRepository
                .getRepositoryInstance(getApplication());

        shouldShowTempNotification = notificationRepository.getShouldShowTempNotification();
        shouldShowHumNotification = notificationRepository.getShouldShowHumNotification();

        warningRepository = WarningRepository
                .getRepositoryInstance(getApplication());

        btServiceRepository = BtServiceRepository.getRepositoryInstance(getApplication());

        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        /*float temperature = intent.getFloatExtra("current_temperature", 0);
        float humidity = intent.getFloatExtra("current_humidity", 0);

        Notification notification = new NotificationCompat
                .Builder(getApplicationContext(), TEMPERATURE_CHANNEL_ID)
                .setContentTitle("TEMPERATURE / HUMIDITY!")
                .setContentText("Temperature: " + temperature + "\nHumidity: " + humidity)
                .setSmallIcon(R.drawable.ic_flame)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();*/

        btServiceRepository.getmIncomingData().observe(this, incomingMessage -> {
            float currentWarningTemp = warningRepository.getTempWarning();
            float currentWarningHum = warningRepository.getHumWarning();

            if (incomingMessage != null) {
                String[] splittedMessage = incomingMessage.split("\\s+");

                if (splittedMessage.length == 2) {
                    String temp = splittedMessage[0];
                    String hum = splittedMessage[1];

                    if (temp.length() == 5 && hum.length() == 5) {
                        try {
                            float floatTemp = Float.parseFloat(temp);
                            float floatHum = Float.parseFloat(hum);

                            if(currentWarningTemp < floatTemp || currentWarningHum < floatHum) {
                                setTemperatureOverflowActions(currentWarningTemp, floatTemp);
                                setHumidityOverflowActions(currentWarningHum, floatHum);
                            }

                            Log.d(TAG, "onViewCreated: Float Temp: " + floatTemp +
                                    "\nHum Temp: " + floatHum);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        return START_NOT_STICKY;
    }

    private void setTemperatureOverflowActions(float currentWarningTemp, float floatTemp) {
        if (currentWarningTemp < floatTemp) {
            Notification temperatureNotification = new NotificationCompat
                    .Builder(getApplicationContext(), TEMPERATURE_CHANNEL_ID)
                    .setContentTitle("TEMPERATURE WARNING!")
                    .setContentText("Temperature is over " + currentWarningTemp + "!")
                    .setSmallIcon(R.drawable.ic_flame)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManagerCompat.notify(TEMPERATURE_NOTIFICATION_ID, temperatureNotification);

            String stringValueOfTemp = String.valueOf(floatTemp);
            String stringValueOfWarning = String.valueOf(currentWarningTemp);

            String difference = String.valueOf(floatTemp - currentWarningTemp);

            String date = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                    .format(new Date());


            Temperature temperature = new Temperature(stringValueOfTemp, stringValueOfWarning, difference, date);
            warningRepository.insertWarningTemperature(temperature);
            Log.d(TAG, "Temperature Overflow inserted to db, temperature: " + stringValueOfTemp);

            /*temperatureNotification = new NotificationCompat.Builder(requireContext(), TEMPERATURE_CHANNEL_ID)
                    .setContentTitle("TEMPERATURE WARNING!")
                    .setContentText("Temperature is over " + currentWarningTemp + "!")
                    .setSmallIcon(R.drawable.ic_flame)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManagerCompat.notify(TEMPERATURE_NOTIFICATION_ID, temperatureNotification);*/
        }
    }

    private void setHumidityOverflowActions(float currentWarningHum, float floatHum) {
        if (currentWarningHum < floatHum) {
            Notification humidityNotification = new NotificationCompat
                    .Builder(getApplicationContext(), HUMIDITY_CHANNEL_ID)
                    .setContentTitle("HUMIDITY WARNING!")
                    .setContentText("Humidity is over " + currentWarningHum + "!")
                    .setSmallIcon(R.drawable.ic_flame)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManagerCompat.notify(HUMIDITY_NOTIFICATION_ID, humidityNotification);

            String stringValueOfHumidity = String.valueOf(floatHum);
            String stringValueOfWarning = String.valueOf(currentWarningHum);
            String difference = String.valueOf(floatHum - currentWarningHum);

            String date = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                    .format(new Date());

            Humidity humidity = new Humidity(stringValueOfHumidity, stringValueOfWarning, difference, date);
            warningRepository.insertWarningHumidity(humidity);
            Log.d(TAG, "Temperature Overflow inserted to db, temperature: " + stringValueOfHumidity);

            /*humidityNotification = new NotificationCompat.Builder(requireContext(), HUMIDITY_CHANNEL_ID)
                    .setContentTitle("HUMIDITY WARNING!")
                    .setContentText("Humidity is over " + currentWarningHum + "!")
                    .setSmallIcon(R.drawable.ic_flame)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManagerCompat.notify(HUMIDITY_NOTIFICATION_ID, temperatureNotification);*/
        }
    }

}
