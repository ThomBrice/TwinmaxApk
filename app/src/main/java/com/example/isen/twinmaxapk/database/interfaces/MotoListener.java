package com.example.isen.twinmaxapk.database.interfaces;

import com.example.isen.twinmaxapk.database.historic.Moto;

/**
 * Created by isen on 21/03/2016.
 */
public interface MotoListener {

    void onViewMaintenance(Moto moto);
    void deleteItem(Moto moto);
}
