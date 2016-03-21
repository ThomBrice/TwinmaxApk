package com.example.isen.twinmaxapk;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import com.example.isen.twinmaxapk.database.historic.Migration;

import com.example.isen.twinmaxapk.bleService.activities_frags.BLE_ScanActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b0 = (Button) findViewById(R.id.b0);
        Button b2 = (Button) findViewById(R.id.b2);

        //instantiate the realm and do migration (compulsory)
        Compute compute = new Compute();
        /*RealmConfiguration config1 = new RealmConfiguration.Builder(this)
                .name("default1")
                .schemaVersion(3)
                .migration(new Migration())
                .build();
        compute.setRealm(Realm.getInstance(config1));*/

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BLE_ScanActivity.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoricActivity.class);
                startActivity(intent);
            }
        });

    }
}
