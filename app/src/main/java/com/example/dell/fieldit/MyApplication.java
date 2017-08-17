package com.example.dell.fieldit;

import android.app.Application;
import android.content.Context;

/**
 * Created by galna21 on 17/08/2017.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        // Get the application context
        MyApplication.context = getApplicationContext();
    }
    public static Context getAppContext() {
        // Return the application context
        return MyApplication.context;
    }
}
