package com.example.thesisproject.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.thesisproject.data.model.Humidity;
import com.example.thesisproject.data.model.Temperature;

@Database(entities = {Temperature.class, Humidity.class}, version = 1)
public abstract class WarningDatabase extends RoomDatabase {

    private static WarningDatabase instance;

    public abstract WarningDao getWarningDao();

    public static synchronized WarningDatabase getDatabaseInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context,
                    WarningDatabase.class,
                    "warning_database"
            ).build();
        }

        return instance;
    }

}
