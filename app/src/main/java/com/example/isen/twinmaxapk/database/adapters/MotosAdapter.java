package com.example.isen.twinmaxapk.database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.historic.Moto;

import java.util.List;

public class MotosAdapter extends BaseAdapter {

    private final List<Moto> motos;
    private final LayoutInflater inflater;

    public MotosAdapter(List<Moto> motos, Context context) {
        this.motos = motos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null != motos ? motos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null != motos ? motos.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.moto_listitem, null);

        final Moto moto = (Moto) getItem(position);

        final TextView name = (TextView) view.findViewById(R.id.motoName);
        name.setText(moto.getName());

        final TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(moto.getDate());


        return view;
    }
}
