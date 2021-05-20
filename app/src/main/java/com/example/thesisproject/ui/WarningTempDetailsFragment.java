package com.example.thesisproject.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.thesisproject.R;
import com.example.thesisproject.data.model.Humidity;
import com.example.thesisproject.data.model.Temperature;

public class WarningTempDetailsFragment extends Fragment {

    private TextView tv_temperature;
    private TextView tv_warningTemperature;
    private TextView tv_difference;
    private TextView tv_date;

    private WarningTempDetailsFragmentArgs args;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Temperature temperature = args.getTemperature();

        tv_temperature.setText(temperature.getTemperature() + "°C");
        tv_warningTemperature.setText(temperature.getWarningTemperature() + "°C");
        tv_difference.setText( "+" + temperature.getDifference() + "°C");
        tv_date.setText(temperature.getDate());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_warning_temp_details, container, false);
        init(v);

        return v;
    }


    private void init(View v) {
        tv_temperature = v.findViewById(R.id.tv_temperature_detail);
        tv_warningTemperature = v.findViewById(R.id.tv_temperature_warning_detail);
        tv_difference = v.findViewById(R.id.tv_temperature_difference);
        tv_date = v.findViewById(R.id.tv_temperature_date_details);

        args = WarningTempDetailsFragmentArgs.fromBundle(requireArguments());
    }

}
