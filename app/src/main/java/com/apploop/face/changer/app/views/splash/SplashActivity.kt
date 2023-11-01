package com.apploop.face.changer.app.views.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.app.MyApplication
import com.apploop.face.changer.app.manager.GoogleMobileAdsConsentManager
import com.apploop.face.changer.app.views.premium.PremiumActivity
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

private const val COUNTER_TIME_MILLISECONDS = 5000L

private const val LOG_TAG = "SplashActivity"

/** Splash Activity that inflates splash activity xml. */
class SplashActivity : AppCompatActivity() {

    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private var secondsRemaining: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Log the Mobile Ads SDK version.

        // Create a timer so the SplashActivity will be displayed for a fixed amount of time.
        createTimer(COUNTER_TIME_MILLISECONDS)

        GoogleMobileAdsConsentManager.getInstance(this).gatherConsent(this) { consentError ->
            if (consentError != null) {
                // Consent not obtained in current session.
                Log.w(LOG_TAG, String.format("%s: %s", consentError.errorCode, consentError.message))
            }

            if (GoogleMobileAdsConsentManager.getInstance(this).canRequestAds) {
                initializeMobileAdsSdk()
            }

            if (secondsRemaining <= 0) {
                startMainActivity()
            }
        }

        // This sample attempts to load ads using consent obtained in the previous session.
        if (GoogleMobileAdsConsentManager.getInstance(this).canRequestAds) {
            initializeMobileAdsSdk()
        }
    }

    /**
     * Create the countdown timer, which counts down to zero and show the app open ad.
     *
     * @param time the number of milliseconds that the timer counts down from
     */
    private fun createTimer(time: Long) {
        val counterTextView: TextView = findViewById(R.id.ads_txt)

        val countDownTimer: CountDownTimer =
            object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                    counterTextView.text = "This action can contain ads... loading in: $secondsRemaining"

                }

                override fun onFinish() {
                    secondsRemaining = 0
                    counterTextView.text = "Done."

                    (application as MyApplication).showAdIfAvailable(
                        this@SplashActivity,
                        object : MyApplication.OnShowAdCompleteListener {
                            override fun onShowAdComplete() {
                                // Check if the consent form is currently on screen before moving to the main
                                // activity.
                                startMainActivity()

//                                if (GoogleMobileAdsConsentManager.getInstance(this@SplashActivity).canRequestAds) {
//                                    startMainActivity()
//                                }
                            }
                        }
                    )
                }
            }
        countDownTimer.start()
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }



        // Load an ad.
        (application as MyApplication).loadAd(this)
    }

    /** Start the MainActivity. */
    fun startMainActivity() {
        val intent = Intent(this, PremiumActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()

    }
}