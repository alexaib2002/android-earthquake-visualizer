package org.dam.earthquakevisualizer.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.dam.earthquakevisualizer.dao.CountryDao;
import org.dam.earthquakevisualizer.dao.EarthquakeDao;
import org.dam.earthquakevisualizer.javabeans.Country;
import org.dam.earthquakevisualizer.javabeans.Earthquake;

@Database(entities = {Country.class, Earthquake.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CountryDao countryDAO();
    public abstract EarthquakeDao earthquakeDAO();

    private static AppDatabase INSTANCE = null;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "earthquakes.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
