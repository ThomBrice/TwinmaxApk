package com.example.isen.twinmaxapk.manual;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadFileHelper {

    public static String readFile(Context context){

        String tempo;
        String contenuDuFichier="";

        InputStream input = null;
        try {

            input = context.getResources().getAssets().open("ManualText.txt");
            BufferedReader bfr = new BufferedReader(new InputStreamReader(input));
            while ((tempo=bfr.readLine())!=null){
                contenuDuFichier+=tempo;
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contenuDuFichier;
    }

    public static List<Page> getManualFromFile(Context context) {
        String fileContent =readFile(context);
        List<Page> listOfPage = new ArrayList<>();

        String arrayOfPages[] = fileContent.split("Title;");

        for (int i=0;i<arrayOfPages.length;i++){
            String pageContent[] = arrayOfPages[i].split(";");
            for(int u=0;u<pageContent.length;u++) {
                String tempo[]=pageContent[u].split("//");
                String pageCategorie = tempo[0];
                String pagePicture = tempo[1];
                String pageText = tempo[2];

                Page page=new Page(pageCategorie,pagePicture,pageText);
                listOfPage.add(page);
            }
        }
        return listOfPage ;
    }
}
