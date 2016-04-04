package com.example.isen.twinmaxapk.database.interfaces;

import com.example.isen.twinmaxapk.database.historic.Maintenance;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by isen on 22/03/2016.
 */
public interface MaintenanceChangeListener {

    void onMotoRetrieved(RealmList<Maintenance> maintenances);
}
