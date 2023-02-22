package org.dam.earthquakevisualizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.widget.Spinner;
import android.widget.TextView;

import org.dam.earthquakevisualizer.dao.EarthquakeDao;
import org.dam.earthquakevisualizer.interfaces.ExecutableFilter;

import java.util.HashMap;

public class FilterDialog extends DialogFragment {

    private FilterAcceptListener acceptListener;
    protected EarthquakeDao earthquakeDao;
    protected Spinner operatorSpn;
    protected TextView valueEditText;
    protected TextView countryEditText;
    protected MainActivity mainActivity;

    public FilterDialog() {
        acceptListener = new FilterAcceptListener();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Selección de filtro")
                .setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_filter_dialog, null))
                .setPositiveButton("Aplicar filtro", acceptListener)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create();
    }

    public void setActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private class FilterAcceptListener implements DialogInterface.OnClickListener {
        // Mapa que relaciona el tipo de filtro con una lambda para realizar la consulta DAO
        final HashMap<String, ExecutableFilter> magnitudeFilters = new HashMap<String, ExecutableFilter>() {{
            put("Mayor que", () -> earthquakeDao.getGreaterMagnitude(Double
                    .parseDouble(valueEditText.getText().toString())));
            put("Menor que", () -> earthquakeDao.getLessMagnitude(Double
                    .parseDouble(valueEditText.getText().toString())));
            put("Igual que", () -> earthquakeDao.getEqualMagnitude(Double
                    .parseDouble(valueEditText.getText().toString())));
            put("Mayor o igual que", () -> earthquakeDao.getGreaterOrEqualMagnitude(Double
                    .parseDouble(valueEditText.getText().toString())));
            put("Menor o igual que", () -> earthquakeDao.getLessOrEqualMagnitude(Double
                    .parseDouble(valueEditText.getText().toString())));
        }};
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (((String) operatorSpn.getSelectedItem()).equals("Todas")
                    && countryEditText.getText().toString().isEmpty())
                return;
            // set the filter in MainActivity
            mainActivity.setFilterDao(magnitudeFilters.get(operatorSpn.getSelectedItem()));
        }
    }
}