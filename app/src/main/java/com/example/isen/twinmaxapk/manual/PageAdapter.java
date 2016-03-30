package com.example.isen.twinmaxapk.manual;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isen.twinmaxapk.R;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<PageViewHolder>{

    List<Page> pages;

    public PageAdapter(List<Page> pagesSet){
        pages = pagesSet;
    }

    @Override
    public PageViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.manual_card,viewGroup,false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PageViewHolder myHolder, int position){
        Page page = pages.get(position);
        myHolder.setPage(page);
    }

    @Override
    public int getItemCount(){
        return pages.size();
    }

}