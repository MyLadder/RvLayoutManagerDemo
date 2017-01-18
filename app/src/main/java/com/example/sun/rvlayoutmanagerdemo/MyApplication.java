package com.example.sun.rvlayoutmanagerdemo;

import android.app.Application;

/**
 * Created by Administrator on 2017/1/16.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CardConfig.initConfig(this);
    }
}
