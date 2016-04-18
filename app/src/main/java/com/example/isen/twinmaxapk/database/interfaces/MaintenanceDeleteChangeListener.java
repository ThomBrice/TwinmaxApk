package com.example.isen.twinmaxapk.database.interfaces;

import com.example.isen.twinmaxapk.database.historic.Maintenance;

import io.realm.RealmList;

public interface MaintenanceDeleteChangeListener {

    void onMaintenanceDeleteRetrieved(RealmList<Maintenance> maintenances);
}
