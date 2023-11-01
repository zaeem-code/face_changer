package com.apploop.face.changer.app.app;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.apploop.face.changer.app.utils.Constants;
import com.apploop.face.changer.app.utils.SharedPrefHelper;
import com.apploop.face.changer.app.utils.Constants;
import com.apploop.face.changer.app.utils.SharedPrefHelper;

public class AppLifeCycleClass implements LifecycleObserver {
    public String TAG = "AppLifeCycle";
    public Context context;

    public void init(){
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        context = App.Companion.applicationContext();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.d(TAG, "onStart: ");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        SharedPrefHelper.writeBoolean(Constants.IN_APP_KEY,false);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.d(TAG, "onResume: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Log.d(TAG, "onStop: ");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.d(TAG, "onPause: ");
    }
}