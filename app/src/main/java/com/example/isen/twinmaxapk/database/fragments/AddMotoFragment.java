package com.example.isen.twinmaxapk.database.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;

import io.realm.RealmList;

/**
 * Created by isen on 03/04/2016.
 */
public class AddMotoFragment extends Fragment {

    private Context context;
    private EditText nameMoto;
    private EditText date;
    private EditText note;
    private Button addButton;
    private MotoListener mListener;


    public AddMotoFragment() {
        // Required empty public constructor
    }

    public AddMotoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_moto, container, false);

        nameMoto = (EditText) rootView.findViewById(R.id.nameMoto);
        date = (EditText) rootView.findViewById(R.id.date);
        note = (EditText) rootView.findViewById(R.id.note);
        addButton = (Button) rootView.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Maintenance maintenance = new Maintenance(date.getText().toString(),note.getText().toString());

                RealmList<Maintenance> maintenances = new RealmList<Maintenance>();
                maintenances.add(maintenance);
                Moto moto = new Moto(nameMoto.getText().toString(),date.getText().toString());
                moto.setMaintenances(maintenances);
                if (mListener !=null) {
                    mListener.addMoto(moto);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof MotoListener){
            mListener = (MotoListener) activity;
        }
    }
}
