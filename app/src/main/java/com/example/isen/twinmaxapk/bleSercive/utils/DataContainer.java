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

    public boolean isEmpty() {
        return dataContainer.isEmpty();
    }


    public void resetStatics() {
        isFirst = false;
    }

    private static boolean isFirst = true;
    private int[] precIndex = {0,0};
    public  List<Measure> getSub(int indexMin, int indexMax) {
        synchronized (dataContainer) {
            if (isFirst) {
                isFirst = false;
            } else {
                for (int i = precIndex[0]; i <= precIndex[1]; i++) {
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
    }

    private boolean isFFirst = true;
    public Measure getFirst() {
        synchronized (dataContainer) {
            if (isFFirst) {
                isFFirst = false;
            } else if (!dataContainer.isEmpty()) {
                dataContainer.remove(0);
            }
            if (!dataContainer.isEmpty()) {
                return dataContainer.get(0);
            } else {
                return null;
            }
        }
    }

    public int sizeOfList() {
        return dataContainer.size();
    }

    public void addValue(Measure newVal) {
        synchronized (dataContainer) {
            if (dataContainer != null) {
                //  Log.w("Taille data clean", "Taille CLEAN : " + dataContainer.size());
                dataContainer.add(newVal);
            }
        }
    }
}
