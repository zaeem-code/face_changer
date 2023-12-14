package com.apploop.face.changer.app.views.splash

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.airbnb.lottie.LottieAnimationView
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.app.MyApplication
import com.apploop.face.changer.app.manager.GoogleMobileAdsConsentManager
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.SharedPrefHelper
import com.apploop.face.changer.app.views.mainactivity.MainActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


private const val COUNTER_TIME_MILLISECONDS = 5000L

private const val LOG_TAG = "SplashActivity"

/** Splash Activity that inflates splash activity xml. */
  class SplashActivity : AppCompatActivity() {

    private lateinit var lottieAnimator: LottieAnimationView
    private lateinit var adsText: TextView
    var appUpdateManager: AppUpdateManager? = null
    private var tryAgainForAd = true
    private var mLastClickTime = 0L
    private var secondsRemaining: Long = 0L
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)

    private var splashLoaded = false

    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition { true }
        setContentView(R.layout.activity_splash)
        statusBarColor(R.color.background)

        lottieAnimator=findViewById(R.id.animation_view)
        adsText=findViewById(R.id.ads_txt)

        splashLoaded=SharedPrefHelper.readBoolean("splashLoaded")

        if (!splashLoaded)
        {
            Log.d("egtspll", "onCreate")
            SharedPrefHelper.writeBoolean("splashLoaded",true)
            initView()
        }






    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("egtspll", "onDestroy")

    }




    private fun createTimer(time: Long) {
        val countDownTimer: CountDownTimer =
            object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                }

                override fun onFinish() {
                    secondsRemaining = 0

                    continueWithSplash()

                }
            }
        countDownTimer.start()

    }


    private fun initView() {



            createTimer(COUNTER_TIME_MILLISECONDS)
//            googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(applicationContext)
//            googleMobileAdsConsentManager.gatherConsent(this) { consentError ->
//                if (consentError != null) {
//                    // Consent not obtained in current session.
//                    Log.d("egtspll", String.format("%s: %s", consentError.errorCode, consentError.message))
//                }
//                if (googleMobileAdsConsentManager.canRequestAds) {
//                    initializeMobileAdsSdk()
//                    Log.d("egtspll", "googleMobileAdsConsentManager -------->  initializeMobileAdsSdk")
//                }
//
//
//                if (secondsRemaining <= 0) {
//                    continueWithSplash()
//                    Log.d("egtspll", "time less then 0 --------> openLoginActivity")
//
//                }
//            }





    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Mobile Ads SDK.
//        AdsManager.Companion.instance?.initialize(this)

        // Load an ad.
//        (application as MyApplication).loadAd(this)
    }

    private fun continueWithSplash() {

         openLoginActivity()
    }


    private fun openLoginActivity() {
        // load ads
        startActivity(Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
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
                    Log.d("egtspll", "else updateApp--------> initView ")

                }
            }.addOnFailureListener { e: java.lang.Exception ->
                e.printStackTrace()
                initView()
                Log.d("egtspll", "addOnFailureListener updateApp-------->initView  ")

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