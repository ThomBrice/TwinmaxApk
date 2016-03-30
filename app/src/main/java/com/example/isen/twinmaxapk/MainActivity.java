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
import com.example.isen.twinmaxapk.database.historic.Migration;
import com.example.isen.twinmaxapk.manual.Manual;

import io.realm.Realm;
import io.realm.RealmConfiguration;
//tube
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button acquisition_button = (Button) findViewById(R.id.Acquisition_button);
        Button historique_button = (Button) findViewById(R.id.Historique_button);
        Button manual_button = (Button) findViewById(R.id.Mode_emploi_button);
        Button reglage_button = (Button) findViewById(R.id.Reglages_button);

        //instantiate the realm and do migration (compulsory)
        RealmConfiguration config1 = new RealmConfiguration.Builder(this)
                .name("default1")
                .schemaVersion(3)
                .migration(new Migration())
                .build();
        Compute.setRealm(Realm.getInstance(config1));

        acquisition_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BLE_ScanActivity.class);
                startActivity(intent);
            }
        });

        historique_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoricActivity.class);
                startActivity(intent);
            }
        });

        manual_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Manual.class);
                startActivity(intent);
            }
        });
    }
}
