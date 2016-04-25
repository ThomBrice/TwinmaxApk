package com.example.isen.twinmaxapk.bleSercive.utils;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.channels.Pipe;

/**
 * Created by Matthieu on 21/03/2016.
 */
public class RawContainer {
    private ObservableArrayList<Byte> container;
    public FrameState rawFrameState;


    private PipedInputStream mInputStream;
    private PipedOutputStream mOutputStream;
    private DataOutputStream mDatOutputStream;
    private DataInputStream mDatainputStream;
    public boolean isRawContainerEmpty() {
        return false;
        //return container.isEmpty();
    }

    public RawContainer(ObservableList.OnListChangedCallback changeCallback) {
        container = new ObservableArrayList();
        container.addOnListChangedCallback(changeCallback);
        rawFrameState = FrameState.END;
        try {
            mInputStream = new PipedInputStream();
            mOutputStream = new PipedOutputStream(mInputStream);
            mDatOutputStream = new DataOutputStream(mOutputStream);
            mDatainputStream = new DataInputStream(mInputStream);
        } catch (IOException e) {
            Log.e("PipecreationRaw",e.getMessage());
        }
    }



    private int total = 0;
    public void addFrame(byte[] data, int size){
        try {
            mDatOutputStream.write(data,0,size);
            mDatOutputStream.flush();
            total += size;
            //Log.e("adding data","ADDING DATA : " + size);
        } catch (IOException e) {
            Log.e("RAW DATA","Couldn't write the data");
        }
        if(total >= 100) {
            byte b=0;
            container.clear();
            container.add(new Byte(b));
        }
        /*if(container.size() >= 10000) {
           // skipFrame(500);
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
        }*/

    }
    public static boolean isFirst = true;
    public int getSize() {
        return container.size();
    }


    public byte getFirst() throws IOException{
        /*byte b = 0;
        if(!isFirst) {
            container.remove(0);
           // container.remove(container.size()-1);

        }
        if(!container.isEmpty()) {
            b = container.get(0).byteValue();
         //   b = container.get(container.size()-1).byteValue();
        }
        isFirst = false;
        return b;*/
        byte b=0;

        b = mDatainputStream.readByte();

        total--;
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
