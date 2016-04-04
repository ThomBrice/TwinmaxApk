package com.example.isen.twinmaxapk.database.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.adapters.MaintenancesAdapter;
import com.example.isen.twinmaxapk.database.adapters.MotosAdapter;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceChangeListener;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by isen on 20/03/2016.
 */
public class MaintenancesFragment extends Fragment implements MaintenanceChangeListener {

    private ListView listView;
    private Context context;
    private Moto moto;

    public MaintenancesFragment() {
    }

    public MaintenancesFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maintenances, container, false);

        listView = (ListView) rootView.findViewById(R.id.motosListView);

        final ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView.setEmptyView(progressBar);

        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.maintenancesRootRelativeLayout);
        root.addView(progressBar);

        moto = (Moto) getArguments().getSerializable("moto");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        onMotoRetrieved(moto.getMaintenances());
    }

    @Override
    public void onMotoRetrieved(RealmList<Maintenance> maintenances) {
        final MaintenancesAdapter adapter = new MaintenancesAdapter(maintenances, context);
        listView.setAdapter(adapter);
    }
}
