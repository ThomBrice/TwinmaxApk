package com.example.isen.twinmaxapk;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.example.isen.twinmaxapk.database.fragments.AddMotoFragment;
import com.example.isen.twinmaxapk.database.fragments.ChartFragment;
import com.example.isen.twinmaxapk.database.fragments.MaintenancesFragment;
import com.example.isen.twinmaxapk.database.fragments.MotoDeleteFragment;
import com.example.isen.twinmaxapk.database.fragments.MotoFragment;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceListener;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;
import com.github.mikephil.charting.data.LineData;

public class HistoricActivity extends Activity implements MotoListener, MaintenanceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        Compute compute = new Compute();

        compute.emptyDatabase();
        compute.someItemsInDatabase();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MotoFragment motoFragment = new MotoFragment(this);
        transaction.add(R.id.container, motoFragment);
        transaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onViewMaintenance(Moto moto) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        final MaintenancesFragment fragment = new MaintenancesFragment(this);
        final Bundle bundle = new Bundle();

        //mettre les param de moto dans maintenances

        bundle.putSerializable("moto",moto);
        fragment.setArguments(bundle);

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();

    }


    @Override
    public void onViewDelete() {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        final MotoDeleteFragment fragment = new MotoDeleteFragment(this);

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();
    }

    @Override
    public void addMoto(Moto moto) {

        Compute.addMoto(moto);
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        final MotoFragment fragment = new MotoFragment(this);

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();
    }

    @Override
    public void onViewGraph(LineData lineData) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        final ChartFragment fragment = new ChartFragment(this, lineData);

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();
    }
}
