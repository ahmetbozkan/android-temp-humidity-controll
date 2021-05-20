package com.example.thesisproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesisproject.R;
import com.example.thesisproject.data.model.Temperature;
import com.example.thesisproject.util.WarningTemperatureAdapter;


public class WarningTemperaturesFragment extends Fragment implements WarningTemperatureAdapter.OnItemClickListener {

    private WarningTemperatureAdapter adapter;

    private ServiceViewModel serviceViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceViewModel.getAllWarningTemperatures().observe(getViewLifecycleOwner(), temperatures -> {
            adapter.submitList(temperatures);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_warning_temperatures, container, false);
        buildRecyclerView(v);

        serviceViewModel = new ViewModelProvider(requireActivity())
                .get(ServiceViewModel.class);

        return v;
    }

    private void buildRecyclerView(View v) {
        RecyclerView rv = v.findViewById(R.id.rv_warning_temperatures);
        rv.setHasFixedSize(true);

        adapter = new WarningTemperatureAdapter(this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Temperature temperature) {
        NavDirections action = WarningTemperaturesFragmentDirections
                .actionWarningTemperaturesFragment2ToWarningTempDetailsFragment(temperature);

        Navigation
                .findNavController(requireView())
                .navigate(action);
    }
}
