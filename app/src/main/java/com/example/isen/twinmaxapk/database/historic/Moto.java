package com.example.isen.twinmaxapk.database.historic;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by isen on 10/03/2016.
 */
public class Moto extends RealmObject {

    private String name;
    private RealmList<Maintenance> maintenances;

    public Moto() {
    }

    public Moto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(RealmList<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }
}
