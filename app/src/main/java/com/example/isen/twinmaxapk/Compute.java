package com.example.isen.twinmaxapk;

import com.example.isen.twinmaxapk.database.Measure;
import com.example.isen.twinmaxapk.database.historic.Moto;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by isen on 11/03/2016.
 */
public class Compute {

    private static ArrayList<Measure> measures;
    private static Realm realm;

    public Compute() {
        measures = new ArrayList<>();
    }

    public static synchronized ArrayList<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(ArrayList<Measure> measures) {
        this.measures = measures;
    }

    public static Realm getRealm() {
        return realm;
    }

    public static void setRealm(Realm realm) {
        Compute.realm = realm;
    }

    public static synchronized void addMeasure(Measure measure) {
        measures.add(measure);
    }

    public void deleteOneCycleItems(int numberPoints){

        if (measures.size() > 3*numberPoints) {
            measures.subList(0, numberPoints - 1).clear();
        }
    }

    public void rpm(){

    }

    public void emptyDatabase(){
        if(realm !=null){
            realm.beginTransaction();
            realm.clear(Moto.class);
            realm.commitTransaction();
        }

    }

    public void sommeItemsInDatabase(){

        Moto moto1 = new Moto("Kawazaki Z750", "10/03/2016");
        Moto moto2 = new Moto("Suzuki GSXR1000", "02/02/2016");
        Moto moto3 = new Moto("Pan European", "28/01/2016");
        Moto moto4 = new Moto("Le vélo de Brice", "01/01/2016");

        realm.beginTransaction();
        realm.copyToRealm(moto1);
        realm.copyToRealm(moto2);
        realm.copyToRealm(moto3);
        realm.copyToRealm(moto4);
        realm.commitTransaction();


    }
}

