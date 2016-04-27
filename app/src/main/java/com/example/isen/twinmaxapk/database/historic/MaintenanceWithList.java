package com.example.isen.twinmaxapk.database.historic;

import com.example.isen.twinmaxapk.database.Measure;
import com.example.isen.twinmaxapk.database.RealmMeasure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by isen on 27/04/2016.
 */
public class MaintenanceWithList implements Serializable {

    private String date;
    private String note;
    private ArrayList<RealmMeasure> measures;

    public MaintenanceWithList() {
    }

    public MaintenanceWithList(String date, String note) {
        this.date = date;
        this.note = note;
        measures = new ArrayList<>();
    }

    public MaintenanceWithList(String date, String note, ArrayList<Measure> measures1) {
        this.date = date;
        this.note = note;
        measures = new ArrayList<>();
        int c0;
        int c1;
        int c2;
        int c3;
        for(int i=0;i<measures1.size();i++){
            c0 = measures1.get(i).get(0);
            c1 = measures1.get(i).get(1);
            c2 = measures1.get(i).get(2);
            c3 = measures1.get(i).get(3);
            RealmMeasure Measure = new RealmMeasure(c0,c1,c2,c3);
            this.measures.add(Measure);
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<RealmMeasure> getMeasures() {
        return measures;
    }

    public void setMeasures(ArrayList<RealmMeasure> measures) {
        this.measures = measures;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

