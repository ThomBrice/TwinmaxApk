package com.example.isen.twinmaxapk.database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isen.twinmaxapk.R;
import com.example.isen.twinmaxapk.database.RealmMeasure;
import com.example.isen.twinmaxapk.database.fragments.ChartFragment;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.example.isen.twinmaxapk.database.interfaces.MaintenanceListener;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class MaintenancesAdapter extends BaseAdapter {

    private final List<Maintenance> maintenances;
    private final LayoutInflater inflater;
    private Context context;
    private Button courbesButton;
    private MaintenanceListener mListener;

    public MaintenancesAdapter(List<Maintenance> maintenances, Context context, MaintenanceListener listener) {
        this.maintenances = maintenances;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return null != maintenances ? maintenances.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null != maintenances ? maintenances.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = inflater.inflate(R.layout.maintenance_listitem, null);

        final Maintenance maintenance = (Maintenance) getItem(position);

        final TextView name = (TextView) view.findViewById(R.id.details);
        name.setText(maintenance.getNote());

        final TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(maintenance.getDate());

        courbesButton = (Button) view.findViewById(R.id.courbesbutton);

        courbesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Maintenance m = (Maintenance) getItem(position);
                LineData lineData = fillLineData(m.getMeasures());
                if (null != mListener){
                    mListener.onViewGraph(lineData);
                }
            }
        });

        return view;
    }

    public LineData fillLineData(RealmList<RealmMeasure> measures){

        ArrayList<Entry> data0 = new ArrayList<>();
        ArrayList<String> labelsInit = new ArrayList<>();

        for (int i=0; i<measures.size();i++){
            data0.add(new Entry(measures.get(i).getC0(),i));
            labelsInit.add("");
        }

        ArrayList<LineDataSet> lines = new ArrayList<>();
        lines.add(new LineDataSet(data0,"data0"));

        return new LineData(labelsInit, lines);
    }
}
