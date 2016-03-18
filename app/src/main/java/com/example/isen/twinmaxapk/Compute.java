package com.example.isen.twinmaxapk;

import com.example.isen.twinmaxapk.database.Measure;

import java.util.ArrayList;

/**
 * Created by isen on 11/03/2016.
 */
public class Compute {

    private ArrayList<Measure> measures;

    public Compute() {
        measures = new ArrayList<>();
    }

    public ArrayList<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(ArrayList<Measure> measures) {
        this.measures = measures;
    }

    public void addMeasure(Measure measure){
        measures.add(measure);
    }

    public void deleteOneCycleItems(int numberPoints){

        if (measures.size() > 3*numberPoints) {
            measures.subList(0, numberPoints - 1).clear();
        }
    }

    public void rpm(){

    }
}

