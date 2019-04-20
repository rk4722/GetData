package com.swadesiapps.getdata;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class AppClas extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}
