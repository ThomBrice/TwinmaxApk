package com.example.isen.twinmaxapk;

import android.app.Application;
import android.content.Context;

/**
 * Created by isen on 18/04/2016.
 */
public class TMApplication extends Application {

    private static Context sContext;

    public void onCreate(){
        super.onCreate();

        // Keep a reference to the application context
        sContext = getApplicationContext();
    }

    // Used to access Context anywhere within the app
    public static Context getContext() {
        return sContext;
    }
}
