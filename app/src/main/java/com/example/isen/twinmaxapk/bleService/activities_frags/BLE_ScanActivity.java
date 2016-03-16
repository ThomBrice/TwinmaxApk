package com.example.isen.twinmaxapk.bleService.activities_frags;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isen.twinmaxapk.R;

import java.util.ArrayList;

/**
 * Created by Matthieu on 16/03/2016.
 */
public class BLE_ScanActivity extends ListActivity{
    private BLE_ScanAdapter mListDeviceAdapter;
    private BluetoothAdapter mBAdapter;
    private boolean scanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        //Check wether or not the device support BLE
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(this, R.string.toast_ble_not_supported, Toast.LENGTH_LONG).show();
            finish();
        }


        //Initialize a blutooth adapter
        final BluetoothManager bleManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBAdapter = bleManager.getAdapter();

        //Is bluetooth supported
        if(mBAdapter == null) {
            Toast.makeText(this, R.string.toast_bl_not_supported, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if(!mBAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        mListDeviceAdapter = new BLE_ScanAdapter();
        setListAdapter(mListDeviceAdapter);
        scanLeDevice(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_menu,menu);
        if(!scanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mListDeviceAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, R.string.ble_refused_bluetooth, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mListDeviceAdapter.clear();
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //TODO implement method : create intent with bundle info(device) 
        super.onListItemClick(l, v, position, id);
    }

    private void scanLeDevice(final boolean enable) {
        if(enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    mBAdapter.startLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            scanning = true;
            mBAdapter.startLeScan(mLeScanCallback);
        } else {
            scanning = false;
            mBAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }



    private class BLE_ScanAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflater;

        public BLE_ScanAdapter() {
            super();
            mLeDevices = new ArrayList<>();
            mInflater = BLE_ScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return mLeDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.ble_scan_adapter_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAdress = (TextView) convertView.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            BluetoothDevice device = mLeDevices.get(position);
            final String deviceName = device.getName();
            final String deviceAddress = device.getAddress();

            if(deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
            } else {
                viewHolder.deviceName.setText(R.string.ble_scan_adapter_unknown_device);
            }

            if(deviceAddress != null && deviceName.length() > 0) {
                viewHolder.deviceAdress.setText(deviceAddress);
            } else {
                viewHolder.deviceAdress.setText(R.string.ble_scan_adapter_unknown_address);
            }


            return convertView;
        }

        private class ViewHolder {
            TextView deviceName;
            TextView deviceAdress;
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListDeviceAdapter.addDevice(device);
                    mListDeviceAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
