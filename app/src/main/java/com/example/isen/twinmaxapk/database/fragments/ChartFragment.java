package com.example.isen.twinmaxapk.database.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isen.twinmaxapk.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

public class ChartFragment extends Fragment {

    private LineChart chart;
    private Context context;
    private LineData lineData;

    public ChartFragment() {
    }

    public ChartFragment(Context context, LineData lineData) {
        this.context = context;
        this.lineData = lineData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        chart = new LineChart(context);

        chart = (LineChart) rootView.findViewById(R.id.graph);

        chart.setData(lineData);

        return rootView;
    }
}
