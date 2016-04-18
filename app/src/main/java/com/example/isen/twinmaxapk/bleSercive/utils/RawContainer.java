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

    public boolean isRawContainerEmpty() {
        return container.isEmpty();
    }

    public RawContainer(ObservableList.OnListChangedCallback changeCallback) {
        container = new ObservableArrayList();
        container.addOnListChangedCallback(changeCallback);
        rawFrameState = FrameState.END;
    }

    public synchronized void addFrame(byte[] data, int size){
        if(container.size() >= 2000) {
            container.clear();
        }
        if(container != null && data != null) {
            for(byte b: data) {
                if(b == 0) {
                    return;
                }
                container.add(new Byte(b));
                //container.add(0, new Byte(b));
            }
           // Log.w("Size", "value : " + container.size());
        }
    }
    public static boolean isFirst = true;
    public int getSize() {
        return container.size();
    }
    public synchronized byte getFirst() {
        byte b = 0;
        if(!isFirst) {
            container.remove(0);
           // container.remove(container.size()-1);

        }
        if(!container.isEmpty()) {
            b = container.get(0).byteValue();
         //   b = container.get(container.size()-1).byteValue();
        }
        isFirst = false;
        return b;
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
