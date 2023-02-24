package org.dam.earthquakevisualizer.dao;

import androidx.room.Dao;
import androidx.room.Query;

import org.dam.earthquakevisualizer.javabeans.Earthquake;

import java.util.List;

@Dao
public interface EarthquakeDao {
    // Getter queries
    @Query("SELECT * FROM earthquakes")
    List<Earthquake> getAll();

    // Get all earthquakes with a magnitude greater or equal to the given magnitude
    @Query("SELECT * FROM earthquakes WHERE magnitude >= :magnitude")
    List<Earthquake> getGreaterOrEqualMagnitude(double magnitude);

    @Query("SELECT * FROM earthquakes WHERE magnitude > :magnitude")
    List<Earthquake> getGreaterMagnitude(double magnitude);

    @Query("SELECT * FROM earthquakes WHERE magnitude <= :magnitude")
    List<Earthquake> getLessOrEqualMagnitude(double magnitude);

    @Query("SELECT * FROM earthquakes WHERE magnitude < :magnitude")
    List<Earthquake> getLessMagnitude(double magnitude);

    @Query("SELECT * FROM earthquakes WHERE magnitude = :magnitude")
    List<Earthquake> getEqualMagnitude(double magnitude);

    // Get all earthquakes by given country
    @Query("SELECT * FROM earthquakes WHERE date IN (SELECT date FROM affected_countries WHERE country LIKE :country)")
    List<Earthquake> getByCountry(String country);

    // Insert queries
    @Query("INSERT INTO earthquakes VALUES (:date, :name, :magnitude, :cords, :location, :deathToll)")
    void insert(String date, String name, double magnitude, String cords, String location, String deathToll);
}
