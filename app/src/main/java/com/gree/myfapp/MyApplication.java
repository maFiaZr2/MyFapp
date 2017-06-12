package com.gree.myfapp;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;


/**
 * Created by asus on 2016/9/7.
 */
public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
