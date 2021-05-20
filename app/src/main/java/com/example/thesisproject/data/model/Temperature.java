package com.example.thesisproject.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "temperature_overflows")
public class Temperature implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "temperature")
    public String temperature;

    @ColumnInfo(name = "warning_temperature")
    public String warningTemperature;

    @ColumnInfo(name = "difference")
    public String difference;

    @ColumnInfo(name = "date")
    public String date;

    public Temperature() {}

    public Temperature(String temperature, String warningTemperature, String difference, String date) {
        this.temperature = temperature;
        this.warningTemperature = warningTemperature;
        this.difference = difference;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWarningTemperature() {
        return warningTemperature;
    }

    public void setWarningTemperature(String warningTemperature) {
        this.warningTemperature = warningTemperature;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
