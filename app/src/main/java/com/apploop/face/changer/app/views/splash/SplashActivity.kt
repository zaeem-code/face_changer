package com.apploop.face.changer.app.views.splash

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.app.App
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.manager.AppOpenManager
import com.apploop.face.changer.app.manager.GoogleMobileAdsConsentManager
import com.apploop.face.changer.app.manager.OpenAdCallback
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.views.mainactivity.MainActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability


private const val COUNTER_TIME_MILLISECONDS = 5000L

private const val LOG_TAG = "SplashActivity"

/** Splash Activity that inflates splash activity xml. */
class SplashActivity : AppCompatActivity() {

    var appUpdateManager: AppUpdateManager? = null
    private val openAdManager = AppOpenManager.getInstance(App.getContext())
    private var tryAgainForAd = true
    private var mLastClickTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        statusBarColor(R.color.background)



        /// inApp update
        appUpdateManager = AppUpdateManagerFactory.create(this)

        updateApp()
    }





    private fun initView() {

        GoogleMobileAdsConsentManager.getInstance(this).gatherConsent(this) { consentError ->
            if (consentError != null) {
                // Consent not obtained in current session.
                Log.d(
                    "egtspll",
                    String.format("%s: %s", consentError.errorCode, consentError.message)
                )
                continueWithSplash()
            }
            Log.d("egtspll", "String.format cnsentError.errorCode, consentError.message)")
            if (GoogleMobileAdsConsentManager.getInstance(this).canRequestAds) {
                continueWithSplash()
            }
        }


    }

    private fun continueWithSplash() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
            Log.d("splash", "init: prevent double click")
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()


        showOpenAd()

    }

    private fun showOpenAd()
    {
        if (openAdManager.isAdAvailable){


            openAdManager.showAdIfAvailable(object : OpenAdCallback
            {
                override fun onDismiss()
                {
                    openLoginActivity()
                }

                override fun onFailed()
                {
                    openLoginActivity()
                }

            })
        }
        else
        {
            Handler(Looper.getMainLooper()).postDelayed({
                openAdManager.showAdIfAvailable(object : OpenAdCallback
                {
                    override fun onDismiss()
                    {
                        openLoginActivity()
                    }
                    override fun onFailed()
                    {
                        if (tryAgainForAd)
                        {
                            showOpenAd()
                            tryAgainForAd = false
                        }else
                        {
                            openLoginActivity()
                        }
                    }
                })
            },3850)
        }
    }

    private fun openLoginActivity() {

        // load ads
        AdsManager.getInstance().loadInterstitialAd(this)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun updateApp() {
        try {
            val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    try {
                        appUpdateManager!!.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, this, 101
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                } else {
                    initView()

                }
            }.addOnFailureListener { e: java.lang.Exception ->
                e.printStackTrace()
                initView()

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode != RESULT_OK) {
                continueWithSplash()
            } else {
                continueWithSplash()
            }
        }
    }

}