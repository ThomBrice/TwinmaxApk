package com.example.isen.twinmaxapk.bleSercive.utils;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import com.example.isen.twinmaxapk.database.Measure;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 23/03/2016.
 */
public class DataContainer {
    private ObservableArrayList<Measure> dataContainer;
    private int graphToSkip;
    private int sizeOfGraph;


    public int getSizeOfList() {
        synchronized (dataContainer) {
            return dataContainer.size();
        }
    }

    public void setSizeOfGraph(int sizeOfGraph) {
        this.sizeOfGraph = sizeOfGraph;
    }

    public int getSizeOfGraph() {
        return sizeOfGraph;
    }

    public DataContainer(ObservableList.OnListChangedCallback callback) {
        dataContainer = new ObservableArrayList<>();
        dataContainer.addOnListChangedCallback(callback);
        graphToSkip = 0;
        sizeOfGraph = 200;
    }

    public boolean isEmpty() {
        synchronized (dataContainer) {
            return dataContainer.isEmpty();
        }
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

    private boolean isFFirst = true;

    public List<Measure> getGraphValues() {
        synchronized (dataContainer) {
            List<Measure> tempList = new ArrayList<>();
            for (int i = 0; i < sizeOfGraph; i++) {
                if (!dataContainer.isEmpty() && dataContainer.size() > 0) {
                    tempList.add(dataContainer.remove(0));
                }
            }


            if (mSkipsGraph == null || mSkipsGraph.getState().compareTo(Thread.State.TERMINATED) == 0) {
                mSkipsGraph = new SkipGraphs();
                mSkipsGraph.start();
            }

            return tempList;
            //return dataContainer.subList(0,200);
        }
    }
    private SkipGraphs mSkipsGraph;
    private class SkipGraphs extends Thread {
        @Override
        public void run() {
            synchronized(dataContainer) {
                for (int i = 0; i < graphToSkip; i++) {
                    getGraphValues();
                }
            }
        }
    }


    public synchronized Measure getFirst() {
        if(isFFirst) {
            isFFirst = false;
        } else if(!dataContainer.isEmpty() && dataContainer.size() > 0){
            dataContainer.remove(0);
        }
        if(!dataContainer.isEmpty()) {
            return dataContainer.get(0);
        } else {
            return null;
        }
    }

    public int sizeOfList() {
        return dataContainer.size();
    }


    public int getGraphToSkip() {
        return graphToSkip;
    }

    public void addValue(Measure newVal) {
        synchronized (dataContainer) {
            if (dataContainer != null) {

                dataContainer.add(newVal);
                graphToSkip = (int) (dataContainer.size() / 1000) + 2;
                //Log.e("Taille data clean", "Taille CLEAN : " + dataContainer.size() + "\n" +"TO SKIP : " + graphToSkip);
            }
        }
    }
}
