package com.example.isen.twinmaxapk.database.historic;



import com.example.isen.twinmaxapk.database.Measure;
import com.example.isen.twinmaxapk.database.RealmMeasure;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Maintenance extends RealmObject implements Serializable {

    private String date;
    private String note;
    private RealmList<RealmMeasure> measures;

    public Maintenance() {
    }

    public Maintenance(String  date, String note) {
        this.date = date;
        this.note = note;
    }

    public Maintenance(String date, String note, ArrayList<Measure> measures1) {
        this.date = date;
        this.note = note;
        measures = new RealmList<>();
        int c0;
        int c1;
        int c2;
        int c3;
        for(int i=0;i<measures1.size();i++){
            c0 = measures1.get(i).get(0);
            c1 = measures1.get(i).get(1);
            c2 = measures1.get(i).get(2);
            c3 = measures1.get(i).get(3);
            RealmMeasure realmMeasure = new RealmMeasure(c0,c1,c2,c3);
            this.measures.add(realmMeasure);
        }

    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RealmList<RealmMeasure> getMeasures() {
        return measures;
    }

    public void setMeasures(RealmList<RealmMeasure> measures) {
        this.measures = measures;
    }
}
