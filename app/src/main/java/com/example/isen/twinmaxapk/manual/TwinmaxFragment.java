package com.example.isen.twinmaxapk.manual;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isen.twinmaxapk.R;

import java.util.ArrayList;
import java.util.List;

public class TwinmaxFragment extends Fragment {
    private String title;
    private int page;

    private List<Page> pages = new ArrayList<>();

    protected RecyclerView myRecyclerView;
    private PageAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public static TwinmaxFragment newInstance(int page, String title) {
        TwinmaxFragment fragment = new TwinmaxFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        page=getArguments().getInt("someInt", 0);
        title=getArguments().getString("someTitle");
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_manual, container,false);
        myRecyclerView = (RecyclerView) rootView.findViewById(R.id.manualRecycler);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity());


        myRecyclerView = (RecyclerView) view.findViewById(R.id.manualRecycler);
        //myRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PageAdapter(pages);
        myRecyclerView.setAdapter(mAdapter);
    }

    private void initDataset(){
        pages.add(new Page("kok","twinmaxphoto","twinmax"));
        pages.add(new Page("kok","twinmaxphoto","twinmax"));
        pages.add(new Page("kok","twinmaxphoto","twinmax"));
        pages.add(new Page("kok","twinmaxphoto","twinmax"));
    }
}

