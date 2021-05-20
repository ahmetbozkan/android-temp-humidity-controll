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

public class WarningHumidityDetailsFragment extends Fragment {

    private TextView tv_humidity;
    private TextView tv_warningHumidity;
    private TextView tv_difference;
    private TextView tv_date;

    private WarningHumidityDetailsFragmentArgs args;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Humidity humidity = args.getHumidity();

        tv_humidity.setText(humidity.getHumidity() + "%");
        tv_warningHumidity.setText(humidity.getWarningHumidity() + "%");
        tv_difference.setText( "+" + humidity.getDifference() + "%");
        tv_date.setText(humidity.getDate());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_warning_humidity_details, container, false);
        init(v);

        return v;
    }

    private void init(View v) {
        tv_humidity = v.findViewById(R.id.tv_humidity_detail);
        tv_warningHumidity = v.findViewById(R.id.tv_humidity_warning_detail);
        tv_difference = v.findViewById(R.id.tv_humidity_difference);
        tv_date = v.findViewById(R.id.tv_humidity_date_details);

        args = WarningHumidityDetailsFragmentArgs.fromBundle(requireArguments());
    }
}
