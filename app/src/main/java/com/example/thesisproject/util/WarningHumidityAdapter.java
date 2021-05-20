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
import com.example.thesisproject.data.model.Humidity;
import com.example.thesisproject.data.model.Temperature;

public class WarningHumidityAdapter extends ListAdapter<Humidity, WarningHumidityAdapter.WarningTemperatureViewHolder> {

    private OnItemClickListener onItemClickListener;

    private static final DiffUtil.ItemCallback<Humidity> DIFF_CALLBACK = new DiffUtil.ItemCallback<Humidity>() {
        @Override
        public boolean areItemsTheSame(@NonNull Humidity oldItem, @NonNull Humidity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Humidity oldItem, @NonNull Humidity newItem) {
            return oldItem.getHumidity().equals(newItem.getHumidity()) &&
                    oldItem.getWarningHumidity().equals(newItem.getWarningHumidity()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    public WarningHumidityAdapter(OnItemClickListener onItemClickListener) {
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
        Humidity humidity = getItem(position);

        holder.tv_humidity.setText(humidity.getHumidity() + "%");
        holder.tv_temperatureHumidityPlaceHolder.setText("Humidity: ");
        holder.tv_date.setText(humidity.getDate());
    }

    public class WarningTemperatureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView tv_humidity;
        private final TextView tv_temperatureHumidityPlaceHolder;
        private final TextView tv_date;

        public WarningTemperatureViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_humidity = itemView.findViewById(R.id.text_view_temperature);
            tv_temperatureHumidityPlaceHolder = itemView.findViewById(R.id.text_view_temperature_humidity);
            tv_date = itemView.findViewById(R.id.text_view_date);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Humidity humidity = getItem(position);

                if(humidity != null) {
                    onItemClickListener.onItemClick(humidity);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Humidity humidity);
    }
}
