package com.example.xiao.a360speedball.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.xiao.a360speedball.engine.FloatViewManager;


public class FloatService extends Service {


    public FloatService() {


    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FloatViewManager manager = FloatViewManager.getInstance(this);
        manager.showFloatCircleView();



    }
}
