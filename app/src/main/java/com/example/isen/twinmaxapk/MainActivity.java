
package com.example.isen.twinmaxapk;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.isen.twinmaxapk.bleService.activities_frags.BTScanActivity;
import com.example.isen.twinmaxapk.database.historic.Migration;
import com.example.isen.twinmaxapk.manual.Manual;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends Activity {
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView acquisitionCard = (CardView) findViewById(R.id.Acquisition_card);
        CardView historiqueCard = (CardView) findViewById(R.id.Historique_card);
        CardView manualCard = (CardView) findViewById(R.id.Mode_emploi_card);
        CardView reglageCard = (CardView) findViewById(R.id.Reglage_card);

        //instantiate the realm and do migration (compulsory)
        RealmConfiguration config1 = new RealmConfiguration.Builder(this)
                .name("default1")
                .schemaVersion(3)
                .migration(new Migration())
                .build();
        Compute.setRealm(Realm.getInstance(config1));

        acquisitionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    Intent intent = new Intent(getApplicationContext(), BTScanActivity.class);
                    startActivity(intent);
                }
            }
        });

        historiqueCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoricActivity.class);
                final Bundle extras = new Bundle();
                extras.putInt( Constants.GOTOHISTORIC, 1);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        manualCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Manual.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1) {
            Intent intent = new Intent(getApplicationContext(), BTScanActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),"La Bluetooth n'a pas été allumé. Veuillez réessayer.", Toast.LENGTH_LONG).show();
        }
    }
}
