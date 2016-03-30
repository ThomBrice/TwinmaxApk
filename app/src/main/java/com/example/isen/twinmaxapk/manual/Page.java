package com.example.isen.twinmaxapk.manual;

public class Page {
    protected String categorie;
    protected String picture;
    protected String text;

    public Page (String categorie, String picture, String text){
        this.text = text;
        this.picture = picture;
        this.categorie=categorie;
    }

    public String getText(){
        return text;
    }

    public String getPicture() {return picture;}

    public String getCategorie() {return categorie;}
}
