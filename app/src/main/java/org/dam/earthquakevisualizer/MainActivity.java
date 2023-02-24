package org.dam.earthquakevisualizer;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dam.earthquakevisualizer.dao.EarthquakeDao;
import org.dam.earthquakevisualizer.db.AppDatabase;
import org.dam.earthquakevisualizer.db.DbDataLoad;
import org.dam.earthquakevisualizer.interfaces.ExecutableFilter;
import org.dam.earthquakevisualizer.javabeans.Country;
import org.dam.earthquakevisualizer.javabeans.Earthquake;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button filterBtn;
    private Button queryBtn;
    private RecyclerView earthquakeRecView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Earthquake> earthquakeList = new ArrayList<>();
    private ExecutableFilter filterDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDbLoad();

        filterBtn = findViewById(R.id.filterBtn);
        queryBtn = findViewById(R.id.queryBtn);
        earthquakeRecView = findViewById(R.id.earthquakeRecView);

        layoutManager = new LinearLayoutManager(this);
        earthquakeRecView.setLayoutManager(layoutManager);
        earthquakeRecView.setAdapter(new EarthquakeAdapter(earthquakeList));

        filterBtn.setOnClickListener(v -> {
            FilterDialog filterDialog = new FilterDialog();
            filterDialog.setActivity(this);
            filterDialog.show(getSupportFragmentManager(), "filterDialog");
        });

        filterDao = AppDatabase.getInstance(this).earthquakeDAO()::getAll; // set reference
        queryBtn.setOnClickListener(v -> {
            earthquakeList.clear();
            earthquakeList.addAll(filterDao.run());
            earthquakeRecView.getAdapter().notifyDataSetChanged();
        });
    }

    public void setFilterDao(ExecutableFilter filterDao) {
        this.filterDao = filterDao;
    }

    private void initDbLoad() {
        AppDatabase db = AppDatabase.getInstance(this);
        if (db.earthquakeDAO().getAll().size() == 0 && db.countryDAO().getAll().size() == 0) {
            for (Earthquake earthquake : DbDataLoad.EARTHQUAKES){
                db.earthquakeDAO().insert(earthquake.date, earthquake.name, earthquake.magnitude,
                        earthquake.cords, earthquake.location, earthquake.deathToll);
            }
            for (Country country : DbDataLoad.COUNTRIES) {
                db.countryDAO().insert(country.date, country.country);
            }
            System.out.println("DB was initialized with new data");
        }
        else
            System.out.println("Skipping initialization of DB");
    }
}