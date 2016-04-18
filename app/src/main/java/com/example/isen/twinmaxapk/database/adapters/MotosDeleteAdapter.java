package com.example.isen.twinmaxapk.database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;

import java.util.List;

import io.realm.RealmResults;

public class MotosDeleteAdapter extends BaseAdapter {

    private final List<Moto> motos;
    private final LayoutInflater inflater;
    private CheckBox checkBox;
    private Button delete;

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



        final View view = inflater.inflate(R.layout.moto_delete_listitem, null);

        final Moto moto = (Moto) getItem(position);

        final TextView name = (TextView) view.findViewById(R.id.motoNameDeleteListItem);
        name.setText(moto.getName());

        final TextView date = (TextView) view.findViewById(R.id.dateDeleteListItem);
        date.setText(moto.getDate());

        checkBox = (CheckBox) view.findViewById(R.id.checkboxDeleteListItem);

        return view;
    }

    public void deleteItemSelected(int i){
        if (checkBox.isChecked()){


            Moto m = (Moto) getItem(i);


            Compute.getRealm().beginTransaction();
            ((Moto) getItem(i)).removeFromRealm();
            Compute.getRealm().commitTransaction();
        }
    }
}
