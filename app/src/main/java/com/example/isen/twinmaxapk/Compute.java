package com.example.isen.twinmaxapk;

import com.example.isen.twinmaxapk.database.Measure;

import java.util.ArrayList;

/**
 * Created by isen on 11/03/2016.
 */
public class Compute {

    private ArrayList<Measure> measures;
    private int mRPM;
    private int nbPoint;

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
        boolean deuxiemeMin=false,premierMin=false;
        int i=1,min2=measures.get(0).get(0),min1=min2,index1;

        while(deuxiemeMin==false){
            if(measures.get(i).get(0)<=min1 && premierMin==false){
                min1=measures.get(i).get(0);
                i++;
                if(measures.get(i).get(0)>min1){
                    premierMin=true;
                    index1=i;
                }
            }
            if(premierMin==true){

            }


        }

    }
}

