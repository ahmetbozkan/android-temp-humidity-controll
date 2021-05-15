package com.example.thesisproject.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.thesisproject.R;

public class MonitoringFragment extends Fragment {
    private static final String TAG = "MonitoringFragment";

    private ServiceViewModel serviceViewModel;

    private static final String TEMPERATURE_CHANNEL_ID = "warning_temp_notification_id";
    private static final String TEMPERATURE_CHANNEL_NAME = "warning_temp_notification_name";
    private static final int TEMPERATURE_NOTIFICATION_ID = 0;
    private Notification temperatureNotification;
    private NotificationManagerCompat notificationManagerCompat;

    private static final String HUMIDITY_CHANNEL_ID = "warning_hum_notification_id";
    private static final String HUMIDITY_CHANNEL_NAME = "warning_hum_notification_name";
    private static final int HUMIDITY_NOTIFICATION_ID = 1;
    private Notification humidityNotification;

    private TextView tv_temperature;
    private TextView tv_humidity;

    private TextView tv_warningTemp;
    private TextView tv_warningHum;

    private ImageButton btn_increaseWarningTemp;
    private ImageButton btn_decreaseWarningTemp;
    private ImageButton btn_increaseWarningHum;
    private ImageButton btn_decreaseWarningHum;

    private Button btn_saveWarningTemp;
    private Button btn_saveWarningHum;

    private float currentWarningTemp;
    private float temporaryWarningTemp;

    private float currentWarningHum;
    private float temporaryWarningHum;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceViewModel.getmIncomingMessage().observe(getViewLifecycleOwner(), incomingMessage -> {
            if (incomingMessage != null) {
                String[] splittedMessage = incomingMessage.split("\\s+");

                if (splittedMessage.length == 2) {
                    String temp = splittedMessage[0];
                    String hum = splittedMessage[1];

                    if (temp.length() == 5 && hum.length() == 5) {
                        try {
                            float floatTemp = Float.parseFloat(temp);
                            float floatHum = Float.parseFloat(hum);

                            tv_temperature.setText(temp + "°C");
                            tv_humidity.setText(hum + "%");

                            setTemperatureOverflowActions(currentWarningTemp, floatTemp);
                            setHumidityOverflowActions(currentWarningHum, floatHum);

                            Log.d(TAG, "onViewCreated: Float Temp: " + floatTemp +
                                    "\nHum Temp: " + floatHum);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monitoring, container, false);
        init(v);
        createTemperatureNotificationChannel();
        createHumidityNotificationChannel();

        notificationManagerCompat = NotificationManagerCompat.from(requireContext());

        serviceViewModel = new ViewModelProvider(requireActivity())
                .get(ServiceViewModel.class);


        currentWarningTemp = serviceViewModel.getTempWarning();
        tv_warningTemp.setText(currentWarningTemp + "°C");
        temporaryWarningTemp = currentWarningTemp;

        currentWarningHum = serviceViewModel.getHumWarning();
        tv_warningHum.setText(currentWarningHum + "%");
        temporaryWarningHum = currentWarningHum;

        return v;
    }

    private void setTemperatureOverflowActions(float currentWarningTemp, float floatTemp) {
        if (currentWarningTemp < floatTemp) {
            tv_temperature.setTextColor(Color.parseColor("#FF0000"));

            temperatureNotification = new NotificationCompat.Builder(requireContext(), TEMPERATURE_CHANNEL_ID)
                    .setContentTitle("TEMPERATURE WARNING!")
                    .setContentText("Temperature is over " + currentWarningTemp + "!")
                    .setSmallIcon(R.drawable.ic_flame)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManagerCompat.notify(TEMPERATURE_NOTIFICATION_ID, temperatureNotification);
        }
        else {
            tv_temperature.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void setHumidityOverflowActions(float currentWarningHum, float floatHum) {
        if (currentWarningHum < floatHum) {
            tv_humidity.setTextColor(Color.parseColor("#FF0000"));

            humidityNotification = new NotificationCompat.Builder(requireContext(), HUMIDITY_CHANNEL_ID)
                    .setContentTitle("HUMIDITY WARNING!")
                    .setContentText("Humidity is over " + currentWarningHum + "!")
                    .setSmallIcon(R.drawable.ic_flame)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build();

            notificationManagerCompat.notify(HUMIDITY_NOTIFICATION_ID, temperatureNotification);
        }
        else {
            tv_humidity.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: App is in the background");
    }

    private void init(View v) {
        tv_temperature = v.findViewById(R.id.tv_temperature);
        tv_humidity = v.findViewById(R.id.tv_humidity);

        tv_warningTemp = v.findViewById(R.id.tv_warning_temp);
        tv_warningHum = v.findViewById(R.id.tv_warning_humidity);

        btn_increaseWarningTemp = v.findViewById(R.id.btn_increase_warning_temp);
        btn_decreaseWarningTemp = v.findViewById(R.id.btn_decrease_warning_temp);
        btn_increaseWarningHum = v.findViewById(R.id.btn_increase_warning_hum);
        btn_decreaseWarningHum = v.findViewById(R.id.btn_decrease_warning_hum);

        btn_saveWarningTemp = v.findViewById(R.id.btn_save_warning_temp);
        btn_saveWarningHum = v.findViewById(R.id.btn_save_warning_hum);

        setWarningTempListeners();
        setWarningHumListeners();
    }

    @SuppressLint("SetTextI18n")
    private void setWarningTempListeners() {
        btn_increaseWarningTemp.setOnClickListener(button -> {
            currentWarningTemp++;
            tv_warningTemp.setText(currentWarningTemp + "°C");

            btn_saveWarningTemp.setEnabled(!(currentWarningTemp == temporaryWarningTemp));
        });

        btn_decreaseWarningTemp.setOnClickListener(button -> {
            currentWarningTemp--;
            tv_warningTemp.setText(currentWarningTemp + "°C");

            btn_saveWarningTemp.setEnabled(!(currentWarningTemp == temporaryWarningTemp));
        });

        btn_saveWarningTemp.setOnClickListener(button -> {
            serviceViewModel.setTempWarning(currentWarningTemp);
            temporaryWarningTemp = currentWarningTemp;

            btn_saveWarningTemp.setEnabled(false);
        });
    }

    @SuppressLint("SetTextI18n")
    private void setWarningHumListeners() {
        btn_increaseWarningHum.setOnClickListener(button -> {
            currentWarningHum++;
            tv_warningHum.setText(currentWarningHum + "%");

            btn_saveWarningHum.setEnabled(!(currentWarningHum == temporaryWarningHum));
        });

        btn_decreaseWarningHum.setOnClickListener(button -> {
            currentWarningHum--;
            tv_warningHum.setText(currentWarningHum + "%");

            btn_saveWarningHum.setEnabled(!(currentWarningHum == temporaryWarningHum));
        });

        btn_saveWarningHum.setOnClickListener(button -> {
            serviceViewModel.setHumWarning(currentWarningHum);
            temporaryWarningHum = currentWarningHum;

            btn_saveWarningHum.setEnabled(false);
        });
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

            NotificationManager manager = (NotificationManager) requireActivity()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
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

            NotificationManager manager = (NotificationManager) requireActivity()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

        }
    }
}
