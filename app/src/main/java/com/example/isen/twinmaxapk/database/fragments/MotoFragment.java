package com.example.isen.twinmaxapk.database.fragments;


import android.app.ActionBar;


import com.example.isen.twinmaxapk.HistoricActivity;
import com.example.isen.twinmaxapk.database.adapters.MotosAdapter;
import com.example.isen.twinmaxapk.database.historic.Moto;

import io.realm.RealmResults;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.isen.twinmaxapk.Compute;
import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.interfaces.MotoChangeListener;
import com.example.isen.twinmaxapk.database.interfaces.MotoListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class MotoFragment extends Fragment implements MotoChangeListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private Button add;
    private Context context;
    private MotoListener mListener;



    public MotoFragment() {
        // Required empty public constructor
    }

    public MotoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_motos, container, false);

        listView = (ListView) rootView.findViewById(R.id.motosListView);
        add = (Button) rootView.findViewById(R.id.add);


        final ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        listView.setEmptyView(progressBar);

        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.motosRootRelativeLayout);
        root.addView(progressBar);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    final FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    final AddMotoFragment fragment = new AddMotoFragment(context);

                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null).commit();
                }
            }
        });

        listView.setAdapter(new ArrayAdapter<Moto>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<Moto>()));

        listView.setOnItemClickListener(this);

        listView.setOnItemLongClickListener(this);

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
        public void onStop() {
            super.onStop();
        }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener){
            final Moto moto = (Moto) parent.getItemAtPosition(position);
            mListener.onViewMaintenance(moto);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                if (null != mListener){
                    final Moto moto = (Moto) adapter.getItemAtPosition(position);
                    mListener.onViewPopupDeleteMoto(moto);
                    /*
                    final Moto moto = (Moto) adapter.getItemAtPosition(position);
                    mListener.onViewDeleteMoto();
                    */
                }
        return true;
    }
}
