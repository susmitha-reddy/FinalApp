package com.example.smartcart;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;


public class RealmApplication extends Application {
    String appId = "smartcart-yefkx";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Log.d("Realm", "Realm is initialized");
    }
}
