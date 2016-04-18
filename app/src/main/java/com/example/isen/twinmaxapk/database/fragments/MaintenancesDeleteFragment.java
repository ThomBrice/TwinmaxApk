package com.example.isen.twinmaxapk.database.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.adapters.MaintenancesAdapter;
import com.example.isen.twinmaxapk.database.adapters.MaintenancesDeleteAdapter;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceChangeListener;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceDeleteChangeListener;

import io.realm.RealmList;

/**
 * Created by isen on 07/04/2016.
 */
public class MaintenancesDeleteFragment extends Fragment implements MaintenanceDeleteChangeListener {

    private Context context;
    private ListView listView;
    private Moto moto;

    public MaintenancesDeleteFragment() {
    }

    public MaintenancesDeleteFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maintenances_delete, container, false);

        listView = (ListView) rootView.findViewById(R.id.maintenancesDeleteListView);

        final ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView.setEmptyView(progressBar);

        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.maintenancesDeleteRootRelativeLayout);
        root.addView(progressBar);

        moto = (Moto) getArguments().getSerializable("moto");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        onMaintenanceDeleteRetrieved(moto.getMaintenances());
    }


    @Override
    public void onMaintenanceDeleteRetrieved(RealmList<Maintenance> maintenances) {
        final MaintenancesDeleteAdapter adapter = new MaintenancesDeleteAdapter(maintenances, context);
        listView.setAdapter(adapter);
    }
}
