package org.dam.earthquakevisualizer.dao;

import androidx.room.Dao;
import androidx.room.Query;

import org.dam.earthquakevisualizer.javabeans.Country;

import java.util.List;

@Dao
public interface CountryDao {
    @Query("SELECT * FROM affected_countries")
    List<Country> getAll();
    @Query("SELECT DISTINCT country FROM affected_countries ORDER BY country ASC")
    String[] getCountries();

    @Query("INSERT INTO affected_countries VALUES (:date, :country)")
    void insert(String date, String country);
}
