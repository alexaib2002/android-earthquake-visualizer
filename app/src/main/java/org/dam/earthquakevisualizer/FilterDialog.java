package org.dam.earthquakevisualizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.dam.earthquakevisualizer.dao.CountryDao;
import org.dam.earthquakevisualizer.dao.EarthquakeDao;
import org.dam.earthquakevisualizer.db.AppDatabase;
import org.dam.earthquakevisualizer.interfaces.ExecutableFilter;
import org.dam.earthquakevisualizer.javabeans.Earthquake;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterDialog extends DialogFragment {

    private FilterAcceptListener acceptListener;
    protected EarthquakeDao earthquakeDao;
    protected CountryDao countryDao;
    protected Spinner operatorSpn;
    protected TextView valueEditText;
    protected Spinner countrySpn;
    protected MainActivity mainActivity;
    protected CheckBox magnitudeChk;
    protected CheckBox countryChk;
    protected String[] spnStr;

    public FilterDialog() {
        acceptListener = new FilterAcceptListener();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_filter_dialog, null);
        operatorSpn = v.findViewById(R.id.operatorSpn);
        valueEditText = v.findViewById(R.id.valueEditText);
        countrySpn = v.findViewById(R.id.countrySpn);
        spnStr = getResources().getStringArray(R.array.operators);
        magnitudeChk = v.findViewById(R.id.magnitudeChk);
        countryChk = v.findViewById(R.id.countryChk);
        countrySpn.setAdapter(new ArrayAdapter<>(v.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countryDao.getCountries()));
        return new AlertDialog.Builder(getActivity())
                .setTitle("Selección de filtro")
                .setView(v)
                .setPositiveButton("Aplicar filtro", acceptListener)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create();
    }

    public void setActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        earthquakeDao = AppDatabase.getInstance(mainActivity).earthquakeDAO();
        countryDao = AppDatabase.getInstance(mainActivity).countryDAO();
    }

    private class FilterAcceptListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (operatorSpn.getSelectedItem().equals("Todas") // Filter reset
                    && countrySpn.getSelectedItem().toString().isEmpty()) {
                mainActivity.setFilterDao(earthquakeDao::getAll);
                return;
            }
            ExecutableFilter magnitudeFilter = null;
            ExecutableFilter countryFilter = null;
            // Try with magnitude filter
            if (magnitudeChk.isChecked()) {
                magnitudeFilter = getMagnitudeFilter();
            }
            // Try with country filter
            if (countryChk.isChecked()) {
                countryFilter = () ->
                        earthquakeDao.getByCountry("%" + countrySpn.getSelectedItem() + "%");
            }
            // Match which filter must be applied
            if (magnitudeFilter != null && countryFilter != null) {
                // static final copies of vars
                @NotNull ExecutableFilter finalMagnitudeFilter = magnitudeFilter;
                @NotNull ExecutableFilter finalCountryFilter = countryFilter;
                mainActivity.setFilterDao(() -> {
                    ArrayList<Earthquake> countryList = new ArrayList<>(finalCountryFilter.run());
                    ArrayList<Earthquake> bothList = new ArrayList<>();
                    for (Earthquake magEarth : finalMagnitudeFilter.run()) {
                        if (countryList.contains(magEarth))
                            bothList.add(magEarth);
                    }
                    return bothList;
                });
            } else if (magnitudeFilter != null)
                mainActivity.setFilterDao(magnitudeFilter);
            else if (countryFilter != null)
                mainActivity.setFilterDao(countryFilter);
        }

        private ExecutableFilter getMagnitudeFilter() {
            if (valueEditText.getText().toString().isEmpty()) {
                valueEditText.setError("Debe introducir un valor numérico");
                return null;
            }
            if (operatorSpn.getSelectedItem().equals("Todas")) {
                operatorSpn.requestFocus();
                Toast.makeText(mainActivity,
                        "Debes seleccionar un filtro para el valor numerico introducido",
                        Toast.LENGTH_SHORT).show();
                return null;
            }
            Double inputMagnitude = Double
                    .parseDouble(valueEditText.getText().toString());
            HashMap<String, ExecutableFilter> filterMap = new
                    HashMap<String, ExecutableFilter>() {{
                        put(spnStr[1], () ->
                                earthquakeDao.getGreaterMagnitude(inputMagnitude));
                        put(spnStr[2], () ->
                                earthquakeDao.getLessMagnitude(inputMagnitude));
                        put(spnStr[3], () ->
                                earthquakeDao.getEqualMagnitude(inputMagnitude));
                        put(spnStr[4], () ->
                                earthquakeDao.getGreaterOrEqualMagnitude(inputMagnitude));
                        put(spnStr[5], () ->
                                earthquakeDao.getLessOrEqualMagnitude(inputMagnitude));
                    }};
            // Set the filter in MainActivity
            return filterMap.get(operatorSpn.getSelectedItem());
        }
    }
}