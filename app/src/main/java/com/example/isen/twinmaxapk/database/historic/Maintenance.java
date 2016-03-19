package com.example.isen.twinmaxapk.database.historic;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by isen on 10/03/2016.
 */
public class Maintenance extends RealmObject {

    private Date date;
    private String note;

    public Maintenance() {
    }

    public Maintenance(Date date, String note) {
        this.date = date;
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
