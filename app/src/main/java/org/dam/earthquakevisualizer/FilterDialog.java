package org.dam.earthquakevisualizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FilterDialog extends DialogFragment {

    public FilterDialog() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Selección de filtro")
                .setView(getActivity().getLayoutInflater().inflate(R.layout.fragment_filter_dialog, null))
                .setPositiveButton("Aplicar filtro", null) // FIXME pass lambda from mainActivity
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
    }
}