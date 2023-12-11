package com.apploop.face.changer.app.app;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.app.Application;


import com.apploop.face.changer.app.cropModule.UCropHttpClientStore;
import com.apploop.face.changer.app.manager.AdsManager;
import com.apploop.face.changer.app.manager.AppOpenManager;

import java.util.Objects;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

public class App extends Application {

    private static App mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        setUCropHttpClient();
        mContext = this;
//        AppOpenManager.getInstance(this).fetchAd();


    }

    public static App getContext() {
        return mContext;
    }

    private void setUCropHttpClient() {
        ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .allEnabledCipherSuites()
                .allEnabledTlsVersions()
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(listOf(cs))
                .build();
        UCropHttpClientStore.INSTANCE.setClient(client);
    }

}
