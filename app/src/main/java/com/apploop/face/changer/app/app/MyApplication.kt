package com.apploop.face.changer.app.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.apploop.face.changer.app.manager.GoogleMobileAdsConsentManager
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import java.util.Date

/** Application class that initializes, loads and show ads when activities change states. */
class MyApplication :
  MultiDexApplication(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

  private var currentActivity: Activity? = null

  companion object {
    private var instance: MyApplication? = null

    fun applicationContext(): MyApplication {
      return instance as MyApplication
    }
  }


  override fun onCreate() {
    super.onCreate()
    setUCropHttpClient()
    instance = this;

    registerActivityLifecycleCallbacks(this)

    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
  }

  override fun onActivityCreated(p0: Activity, p1: Bundle?) {
  }

  override fun onActivityStarted(p0: Activity) {
  }


  override fun onActivityResumed(activity: Activity) {}

  override fun onActivityPaused(activity: Activity) {}

  override fun onActivityStopped(activity: Activity) {}

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

  override fun onActivityDestroyed(activity: Activity) {}




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