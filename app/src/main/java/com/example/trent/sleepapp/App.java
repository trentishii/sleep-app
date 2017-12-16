package com.example.trent.sleepapp;

import android.app.Application;
import android.content.Intent;

/**
 * Created by vinisha on 12/5/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, YourService.class));
    }
}