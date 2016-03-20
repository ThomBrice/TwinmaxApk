package com.example.isen.twinmaxapk.database.fragments;


import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.adapters.MotosAdapter;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MotoChangeListener;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MotoFragment extends Fragment implements MotoChangeListener {


    Compute compute;
    ListView listView;
    Context context;

    public MotoFragment() {
        // Required empty public constructor
    }

    public MotoFragment(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        compute = new Compute();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_motos, container, false);

        listView = (ListView) rootView.findViewById(R.id.motosListView);

        final ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView.setEmptyView(progressBar);

        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.motosRootRelativeLayout);
        root.addView(progressBar);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        RealmResults<Moto> motos = compute.getRealm().where(Moto.class).findAll();
        onMotoRetrieved(motos);
    }

    @Override
    public void onMotoRetrieved(RealmResults<Moto> motos) {
        final MotosAdapter adapter = new MotosAdapter(motos, context);
        listView.setAdapter(adapter);
    }

}
