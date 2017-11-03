package com.example.ervand.voloappnotes.app;

import android.app.Application;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    private static final String TAG = "baseApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        Log.i(TAG,"Init Realm");

    }
}
