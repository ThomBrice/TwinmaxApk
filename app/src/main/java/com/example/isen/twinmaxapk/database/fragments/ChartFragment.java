package com.example.isen.twinmaxapk.database.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
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

        //options data0
        chart.getLineData().getDataSetByIndex(0).setDrawCubic(true);
        chart.getLineData().getDataSetByIndex(0).setValueTextSize(0);
        chart.getLineData().getDataSetByIndex(0).setDrawCircles(false);
        chart.getLineData().getDataSetByIndex(0).setColor(Color.rgb(237, 127, 16));
        //options data1
        chart.getLineData().getDataSetByIndex(1).setDrawCubic(true);
        chart.getLineData().getDataSetByIndex(1).setValueTextSize(0);
        chart.getLineData().getDataSetByIndex(1).setDrawCircles(false);
        chart.getLineData().getDataSetByIndex(1).setColor(Color.rgb(58, 142, 186));
        //options data2
        chart.getLineData().getDataSetByIndex(2).setDrawCubic(true);
        chart.getLineData().getDataSetByIndex(2).setValueTextSize(0);
        chart.getLineData().getDataSetByIndex(2).setDrawCircles(false);
        chart.getLineData().getDataSetByIndex(2).setColor(Color.rgb(127, 221, 76));
        //options data3
        chart.getLineData().getDataSetByIndex(3).setDrawCubic(true);
        chart.getLineData().getDataSetByIndex(3).setValueTextSize(0);
        chart.getLineData().getDataSetByIndex(3).setDrawCircles(false);
        chart.getLineData().getDataSetByIndex(3).setColor(Color.rgb(247, 35, 12));

        return rootView;
    }
}
