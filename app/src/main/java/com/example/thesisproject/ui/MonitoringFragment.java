package com.example.thesisproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thesisproject.R;

public class MonitoringFragment extends Fragment {

    private ServiceViewModel serviceViewModel;

    private TextView tv_temperature;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceViewModel.getmIncomingMessage().observe(getViewLifecycleOwner(), incomingMessage -> {
            if(incomingMessage != null) {
                tv_temperature.setText(incomingMessage);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monitoring, container, false);

        tv_temperature = v.findViewById(R.id.tv_temperature);

        serviceViewModel = new ViewModelProvider(requireActivity())
                .get(ServiceViewModel.class);

        return v;
    }
}
