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
        // Init components
        operatorSpn = v.findViewById(R.id.operatorSpn);
        valueEditText = v.findViewById(R.id.valueEditText);
        countrySpn = v.findViewById(R.id.countrySpn);
        spnStr = getResources().getStringArray(R.array.operators);
        magnitudeChk = v.findViewById(R.id.magnitudeChk);
        countryChk = v.findViewById(R.id.countryChk);
        operatorSpn.setEnabled(false);
        valueEditText.setEnabled(false);
        countrySpn.setEnabled(false);
        // Set listeners
        magnitudeChk.setOnClickListener(v1 -> {
            valueEditText.setError(null);
            if (magnitudeChk.isChecked()) {
                operatorSpn.setEnabled(true);
                valueEditText.setEnabled(true);
                return;
            }
            operatorSpn.setEnabled(false);
            valueEditText.setEnabled(false);
        });
        countryChk.setOnClickListener(v2 -> {
            if (countryChk.isChecked()) {
                countrySpn.setEnabled(true);
                return;
            }
            countrySpn.setEnabled(false);
        });
        countrySpn.setAdapter(new ArrayAdapter<>(v.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countryDao.getCountries()));
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Selección de filtro")
                .setView(v)
                .setPositiveButton("Aplicar filtro", null)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(acceptListener);
        return alertDialog;
    }

    public void setActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        earthquakeDao = AppDatabase.getInstance(mainActivity).earthquakeDAO();
        countryDao = AppDatabase.getInstance(mainActivity).countryDAO();
    }

    private class FilterAcceptListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ExecutableFilter magnitudeFilter = null;
            ExecutableFilter countryFilter = null;
            // Try with magnitude filter
            if (magnitudeChk.isChecked()) {
                if (valueEditText.getText().toString().isEmpty() && !operatorSpn.getSelectedItem()
                        .equals("Todas")) {
                    valueEditText.setError("Debe introducir un valor numérico");
                    return;
                } else if (operatorSpn.getSelectedItem().equals("Todas")) {
                    magnitudeFilter = earthquakeDao::getAll;
                } else {
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
                dismiss();
            } else if (magnitudeFilter != null) {
                mainActivity.setFilterDao(magnitudeFilter);
                dismiss();
            }
            else if (countryFilter != null) {
                mainActivity.setFilterDao(countryFilter);
                dismiss();
            }
            else {
                Toast.makeText(mainActivity, "Debe seleccionar al menos un filtro", Toast.LENGTH_SHORT).show();
            }
        }
    }
}