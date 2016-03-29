package com.example.isen.twinmaxapk;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.isen.twinmaxapk.database.fragments.MotoFragment;

public class HistoricActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        Compute compute = new Compute();
        compute.emptyDatabase();
        compute.sommeItemsInDatabase();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MotoFragment motoFragment = new MotoFragment(this);
        transaction.add(R.id.container, motoFragment);
        transaction.commit();
    }


}
