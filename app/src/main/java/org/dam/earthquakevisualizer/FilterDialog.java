package org.dam.earthquakevisualizer;

import android.app.AlertDialog;
import android.app.Dialog;
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
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_filter_dialog,
                null);
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
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countryDao
                .getCountries()));
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.title_filter_dialog))
                .setView(v)
                .setPositiveButton(getResources().getText(R.string.filter_apply_btn), null)
                .setNegativeButton(getResources().getString(R.string.filter_cancel_btn),
                        (dialog, which) -> dialog.dismiss())
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
                if (!validateEarthquakeFilter())
                    return ;
                magnitudeFilter = generateEarthquakeFilter();
            }
            // Try with country filter
            if (countryChk.isChecked()) {
                countryFilter = () ->
                        earthquakeDao.getByCountry("%" + countrySpn.getSelectedItem() + "%");
                countryFilter = countryFilter.wrapStringOn((String) countrySpn.getSelectedItem());
            }
            // Both filters selected
            if (magnitudeFilter != null && countryFilter != null) {
                ExecutableFilter finalMagnitudeFilter = magnitudeFilter;
                ExecutableFilter finalCountryFilter = countryFilter;
                ExecutableFilter mixedFilter = () -> diffArrays(
                        (ArrayList<Earthquake>) finalMagnitudeFilter.run(),
                        (ArrayList<Earthquake>) finalCountryFilter.run());
                mainActivity.setFilterDao(mixedFilter.wrapStringOn(String
                        .format("%s en %s", magnitudeFilter, countryFilter)));
                dismiss();
            // Magnitude filter selected
            } else if (magnitudeFilter != null) {
                mainActivity.setFilterDao(magnitudeFilter);
                dismiss();
            // Country filter selected
            } else if (countryFilter != null) {
                mainActivity.setFilterDao(countryFilter);
                dismiss();
            // None
            } else {
                Toast.makeText(mainActivity, R.string.err_no_filter,
                        Toast.LENGTH_SHORT).show();
            }
        }

        private boolean validateEarthquakeFilter() {
            String allTxt = getResources().getStringArray(R.array.operators)[0];
            if (valueEditText.getText().toString().isEmpty() && !operatorSpn.getSelectedItem()
                    .equals(allTxt)) {
                valueEditText.setError(getString(R.string.err_number_value));
                return false;
            } else if (!valueEditText.getText().toString().isEmpty()) {
                if (operatorSpn.getSelectedItem().equals(allTxt)) {
                    Toast.makeText(mainActivity,
                            getResources().getText(R.string.err_operator_val),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Double.parseDouble(valueEditText.getText().toString()) > 10) {
                    valueEditText.setError(getString(R.string.err_maxed_value));
                    return false;
                }
            }
            return true;
        }

        public ExecutableFilter generateEarthquakeFilter() {
            ExecutableFilter magnitudeFilter;
            String allTxt = getResources().getStringArray(R.array.operators)[0];
            if (operatorSpn.getSelectedItem().equals(allTxt)) {
                magnitudeFilter = earthquakeDao::getAll;
                magnitudeFilter = magnitudeFilter.wrapStringOn(spnStr[0]);
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
                magnitudeFilter = filterMap.get(operatorSpn.getSelectedItem()).wrapStringOn(String
                        .format("%s %s", operatorSpn.getSelectedItem(), inputMagnitude));
            }
            return magnitudeFilter;
        }

        private ArrayList<Earthquake> diffArrays(ArrayList<Earthquake> magnitudeList,
                                                 ArrayList<Earthquake> countryList) {
            for (Earthquake magEarth : magnitudeList) {
                if (!countryList.contains(magEarth))
                    countryList.remove(magEarth);
            }
            return countryList;
        }
    }
}