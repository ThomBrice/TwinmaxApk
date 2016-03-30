package com.example.isen.twinmaxapk.bleSercive.utils;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

/**
 * Created by Matthieu on 21/03/2016.
 */
public class RawContainer {
    private ObservableArrayList<Byte> container;
    public FrameState rawFrameState;



    public void logContent() {
        int i=0;
        for(Byte b:container) {
            Log.e("Relecture rawData", "indice " + i + "- " + b.toString());
            i++;
        }
    }
    public boolean isRawContainerEmpty() {
        return container.isEmpty();
    }

    public RawContainer(ObservableList.OnListChangedCallback changeCallback) {
        container = new ObservableArrayList();
        container.addOnListChangedCallback(changeCallback);
        rawFrameState = FrameState.END;
    }

    public void addFrame(byte[] data){
        synchronized (container) {
            if (container != null) {
                for (byte b : data) {
                    container.add(new Byte(b));
                    //container.add(0, new Byte(b));
                }
                // Log.w("Size", "value : " + container.size());
            }
        }
    }
    public void resetStatic() {
        isFirst = false;
    }
    public static boolean isFirst = true;
    public byte getFirst() {
        synchronized (container) {
            byte b = 0;
            if (!isFirst) {
                container.remove(0);
                //container.remove(container.size() - 1);

            }
            if (!container.isEmpty()) {
                b = container.get(0).byteValue();
                //b = container.get(container.size() - 1).byteValue();
            }
            isFirst = false;
            return b;
        }
    }

    public void resetFrameState() {
       // Log.w("Reseting","RESETRESETRESETRESETRESET !!!");
        rawFrameState = FrameState.END;
    }

    public enum FrameState {
        Head,
        MSB1,
        LSB1,
        MSB2,
        LSB2,
        MSB3,
        LSB3,
        MSB4,
        LSB4,
        END
    }
}