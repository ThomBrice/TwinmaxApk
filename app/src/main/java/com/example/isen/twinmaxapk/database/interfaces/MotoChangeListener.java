package com.example.isen.twinmaxapk.database.interfaces;


import com.example.isen.twinmaxapk.database.historic.Moto;

import io.realm.RealmResults;

public interface MotoChangeListener {

    void onMotoRetrieved(RealmResults<Moto> motos);
}
