package com.example.thesisproject;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

public class BaseApplication extends Application {

    private static final String TEMPERATURE_CHANNEL_ID = "warning_temp_notification_id";
    private static final String TEMPERATURE_CHANNEL_NAME = "warning_temp_notification_name";

    private static final String HUMIDITY_CHANNEL_ID = "warning_hum_notification_id";
    private static final String HUMIDITY_CHANNEL_NAME = "warning_hum_notification_name";

    @Override
    public void onCreate() {
        super.onCreate();

        createTemperatureNotificationChannel();
        createHumidityNotificationChannel();
    }

    private void createTemperatureNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    TEMPERATURE_CHANNEL_ID,
                    TEMPERATURE_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setLightColor(Color.RED);
            channel.enableLights(true);

            NotificationManager manager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

        }
    }

    private void createHumidityNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    HUMIDITY_CHANNEL_ID,
                    HUMIDITY_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setLightColor(Color.RED);
            channel.enableLights(true);

            NotificationManager manager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

        }
    }


}
