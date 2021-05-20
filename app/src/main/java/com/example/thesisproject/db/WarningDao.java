package com.example.thesisproject.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.thesisproject.data.model.Humidity;
import com.example.thesisproject.data.model.Temperature;

import java.util.List;

@Dao
public interface WarningDao {

    @Insert
    void insertTemperature(Temperature temperature);

    @Insert
    void insertHumidity(Humidity humidity);

    @Query("SELECT * FROM temperature_overflows ORDER BY id DESC")
    LiveData<List<Temperature>> getAllTemperatureOverflows();

    @Query("SELECT * FROM humidity_overflows ORDER BY id DESC")
    LiveData<List<Humidity>> getAllHumidityOverflows();

}
