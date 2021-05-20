package com.example.thesisproject.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "humidity_overflows")
public class Humidity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "humidity")
    public String humidity;

    @ColumnInfo(name = "warning_humidity")
    public String warningHumidity;

    @ColumnInfo(name = "difference")
    public String difference;

    @ColumnInfo(name = "date")
    public String date;

    public Humidity() {
    }

    public Humidity(String humidity, String warningHumidity, String difference, String date) {
        this.humidity = humidity;
        this.warningHumidity = warningHumidity;
        this.difference = difference;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWarningHumidity() {
        return warningHumidity;
    }

    public void setWarningHumidity(String warningHumidity) {
        this.warningHumidity = warningHumidity;
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
