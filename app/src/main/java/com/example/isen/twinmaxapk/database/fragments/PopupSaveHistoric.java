package com.example.isen.twinmaxapk.database.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.isen.twinmaxapk.Acquisition;
import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.Constants;
import com.example.isen.twinmaxapk.HistoricActivity;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.MaintenanceWithList;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceListener;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;
import com.example.isen.twinmaxapk.database.interfaces.SaveMotoListener;

/**
 * Created by isen on 25/04/2016.
 */
public class PopupSaveHistoric extends DialogFragment {

    private Context context;
    private Moto moto;
    private MaintenanceWithList maintenance;
    private Button addNewButton;
    private Button addExistingButton;

    public PopupSaveHistoric() {
    }

    public PopupSaveHistoric(Context context, MaintenanceWithList maintenance) {
        this.context = getActivity();
        this.maintenance = maintenance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NO_FRAME, getTheme());

        View rootView = inflater.inflate(R.layout.fragment_popup_save_historic, container, false);

        addNewButton = (Button) rootView.findViewById(R.id.addNewMoto);
        addExistingButton = (Button) rootView.findViewById(R.id.addToAMoto);

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Acquisition.hasSavedSomething = true;
                Intent intent = new Intent(getActivity(), HistoricActivity.class);
                final Bundle extras = new Bundle();
                extras.putString(Constants.GOTOHISTORIC, "1");
                extras.putSerializable("maintenance", maintenance);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        addExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }
}
