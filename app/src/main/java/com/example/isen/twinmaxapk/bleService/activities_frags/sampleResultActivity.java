

package com.example.isen.twinmaxapk.bleService.activities_frags;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.bleService.BLEService;

/**
 * Created by Matthieu on 17/03/2016.
 */
public class sampleResultActivity extends Activity{

    public final String TAG = "sampleResultActivity";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceName;
    private String mDeviceAddress;
    private TextView mTextview;

    private BLEService mBLEService;


    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //TODO implement
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private final BroadcastReceiver mBrReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO implement
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_result_activity_layout);

        final Intent intent = getIntent();

        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        mTextview = (TextView) findViewById(R.id.sample_result_textview);

        Intent gattServiceIntent = new Intent(this, BLEService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mBrReceiver, makeGattUpdateIntentFilter());
        if(mBLEService != null) {
            final boolean result = mBLEService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result : "+ result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBrReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBLEService = null;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BLEService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
