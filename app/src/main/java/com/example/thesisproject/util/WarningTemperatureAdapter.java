package com.example.thesisproject.util;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thesisproject.R;
import com.example.thesisproject.data.model.Temperature;

public class WarningTemperatureAdapter extends ListAdapter<Temperature, WarningTemperatureAdapter.WarningTemperatureViewHolder> {

    private OnItemClickListener onItemClickListener;

    private static final DiffUtil.ItemCallback<Temperature> DIFF_CALLBACK = new DiffUtil.ItemCallback<Temperature>() {
        @Override
        public boolean areItemsTheSame(@NonNull Temperature oldItem, @NonNull Temperature newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Temperature oldItem, @NonNull Temperature newItem) {
            return oldItem.getTemperature().equals(newItem.getTemperature()) &&
                    oldItem.getWarningTemperature().equals(newItem.getWarningTemperature()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    public WarningTemperatureAdapter(OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public WarningTemperatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.single_overflow_item,
                        parent,
                        false
                );

        return new WarningTemperatureViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WarningTemperatureViewHolder holder, int position) {
        Temperature temperature = getItem(position);

        holder.tv_temperature.setText(temperature.getTemperature() + "°C");
        holder.tv_temperatureHumidityPlaceHolder.setText("Temperature: ");
        holder.tv_date.setText(temperature.getDate());
    }

    public class WarningTemperatureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_temperature;
        private final TextView tv_temperatureHumidityPlaceHolder;
        private final TextView tv_date;

        public WarningTemperatureViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_temperature = itemView.findViewById(R.id.text_view_temperature);
            tv_temperatureHumidityPlaceHolder = itemView.findViewById(R.id.text_view_temperature_humidity);
            tv_date = itemView.findViewById(R.id.text_view_date);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Temperature temperature = getItem(position);

                if(temperature != null) {
                    onItemClickListener.onItemClick(temperature);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Temperature temperature);
    }
}
