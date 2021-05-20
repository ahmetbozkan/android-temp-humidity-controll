package com.example.thesisproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesisproject.R;
import com.example.thesisproject.data.model.Humidity;
import com.example.thesisproject.util.WarningHumidityAdapter;

import java.util.List;

public class WarningHumiditiesFragment extends Fragment implements WarningHumidityAdapter.OnItemClickListener {

    private ServiceViewModel serviceViewModel;

    private WarningHumidityAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceViewModel.getAllWarningHumidities().observe(getViewLifecycleOwner(), humidities -> {
            adapter.submitList(humidities);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_warning_humidities, container, false);
        buildRecyclerView(v);

        serviceViewModel = new ViewModelProvider(requireActivity())
                .get(ServiceViewModel.class);

        return v;
    }

    private void buildRecyclerView(View v) {
        RecyclerView rv = v.findViewById(R.id.rv_warning_humidities);
        rv.setHasFixedSize(true);

        adapter = new WarningHumidityAdapter(this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Humidity humidity) {
        NavDirections action = WarningHumiditiesFragmentDirections
                .actionWarningHumiditiesFragment2ToWarningHumidityDetailsFragment(humidity);

        Navigation
                .findNavController(requireView())
                .navigate(action);
    }
}
