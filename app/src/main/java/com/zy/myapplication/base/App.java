package com.zy.myapplication.base;

import android.app.Application;

import com.android.volley.manager.RequestManager;

/**
 * Created by Android on 2017/8/3.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.getInstance().init(this);
    }
}
