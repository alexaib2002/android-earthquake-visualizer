package org.dam.earthquakevisualizer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dam.earthquakevisualizer.db.AppDatabase;
import org.dam.earthquakevisualizer.db.DbDataLoad;
import org.dam.earthquakevisualizer.interfaces.ExecutableFilter;
import org.dam.earthquakevisualizer.javabeans.Country;
import org.dam.earthquakevisualizer.javabeans.Earthquake;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button filterBtn;
    private Button queryBtn;
    private TextView selectedFilterTv;
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
        selectedFilterTv = findViewById(R.id.selectedFilterTv);

        layoutManager = new LinearLayoutManager(this);
        earthquakeRecView.setLayoutManager(layoutManager);
        earthquakeRecView.setAdapter(new EarthquakeAdapter(earthquakeList));

        filterBtn.setOnClickListener(v -> {
            FilterDialog filterDialog = new FilterDialog();
            filterDialog.setActivity(this);
            filterDialog.show(getSupportFragmentManager(), "filterDialog");
        });

        filterDao = AppDatabase.getInstance(this).earthquakeDAO()::getAll;
        setFilterDao(filterDao.wrapStringOn(getResources().getStringArray(R.array.operators)[0])); // set reference
        queryBtn.setOnClickListener(v -> {
            earthquakeList.clear();
            earthquakeList.addAll(filterDao.run());
            if (earthquakeList.size() == 0)
                Toast.makeText(this, getResources().getString(R.string.err_no_data_found),
                        Toast.LENGTH_SHORT).show();
            earthquakeRecView.getAdapter().notifyDataSetChanged();
        });
    }

    public void setFilterDao(ExecutableFilter filterDao) {
        this.filterDao = filterDao;
        selectedFilterTv.setText(filterDao.toString());
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
        }
    }
}