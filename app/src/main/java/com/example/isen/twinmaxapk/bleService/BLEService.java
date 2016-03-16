package com.example.isen.twinmaxapk.bleService;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by Matthieu on 16/03/2016.
 */
public class BLEService extends Service {
    private final String TAG = "BLEService";
    private BluetoothManager mBManager;
    private BluetoothAdapter mBAdapter;
    private BluetoothGatt mBGatt;
    private String mBLEDeviceAdress;
    private int mConnectionState = STATE_DISCONNECTED;


    private final static int STATE_DISCONNECTED = 0;
    private final static int STATE_CONNECTING = 1;
    private final static int STATE_CONNECTED = 2;

    private final BluetoothGattCallback mBCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    }



    public class LocalBinder extends Binder {
        BLEService getService() {
            return BLEService.this;
        }
    }


    private final IBinder mBinder = new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if(mBManager == null) {
            mBManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if(mBManager == null) {
                Log.e(TAG, "Unable to obtain BLEmanager");
                return false;
            }
        }
        mBAdapter = mBManager.getAdapter();
        if(mBAdapter == null) {
            Log.e(TAG, "Unable to obtain a Bluetooth Adapter");
            return false;
        }
        return true;
    }

    public boolean connect(final String adress) {
        if(mBAdapter == null || adress == null) {
            Log.w(TAG, "BLEAdapter not initialized or no adress specified");
            return false;
        }

        if(mBLEDeviceAdress != null && adress.equals(mBLEDeviceAdress) && mBGatt != null) {
            Log.d(TAG, "Trying to use an existing BLE for connection");
            if(mBGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBAdapter.getRemoteDevice(adress);
        if(device == null) {
            Log.w(TAG, "Device not found, unable to connect");
            return false;
        }

        mBGatt = device.connectGatt(this, false, mBCallback);
        Log.d(TAG, "Trying to create a new connection");
        mBLEDeviceAdress = adress;
        mConnectionState = STATE_CONNECTING;
        return true;
    }


    public void close() {
        if(mBGatt == null) {
            return;
        }
        mBGatt.close();
        mBGatt = null;
    }
}
