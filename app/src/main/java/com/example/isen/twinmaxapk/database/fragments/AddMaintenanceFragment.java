package com.example.isen.twinmaxapk.database.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.HistoricActivity;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.MaintenanceWithList;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceListener;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;

import io.realm.RealmList;

public class AddMaintenanceFragment extends Fragment {

    private EditText date;
    private EditText note;
    private Button add;
    private Context context;
    private Moto moto;
    private MaintenanceWithList maintenance;
    private MaintenanceListener mListener;

    public AddMaintenanceFragment() {
    }

    public AddMaintenanceFragment(Context context, Moto moto, MaintenanceWithList maintenance) {
        this.context= context;
        this.moto = moto;
        this.maintenance = maintenance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_maintenance, container, false);

        date = (EditText) rootView.findViewById(R.id.dateText);
        note = (EditText) rootView.findViewById(R.id.noteText);
        add = (Button) rootView.findViewById(R.id.addButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(maintenance!=null){
                    Maintenance maintenance1 = new Maintenance(date.getText().toString(), note.getText().toString(), maintenance.getMeasures());
                    if (mListener !=null) {
                        mListener.addMaintenance(moto, maintenance1);
                    }
                }
                else {
                    Maintenance maintenance1 = new Maintenance(date.getText().toString(), note.getText().toString());

                    if (mListener !=null) {
                        mListener.addMaintenance(moto, maintenance1);
                    }
                }

                if(HistoricActivity.isFromAcqu) {
                    mListener.quitHistoricActivityFromMaintenance();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof MotoListener){
            mListener = (MaintenanceListener) activity;
        }
    }
}
