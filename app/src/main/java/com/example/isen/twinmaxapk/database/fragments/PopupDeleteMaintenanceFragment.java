package com.example.isen.twinmaxapk.database.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceListener;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;

import io.realm.RealmResults;

/**
 * Created by isen on 15/04/2016.
 */
public class PopupDeleteMaintenanceFragment extends DialogFragment {

    private Context context;
    private Maintenance maintenance;
    private Moto moto;
    private Button yesButton;
    private Button noButton;
    private MaintenanceListener mListener;

    public PopupDeleteMaintenanceFragment() {
    }

    public PopupDeleteMaintenanceFragment(Context context, Maintenance maintenance,Moto moto) {
        this.context = context;
        this.maintenance = maintenance;
        this.moto = moto;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof MotoListener){
            mListener = (MaintenanceListener) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setStyle(DialogFragment.STYLE_NO_FRAME, getTheme());

        View rootView = inflater.inflate(R.layout.fragment_popup_moto_delete, container, false);

        yesButton = (Button) rootView.findViewById(R.id.yesButton);
        noButton = (Button) rootView.findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //moto = (Moto) getArguments().getSerializable("moto");

                Compute.getRealm().beginTransaction();
                maintenance.removeFromRealm();
                Compute.getRealm().commitTransaction();

                mListener.dismissPopupMaintenance();
                mListener.onViewMaintenanceForPopup(moto);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.dismissPopupMaintenance();
                mListener.onViewMaintenanceForPopup(moto);
            }
        });



        return rootView;
    }
}
