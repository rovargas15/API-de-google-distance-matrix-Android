package com.example.empresariales.empresariales.util;

import android.app.Application;


public class ApplicationController extends Application {

    private static ApplicationController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }
}
