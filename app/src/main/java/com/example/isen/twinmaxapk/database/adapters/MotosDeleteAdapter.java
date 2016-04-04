package com.example.isen.twinmaxapk.database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;


import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.historic.Moto;

import java.util.List;

public class MotosDeleteAdapter extends BaseAdapter {

    private final List<Moto> motos;
    private final LayoutInflater inflater;
    private CheckBox checkBox;

    public MotosDeleteAdapter(List<Moto> motos, Context context) {
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


        ViewHolderMotosDelete holder;
        // If we don't have any convertView to reuse, inflate one
        if (null == convertView){
            convertView = inflater.inflate(R.layout.moto_delete_listitem, null);

            // Instantiate the ViewHolderMotos
            holder = new ViewHolderMotosDelete(convertView);
            // Set as tag to the convertView to retrieve it easily
            convertView.setTag(holder);
        } else {
            // Just retrieve the ViewHolderMotos instance in the tag of the view
            holder = (ViewHolderMotosDelete) convertView.getTag();
        }

        final Moto moto = (Moto) getItem(position);

        holder.getDate().setText(moto.getDate());
        holder.getName().setText(moto.getName());

        return convertView;
    }
}
