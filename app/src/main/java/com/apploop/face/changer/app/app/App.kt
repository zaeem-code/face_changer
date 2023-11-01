package com.apploop.face.changer.app.app

import android.app.Application

import com.apploop.face.changer.app.cropModule.UCropHttpClientStore
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.manager.OpenAdManager
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient

class App : Application() {

    companion object {
        private var instance: App? = null

        fun applicationContext(): App {
            return instance as App
        }
    }

    override fun onCreate() {
        super.onCreate()
        setUCropHttpClient()
        instance = this;

        AdsManager.instance?.initialize(this)
        com.apploop.face.changer.app.manager.OpenAdManager.getInstance().initialize(this)
        com.apploop.face.changer.app.manager.OpenAdManager.getInstance().fetchAd()





    }

    private fun setUCropHttpClient() {
        val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .allEnabledCipherSuites()
            .allEnabledTlsVersions()
            .build()
        val client = OkHttpClient.Builder()
            .connectionSpecs(listOf(cs))
            .build()
        com.apploop.face.changer.app.cropModule.UCropHttpClientStore.INSTANCE.client = client
    }




}