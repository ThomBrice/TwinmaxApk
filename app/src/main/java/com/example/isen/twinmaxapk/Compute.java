package com.example.isen.twinmaxapk;

import com.example.isen.twinmaxapk.database.Measure;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by isen on 11/03/2016.
 */
public class Compute {

    private static ArrayList<Measure> measures;
    private static Realm realm;

    public Compute() {
        measures = new ArrayList<>();
    }


    /**
     * @param rangeMin should be 0 most of the times
     * @param rangeMax users choose
     * @return
     */
    private static boolean wasFirstRemovingMeasures = true;
    private static int[] range = {0, 0};

    public static synchronized List<Measure> getMeasures(int rangeMin, int rangeMax) {
        if (!wasFirstRemovingMeasures) {
            for (int i = range[1]; i >= range[0]; i--) {
                measures.remove(i);
            }
        }
        if (rangeMin >= 0 && rangeMax < measures.size()) {
            wasFirstRemovingMeasures = false;
            range[0] = rangeMin;
            range[1] = rangeMax;
            return measures.subList(rangeMin, rangeMax);
        }
        return null;
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

    public void someItemsInDatabase(){


        Moto moto1 = new Moto("Kawazaki Z750", "10/03/2016");
        Moto moto2 = new Moto("Suzuki GSXR1000", "02/02/2016");
        Moto moto3 = new Moto("Pan European", "28/01/2016");
        Moto moto4 = new Moto("Le vélo de Brice", "01/01/2016");

        RealmList<Maintenance> maintenances = new RealmList<>();
        Maintenance maintenance = new Maintenance("22/02/2012","RAS");
        Maintenance maintenance1 = new Maintenance("22/02/2011","Problème carbu");
        maintenances.add(maintenance);
        maintenances.add(maintenance1);

        moto1.setMaintenances(maintenances);
        moto2.setMaintenances(maintenances);
        moto3.setMaintenances(maintenances);
        moto3.setMaintenances(maintenances);

        realm.beginTransaction();
        realm.copyToRealm(moto1);
        realm.copyToRealm(moto2);
        realm.copyToRealm(moto3);
        realm.copyToRealm(moto4);
        realm.commitTransaction();
    }

    public static void addMoto(Moto moto){

        realm.beginTransaction();
        realm.copyToRealm(moto);
        realm.commitTransaction();
    }
}

