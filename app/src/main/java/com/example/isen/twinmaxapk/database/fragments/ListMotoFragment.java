package com.example.isen.twinmaxapk.database.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.adapters.MotosAdapter;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MotoChangeListener;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;

import java.util.ArrayList;

import io.realm.RealmResults;

public class ListMotoFragment extends Fragment implements MotoChangeListener, AdapterView.OnItemClickListener {

    private Context context;
    private ListView listView;
    private MotoListener mListener;

    public ListMotoFragment() {
    }

    public ListMotoFragment(Context  context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_moto, container, false);

        listView = (ListView) rootView.findViewById(R.id.motosListView);

        listView.setAdapter(new ArrayAdapter<Moto>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<Moto>()));

        listView.setOnItemClickListener(this);

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
        onMotoRetrieved(motos);
    }

    @Override
    public void onMotoRetrieved(RealmResults<Moto> motos) {
        final MotosAdapter adapter = new MotosAdapter(motos, getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener){
            final Moto moto = (Moto) parent.getItemAtPosition(position);
            mListener.addMaintenanceView(moto);
        }
    }
}
