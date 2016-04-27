package com.example.isen.twinmaxapk;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.isen.twinmaxapk.database.fragments.AddMotoFragment;
import com.example.isen.twinmaxapk.database.fragments.ChartFragment;
import com.example.isen.twinmaxapk.database.fragments.MaintenancesFragment;
import com.example.isen.twinmaxapk.database.fragments.MotoFragment;
import com.example.isen.twinmaxapk.database.fragments.PopupDeleteMaintenanceFragment;
import com.example.isen.twinmaxapk.database.fragments.PopupDeleteMotoFragment;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.MaintenanceWithList;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceListener;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;
import com.github.mikephil.charting.data.LineData;

public class HistoricActivity extends Activity implements MotoListener, MaintenanceListener {


    PopupDeleteMotoFragment popupMoto;
    PopupDeleteMaintenanceFragment popupMaintenance;
    public static boolean isFromAcqu;

    @Override
    public void quitHistoricActivity() {
        isFromAcqu = false;
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        isFromAcqu = false;
        String fromWhere = getIntent().getExtras().get(Constants.GOTOHISTORIC).toString();
        MaintenanceWithList maintenance = (MaintenanceWithList) getIntent().getExtras().getSerializable("maintenance");

        int from = Integer.parseInt(fromWhere);
        Compute compute = new Compute();

        compute.emptyDatabase();
        compute.someItemsInDatabase();

        if (from== 1) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            AddMotoFragment fragment = new AddMotoFragment(getApplicationContext(),maintenance);
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null).commit();
            isFromAcqu = true;
        }
        else{
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            MotoFragment motoFragment = new MotoFragment(this);
            transaction.add(R.id.container, motoFragment);
            transaction.commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onViewMoto() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MotoFragment motoFragment = new MotoFragment(this);
        transaction.replace(R.id.container, motoFragment);
        transaction.commit();
    }

    @Override
    public void dismissPopupMoto() {
        popupMoto.dismiss();
    }

    @Override
    public void onViewMaintenance(Moto moto) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        final MaintenancesFragment fragment = new MaintenancesFragment(this, moto);
        final Bundle bundle = new Bundle();

        //bundle.putSerializable("moto",moto);
        //fragment.setArguments(bundle);

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();

    }

    @Override
    public void onViewPopupDeleteMoto(Moto moto) {

        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        popupMoto = new PopupDeleteMotoFragment(this, moto);

        popupMoto.show(transaction,"popupMoto");

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

    @Override
    public void onViewMaintenanceForPopup(Moto moto) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        final MaintenancesFragment fragment = new MaintenancesFragment(this, moto);
        final Bundle bundle = new Bundle();

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();
    }

    @Override
    public void dismissPopupMaintenance() {
        popupMaintenance.dismiss();
    }

    @Override
    public void onViewPopupDeleteMaintenance(Maintenance maintenance, Moto moto) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        popupMaintenance = new PopupDeleteMaintenanceFragment(this, maintenance, moto);

        popupMaintenance.show(transaction,"test");
    }
}
