package com.example.isen.twinmaxapk.database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.historic.Maintenance;

import java.util.List;

/**
 * Created by isen on 20/03/2016.
 */
public class MaintenancesAdapter extends BaseAdapter {

    private final List<Maintenance> maintenances;
    private final LayoutInflater inflater;

    public MaintenancesAdapter(List<Maintenance> maintenances, Context context) {
        this.maintenances = maintenances;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null != maintenances ? maintenances.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null != maintenances ? maintenances.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.maintenance_listitem, null);

        final Maintenance maintenance = (Maintenance) getItem(position);

        final TextView name = (TextView) view.findViewById(R.id.details);
        name.setText(maintenance.getNote());

        final TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(maintenance.getDate());


        return view;
    }
}
