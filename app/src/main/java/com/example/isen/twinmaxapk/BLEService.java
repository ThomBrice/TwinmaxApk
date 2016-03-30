package com.example.isen.twinmaxapk;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;


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

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";



    private final BluetoothGattCallback mBCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if(newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server");
                Log.i(TAG, "Attempting to start discovery service: " + mBGatt.discoverServices());
            } else if(newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnect from GATT service");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "on serviceDiscovered receive : " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE,characteristic);
        }
    };

    /**
     * Send broadcast of action
     * @param action action the intent is turned to
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        //TODO gestion des données reçues
        /* Proposition 1:
        1. Check UUID (not mandatory in our case)
        2. Récupérer les données
        3. parser les données
        4. stocker les données/envoyer broadcast to activité concerné avec donnée
         */
        /* Proposition 2:
        1. Envoyé broadcast avec données à l'activité concerné (qui s'occupera de faire le traitement (i.e. parsing des trames)
         */
        final Intent intent = new Intent(action);
        final byte[] data = characteristic.getValue();
        /*if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for(byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));
            intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
                    stringBuilder.toString());
        }*/
        for(byte b: data) {
            Log.w("VeryRaw data", "Valeur : " + (int)(b & 0xFF));
        }

        intent.putExtra(EXTRA_DATA, data);
        sendBroadcast(intent);
    }


    public class LocalBinder extends Binder {
        BLEService getService() {
            return BLEService.this;
        }
    }


    private final IBinder mBinder = new LocalBinder();

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


    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if(mBAdapter == null || address == null) {
            Log.w(TAG, "BLEAdapter not initialized or no adress specified");
            return false;
        }

        if(mBLEDeviceAdress != null && address.equals(mBLEDeviceAdress) && mBGatt != null) {
            Log.d(TAG, "Trying to use an existing BLE for connection");
            if(mBGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBAdapter.getRemoteDevice(address);
        if(device == null) {
            Log.w(TAG, "Device not found, unable to connect");
            return false;
        }

        mBGatt = device.connectGatt(this, false, mBCallback);
        Log.d(TAG, "Trying to create a new connection");
        mBLEDeviceAdress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if(mBAdapter == null || mBGatt == null) {
            Log.w(TAG, "BleAdapadter not initialized");
            return;
        }
        mBGatt.disconnect();
    }


    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if(mBGatt == null) {
            return;
        }
        mBGatt.close();
        mBGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacterestic(BluetoothGattCharacteristic characteristic) {
        if(mBGatt == null || mBAdapter == null) {
            Log.w(TAG, "BlAdapter not initialized");
            return;
        }
        mBGatt.readCharacteristic(characteristic);
    }


    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if(mBGatt == null || mBAdapter == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBGatt.setCharacteristicNotification(characteristic, enabled);

    }


    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if(mBGatt == null) {
            return null;
        }

        return mBGatt.getServices();
    }
}
