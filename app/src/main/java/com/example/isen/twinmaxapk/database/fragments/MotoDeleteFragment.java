package com.example.isen.twinmaxapk.database.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.adapters.MotosDeleteAdapter;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MotoDeleteChangeListener;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;

import java.util.ArrayList;

import io.realm.RealmResults;


public class MotoDeleteFragment extends Fragment implements MotoDeleteChangeListener {

    private ListView listView;
    private Button delete;
    private Context context;
    private MotosDeleteAdapter adapter;
    private MotoListener mListener;

    public MotoDeleteFragment() {
        // Required empty public constructor
    }

    public MotoDeleteFragment(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_motos_delete, container, false);

        listView = (ListView) rootView.findViewById(R.id.motosDeleteListView);
        delete = (Button) rootView.findViewById(R.id.deleteButton);

        listView.setAdapter(new ArrayAdapter<Moto>(getActivity(), android.R.layout.simple_list_item_2, new ArrayList<Moto>()));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = adapter.getCount();
                for (int i=0; i<size;i++){
                    adapter.deleteItemSelected(i);
                }
                mListener.onViewMoto();
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

    @Override
    public void onStart() {
        super.onStart();
        RealmResults<Moto> motos = Compute.getRealm().where(Moto.class).findAll();
        onMotoDeleteRetrieved(motos);
    }

    @Override
    public void onMotoDeleteRetrieved(RealmResults<Moto> motos) {
        adapter = new MotosDeleteAdapter(motos, context);
        listView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
