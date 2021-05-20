package com.example.thesisproject.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.thesisproject.R;
import com.example.thesisproject.service.NotificationService;

public class MonitoringFragment extends Fragment {
    private static final String TAG = "MonitoringFragment";

    private ServiceViewModel serviceViewModel;

    private TextView tv_temperature;
    private TextView tv_humidity;

    private TextView tv_warningTemp;
    private TextView tv_warningHum;

    private ImageButton btn_increaseWarningTemp;
    private ImageButton btn_decreaseWarningTemp;
    private ImageButton btn_increaseWarningHum;
    private ImageButton btn_decreaseWarningHum;
    private ImageButton btn_setTempWarningType;
    private ImageButton btn_setHumWarningType;

    private String currentTemperatureWarningType;
    private String temporaryTemperatureWarningType;

    private String currentHumidityWarningType;
    private String temporaryHumidityWarningType;

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

        Intent intent = new Intent(requireContext(), NotificationService.class);
        requireActivity().startService(intent);

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
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monitoring, container, false);
        init(v);

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


    private void setTemperatureOverflowActions(float currentWarningTemp, float floatTemp){
        if (currentWarningTemp < floatTemp) {
            tv_temperature.setTextColor(Color.parseColor("#FF950000"));
        }
        else {
            tv_temperature.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void setHumidityOverflowActions(float currentWarningHum, float floatHum){
        if (currentWarningHum < floatHum) {
            tv_humidity.setTextColor(Color.parseColor("#FF950000"));
        }
        else {
            tv_humidity.setTextColor(Color.parseColor("#000000"));
        }
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


    private void init(View v) {
        tv_temperature = v.findViewById(R.id.tv_temperature);
        tv_humidity = v.findViewById(R.id.tv_humidity);

        tv_warningTemp = v.findViewById(R.id.tv_warning_temp);
        tv_warningHum = v.findViewById(R.id.tv_warning_humidity);

        btn_increaseWarningTemp = v.findViewById(R.id.btn_increase_warning_temp);
        btn_decreaseWarningTemp = v.findViewById(R.id.btn_decrease_warning_temp);
        btn_increaseWarningHum = v.findViewById(R.id.btn_increase_warning_hum);
        btn_decreaseWarningHum = v.findViewById(R.id.btn_decrease_warning_hum);
        btn_setTempWarningType = v.findViewById(R.id.btn_set_temperature_warning_type);
        btn_setHumWarningType = v.findViewById(R.id.btn_set_humidity_warning_type);

        btn_saveWarningTemp = v.findViewById(R.id.btn_save_warning_temp);
        btn_saveWarningHum = v.findViewById(R.id.btn_save_warning_hum);

        setWarningTempListeners();
        setWarningHumListeners();

        Button btn_temperatureOverflows = v.findViewById(R.id.btn_display_temp_overflows);
        btn_temperatureOverflows.setOnClickListener(button -> {
            NavDirections action = MonitoringFragmentDirections
                    .actionMonitoringFragmentToWarningTemperaturesFragment2();

            Navigation
                    .findNavController(v)
                    .navigate(action);
        });

        Button btn_humidityOverflows = v.findViewById(R.id.btn_display_hum_overflows);
        btn_humidityOverflows.setOnClickListener(button -> {
            NavDirections action = MonitoringFragmentDirections
                    .actionMonitoringFragmentToWarningHumiditiesFragment2();

            Navigation
                    .findNavController(v)
                    .navigate(action);
        });
    }






}
