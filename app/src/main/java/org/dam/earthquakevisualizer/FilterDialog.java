package org.dam.earthquakevisualizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
            // Should we reset the filter back to all?
            if (operatorSpn.getSelectedItem().equals("Todas")
                    && countrySpn.getSelectedItem().toString().isEmpty()) {
                mainActivity.setFilterDao(earthquakeDao::getAll);
                return;
            }
            ExecutableFilter magnitudeFilter = null;
            ExecutableFilter countryFilter = null;
            if (!operatorSpn.getSelectedItem().equals("Todas")
                    && !valueEditText.getText().toString().isEmpty()) {
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
                magnitudeFilter = filterMap.get(operatorSpn.getSelectedItem());
            }
            if (!countrySpn.getSelectedItem().toString().isEmpty()) {
                countryFilter = () ->
                        earthquakeDao.getByCountry("%" + countrySpn.getSelectedItem() + "%");
            }
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
            else {
                // TODO validations failed...
            }
        }
    }
}