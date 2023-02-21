package org.dam.earthquakevisualizer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.dam.earthquakevisualizer.javabeans.Earthquake;

import java.util.ArrayList;

public class EarthquakeAdapter
        extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder> {
    private final ArrayList<Earthquake> earthquakeList;


    public EarthquakeAdapter(ArrayList<Earthquake> earthquakeList) {
        this.earthquakeList = earthquakeList;
    }

    @NonNull
    @Override
    public EarthquakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_earthquake_rv_item, parent, false);
        return new EarthquakeViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeViewHolder holder, int position) {
        Earthquake earthquake = earthquakeList.get(position);
        holder.getNameTv().setText(earthquake.getName());
        holder.getDateTv().setText(earthquake.getDate());
        holder.getMagnitudeTv().setText(String.valueOf(earthquake.getMagnitude()));
        holder.getLocationTv().setText(earthquake.getLocation());
        holder.getDeathTv().setText(earthquake.getDeathToll());
        holder.getCordsTv().setText(earthquake.getCords());
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public static class EarthquakeViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;
        private TextView dateTv;
        private TextView magnitudeTv;
        private TextView locationTv;
        private TextView deathTv;
        private TextView cordsTv;

        public EarthquakeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            magnitudeTv = itemView.findViewById(R.id.magnitudeTv);
            locationTv = itemView.findViewById(R.id.locationTv);
            deathTv = itemView.findViewById(R.id.deathTv);
            cordsTv = itemView.findViewById(R.id.cordsTv);
        }

        public TextView getNameTv() {
            return nameTv;
        }

        public TextView getDateTv() {
            return dateTv;
        }

        public TextView getMagnitudeTv() {
            return magnitudeTv;
        }

        public TextView getLocationTv() {
            return locationTv;
        }

        public TextView getDeathTv() {
            return deathTv;
        }

        public TextView getCordsTv() {
            return cordsTv;
        }
    }
}
