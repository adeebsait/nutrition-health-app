package com.example.finalyear;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

@HiltAndroidApp
public class DietApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        FirebaseApp.getInstance();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
