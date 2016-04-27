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
        if(myPage.getPicture().equals("TwinmaxPhoto")){this.imageView.setImageResource(R.drawable.twinmax_photo);}
        if(myPage.getPicture().equals("accueil1")){this.imageView.setImageResource(R.drawable.accueil1);}
        if(myPage.getPicture().equals("choix_bluetooth")){this.imageView.setImageResource(R.drawable.choix_bluetooth);}
        if(myPage.getPicture().equals("graphiques")){this.imageView.setImageResource(R.drawable.graphiques);}
        if(myPage.getPicture().equals("choix_bluetooth")){this.imageView.setImageResource(R.drawable.choix_bluetooth);}
        if(myPage.getPicture().equals("accueil2")){this.imageView.setImageResource(R.drawable.accueil2);}
        if(myPage.getPicture().equals("ajout_commentaire")){this.imageView.setImageResource(R.drawable.ajout_commentaire);}
        if(myPage.getPicture().equals("ajout_moto")){this.imageView.setImageResource(R.drawable.ajout_moto);}
        if(myPage.getPicture().equals("historique")){this.imageView.setImageResource(R.drawable.historique);}
    }
}