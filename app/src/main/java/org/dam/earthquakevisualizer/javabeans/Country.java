package org.dam.earthquakevisualizer.javabeans;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "affected_countries",
        indices = {
                @Index(value = {"date", "country"}, unique = true)
        },
        primaryKeys = {"date", "country"},
        foreignKeys = {
                @ForeignKey(
                        entity = Earthquake.class,
                        parentColumns = {"date"},
                        childColumns = {"date"},
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Country {
    @NonNull
    public String date = "1970-01-01 00:00:00";

    @NonNull
    public String country = "Unknown";

    public Country(@NonNull String date, @NonNull String country) {
        this.date = date;
        this.country = country;
    }
}
