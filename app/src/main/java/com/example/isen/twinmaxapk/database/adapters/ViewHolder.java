package com.example.isen.twinmaxapk.database.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.isen.twinmaxapk.R;

/**
 * Created by isen on 22/03/2016.
 */
public class ViewHolder {

    private TextView name;
    private TextView date;

    public ViewHolder(View view) {
        this.date = (TextView) view.findViewById(R.id.dateListItem);
        this.name = (TextView) view.findViewById(R.id.motoNameListItem);
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
