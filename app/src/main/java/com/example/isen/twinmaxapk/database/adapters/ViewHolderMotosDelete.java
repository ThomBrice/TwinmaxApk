package com.example.isen.twinmaxapk.database.adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.isen.twinmaxapk.R;

/**
 * Created by isen on 01/04/2016.
 */
public class ViewHolderMotosDelete {

    private TextView name;
    private TextView date;
    private CheckBox checkBox;

    public ViewHolderMotosDelete(View view) {
        this.date = (TextView) view.findViewById(R.id.dateDeleteListItem);
        this.name = (TextView) view.findViewById(R.id.motoNameDeleteListItem);
        this.checkBox = (CheckBox) view.findViewById(R.id.checkboxDeleteListItem);
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }
}

