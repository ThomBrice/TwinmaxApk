package com.example.isen.twinmaxapk.manual;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isen.twinmaxapk.R;

public class PageViewHolder extends RecyclerView.ViewHolder{
    private TextView textView;
    private ImageView imageView;

    //itemView est la vue correspondante à 1 cellule
    public PageViewHolder(View itemView){
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.text);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    //puis ajouter une fonction pour remplir la cellule en fonction de mon objet Page
    public void setPage(Page myPage){
        textView.setText(myPage.getText());
        if(myPage.getPicture().equals("twinmaxphoto")){this.imageView.setImageResource(R.drawable.twinmaxphoto);}
        if(myPage.getPicture().equals("applicationphoto")){this.imageView.setImageResource(R.drawable.applicationphoto);}
    }
}