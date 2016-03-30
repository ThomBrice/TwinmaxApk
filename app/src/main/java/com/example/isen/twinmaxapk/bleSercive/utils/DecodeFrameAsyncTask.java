package com.example.isen.twinmaxapk.bleSercive.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.database.Measure;

/**
 * Created by Matthieu on 21/03/2016.
 */
public class DecodeFrameAsyncTask extends AsyncTask<RawContainer, Integer, Integer> {
    private RawContainer container;
    private DecoderListener mListener;
    private static int[] values = {0,0,0,0};
    private static int[] prevValues = {0,0,0,0};
    public DecodeFrameAsyncTask(DecoderListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected Integer doInBackground(RawContainer... params) {
        if(params[0] != null) {
            container = params[0];
            decode();
        }
        return null;
    }

    private boolean isCorrectFrame(byte b) {
        boolean isGood = true;
        if(b % 2 != 0) {
            isGood = false;
        }
        if((int) (b & 0xFF) >= 128) {
            isGood = false;
        }
        return isGood;//((int)b % 2 == 0) && ((int)(b&0XFF) <128);
    }

    private boolean isHeader(byte b) {
        return ((int) (b & 0XFF) == 128);
    }
    private boolean isFirst = true;
    private void addNewMeasure() {
       // Log.w("Background Decoder", "Capteur 1: " + values[0] + "Capteur 2: " + values[1] + "Capteur 3: " + values[2] + "Capteur 4: " + values[3] );
        //Compute.addMeasure(new Measure(values[0], values[1], values[2], values[3]));
        if(isFirst) {
            for(int i=0;i<4;i++) {
                prevValues[i] = values[i];
            }
            isFirst = false;
        }
        if(mListener != null) {
            for(int i=0;i<4;i++) {
                if(Math.abs(values[i] - prevValues[i]) > 50) {
                    values[i] = (prevValues[i] + values[i])/2;
                    //values[i] = 3500;
                }
                if(values[i] <= 0) {
                    values[i] = prevValues[i];
                    //values[i] = 3500;
                }
            }
            mListener.addCleanData(new Measure(values[0], values[1], values[2], values[3]));
            for(int i=0;i<4;i++) {
                prevValues[i] = values[i];
            }
        }
    }

    private void decode() {
        //Log.w("Async", "Starting to decode the frame");
        while(!container.isRawContainerEmpty()) {
            //    Log.w("Decoder", "Current Frame State" + container.rawFrameState );
            //TODO implement ?
            Byte actual = new Byte(container.getFirst());
            //byte currentByte = container.getFirst();
            byte currentByte = actual.byteValue();
            Log.w("Taille rawDataBuffer", "Valeur : " + (int)(currentByte & 0xFF));
            switch (container.rawFrameState) {
                case Head:
                    break;
                case MSB1:
                    if(isCorrectFrame(currentByte)) {
                        //TODO do correct shifting and add it to int table;
                        values[0] = (currentByte << 5);
                        container.rawFrameState = RawContainer.FrameState.LSB1;
                    } else {
                        container.resetFrameState();
                    }
                    break;
                case LSB1:
                    if(isCorrectFrame(currentByte)) {
                        //TODO do correct shifting and add it to int table;
                        values[0] += (currentByte >> 1);
                        container.rawFrameState = RawContainer.FrameState.MSB2;
                    } else {
                        container.resetFrameState();
                    }
                    break;
                case MSB2:
                    if(isCorrectFrame(currentByte)) {
                        //TODO do correct shifting and add it to int table;
                        values[1] = (currentByte << 5);
                        container.rawFrameState = RawContainer.FrameState.LSB2;
                    } else {
                        container.resetFrameState();
                    }
                    break;
                case LSB2:
                    if(isCorrectFrame(currentByte)) {
                        //TODO do correct shifting and add it to int table;
                        values[1] += (currentByte >> 1);
                        container.rawFrameState = RawContainer.FrameState.MSB3;
                    } else {
                        container.resetFrameState();
                    }
                    break;
                case MSB3:
                    if(isCorrectFrame(currentByte)) {
                        //TODO do correct shifting and add it to int table;
                        values[2] = (currentByte << 5);
                        container.rawFrameState = RawContainer.FrameState.LSB3;
                    } else {
                        container.resetFrameState();
                    }
                    break;
                case LSB3:
                    if(isCorrectFrame(currentByte)) {
                        //TODO do correct shifting and add it to int table;
                        values[2] += (currentByte >> 1);
                        container.rawFrameState = RawContainer.FrameState.MSB4;
                    } else {
                        container.resetFrameState();
                    }
                    break;
                case MSB4:
                    if(isCorrectFrame(currentByte)) {
                        //TODO do correct shifting and add it to int table;
                        values[3] = (currentByte << 5);
                        container.rawFrameState = RawContainer.FrameState.LSB4;
                    } else {
                        container.resetFrameState();
                    }
                    break;
                case LSB4:
                    if(isCorrectFrame(currentByte)) {
                        //TODO do correct shifting and add it to int table;
                        values[3] += (currentByte >> 1);
                        addNewMeasure();
                        container.resetFrameState();
                    } else {
                        container.resetFrameState();
                    }
                    break;
                case END:
                    if(isHeader(currentByte)) {
                        container.rawFrameState = RawContainer.FrameState.MSB1;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
