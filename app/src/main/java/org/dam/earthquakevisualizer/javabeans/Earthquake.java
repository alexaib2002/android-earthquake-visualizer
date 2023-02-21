package org.dam.earthquakevisualizer.javabeans;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "earthquakes",
        indices = {
                @Index(value = {"date", "device_name"}, unique = true)
        }
)
public class Earthquake {
    @PrimaryKey
    @NonNull
    public String date = "1970-01-01 00:00:00";

    @ColumnInfo(name = "device_name")
    public String name;

    @ColumnInfo(name = "magnitude")
    public double magnitude;

    @ColumnInfo(name = "cords")
    public String cords;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "death_toll")
    public String deathToll;

    public Earthquake(@NonNull String date, double magnitude, String name,
                      String location, String cords, String deathToll) {
        this.date = date;
        this.name = name;
        this.magnitude = magnitude;
        this.cords = cords;
        this.location = location;
        this.deathToll = deathToll;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getCords() {
        return cords;
    }

    public String getLocation() {
        return location;
    }

    public String getDeathToll() {
        return deathToll;
    }
}
