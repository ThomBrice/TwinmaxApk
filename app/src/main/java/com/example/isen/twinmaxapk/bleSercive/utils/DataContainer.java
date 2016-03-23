package com.example.isen.twinmaxapk.bleSercive.utils;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import com.example.isen.twinmaxapk.database.Measure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 23/03/2016.
 */
public class DataContainer {
    private ObservableArrayList<Measure> dataContainer;

    public DataContainer(ObservableList.OnListChangedCallback callback) {
        dataContainer = new ObservableArrayList<>();
        dataContainer.addOnListChangedCallback(callback);
    }


    private boolean isFirst = true;
    private int[] precIndex = {0,0};
    public synchronized List<Measure> getSub(int indexMin, int indexMax) {
        if(isFirst) {
            isFirst = false;
        } else {
            for(int i=precIndex[0] ; i<=precIndex[1]; i++) {
                dataContainer.remove(0);
            }
        }
        try {
            precIndex[0] = indexMin;
            precIndex[1] = indexMax;
            return dataContainer.subList(indexMin, indexMax);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public synchronized void addValue(Measure newVal) {
        if(dataContainer != null) {
          //  Log.w("Taille data clean", "Taille CLEAN : " + dataContainer.size());
            dataContainer.add(newVal);
        }
    }
}
