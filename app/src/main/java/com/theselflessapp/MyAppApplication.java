package com.theselflessapp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyAppApplication extends Application {

    private static MyAppApplication sInstance;


    public static MyAppApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        sInstance.initializeInstance();

    }

    private void initializeInstance() {

        // Initialize Fresco
        Fresco.initialize(this.getApplicationContext());
    }

    @Override
    public void onTerminate() {
        // Do your application wise Termination task
        super.onTerminate();
    }
}
