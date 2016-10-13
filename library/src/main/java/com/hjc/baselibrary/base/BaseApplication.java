package com.hjc.baselibrary.base;

import android.app.Application;

/**
 * Base Application
 * Created by hjc on 2016/10/13.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
