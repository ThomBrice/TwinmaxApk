package com.example.isen.twinmaxapk.database.historic;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by isen on 10/03/2016.
 */
public class Maintenance extends RealmObject {

    private String date;
    private String note;

    public Maintenance() {
    }

    public Maintenance(String  date, String note) {
        this.date = date;
        this.note = note;
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
}
