package com.example.lukaszjarka.cardatabase;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isInDebug = BuildConfig.DEBUG;
        Stetho.initializeWithDefaults(this);
    }
}
