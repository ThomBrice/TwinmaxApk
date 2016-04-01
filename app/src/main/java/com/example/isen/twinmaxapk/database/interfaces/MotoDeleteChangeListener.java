package com.example.isen.twinmaxapk.database.interfaces;

import com.example.isen.twinmaxapk.database.historic.Moto;

import io.realm.RealmResults;

/**
 * Created by isen on 01/04/2016.
 */
public interface MotoDeleteChangeListener {

    void onMotoDeleteRetrieved(RealmResults<Moto> motos);
}
