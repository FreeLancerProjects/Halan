package com.semicolon.Halan.Services;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Delta on 19/04/2018.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
