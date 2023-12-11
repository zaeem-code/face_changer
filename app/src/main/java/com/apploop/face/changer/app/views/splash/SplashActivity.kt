package com.apploop.face.changer.app.views.splash

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.app.App
import com.apploop.face.changer.app.app.MyApplication
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.manager.AdsManager.Companion.instance
import com.apploop.face.changer.app.manager.AppOpenManager
import com.apploop.face.changer.app.manager.GoogleMobileAdsConsentManager
import com.apploop.face.changer.app.manager.OpenAdCallback
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.views.mainactivity.MainActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import java.util.Objects


private const val COUNTER_TIME_MILLISECONDS = 5000L

private const val LOG_TAG = "SplashActivity"

/** Splash Activity that inflates splash activity xml. */
class SplashActivity : AppCompatActivity() {

    private lateinit var lottieAnimator: LottieAnimationView
    private lateinit var adsText: TextView
    var appUpdateManager: AppUpdateManager? = null
    private val openAdManager = AppOpenManager.getInstance(MyApplication.Companion.applicationContext())
    private var tryAgainForAd = true
    private var mLastClickTime = 0L

    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        statusBarColor(R.color.background)

        lottieAnimator=findViewById(R.id.animation_view)
        adsText=findViewById(R.id.ads_txt)

        /// inApp update
        appUpdateManager = AppUpdateManagerFactory.create(this)

        updateApp()
    }


    private fun initView() {

        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(applicationContext)

        googleMobileAdsConsentManager.gatherConsent(this) { consentError ->
            if (consentError != null) {
                // Consent not obtained in current session.
                Log.d(
                    "egtspll", String.format("%s: %s", consentError.errorCode, consentError.message))
            }
            if (googleMobileAdsConsentManager.canRequestAds) {
                initializeMobileAdsSdk()
                continueWithSplash()
            }

            Log.d("egtspll", "String.format cnsentError.errorCode, consentError.message)")

        }


        // This sample attempts to load ads using consent obtained in the previous session.
        if (googleMobileAdsConsentManager.canRequestAds) {
            initializeMobileAdsSdk()
            continueWithSplash()

        }


    }

    private fun initializeMobileAdsSdk() {


        // Initialize the Mobile Ads SDK.
        //        AppOpenManager.getInstance(this).fetchAd();
        AdsManager.Companion.instance?.initialize(this)

        // Load an ad.
//        AppOpenManager.getInstance(App.getContext()).fetchAd()
        // Load an ad.
        (application as MyApplication).loadAd(this)
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
//        if (openAdManager.isAdAvailable){
//
//
//            openAdManager.showAdIfAvailable(object : OpenAdCallback
//            {
//                override fun onDismiss()
//                {
//                    openLoginActivity()
//                }
//
//                override fun onFailed()
//                {
//                    openLoginActivity()
//                }
//
//            })
//        }
//        else
//        {
//            Handler(Looper.getMainLooper()).postDelayed({
//                openAdManager.showAdIfAvailable(object : OpenAdCallback
//                {
//                    override fun onDismiss()
//                    {
//                        openLoginActivity()
//                    }
//                    override fun onFailed()
//                    {
//                        if (tryAgainForAd)
//                        {
//                            showOpenAd()
//                            tryAgainForAd = false
//                        }else
//                        {
//                            openLoginActivity()
//                        }
//                    }
//                })
//            },3850)
//        }

//         if (openAdManager.isAdAvailable){
//
//             (application as MyApplication).showAdIfAvailable(this@SplashActivity,
//                 object : MyApplication.OnShowAdCompleteListener {
//                     override fun onShowAdComplete() {
//                         // Check if the consent form is currently on screen before moving to the main
//                         // activity.
//                         if (googleMobileAdsConsentManager.canRequestAds) {
//                             openLoginActivity()
//                         }
//                     }
//                 } )
//        }
//        else
//        {
            Handler(Looper.getMainLooper()).postDelayed({
                (application as MyApplication).showAdIfAvailable(this@SplashActivity,
                    object : MyApplication.OnShowAdCompleteListener {
                        override fun onShowAdComplete() {
                            // Check if the consent form is currently on screen before moving to the main
                            // activity.
                            if (googleMobileAdsConsentManager.canRequestAds) {
                                openLoginActivity()
                            }
                        }
                    } )



            },3850)
//        }



    }

    private fun openLoginActivity() {

        // load ads
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
//                continueWithSplash()
                initView()
            } else {
//                continueWithSplash()
                initView()
            }
        }
    }

}