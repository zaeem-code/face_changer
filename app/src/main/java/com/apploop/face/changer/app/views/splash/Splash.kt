package com.apploop.face.changer.app.views.splash

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.manager.OpenAdManager
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.views.mainactivity.MainActivity

class Splash : AppCompatActivity() {

    private lateinit var lottieAnimator: LottieAnimationView
    private lateinit var adsText: TextView
    var appUpdateManager: AppUpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        statusBarColor(R.color.background)

        /// inApp update
        appUpdateManager = AppUpdateManagerFactory.create(this)


        lottieAnimator=findViewById(R.id.animation_view)
        adsText=findViewById(R.id.ads_txt)


        updateApp()

    }


    private fun homeScreen()
    {
        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            if (com.apploop.face.changer.app.manager.OpenAdManager.getInstance().isAdAvailable && !com.apploop.face.changer.app.manager.OpenAdManager.getInstance().enabledNoAds)
            {
                com.apploop.face.changer.app.manager.OpenAdManager.getInstance().showAdIfAvailable(this)
            } else {


                lottieAnimator.visibility= View.VISIBLE
                adsText.visibility= View.VISIBLE

                Handler(Looper.getMainLooper()).postDelayed({
                    if (com.apploop.face.changer.app.manager.OpenAdManager.getInstance().isAdAvailable && !com.apploop.face.changer.app.manager.OpenAdManager.getInstance().enabledNoAds)
                    {
                        lottieAnimator.visibility= View.INVISIBLE
                        adsText.visibility= View.INVISIBLE

                        com.apploop.face.changer.app.manager.OpenAdManager.getInstance().showAdIfAvailable(this)
                    }
                    else
                    {
                        lottieAnimator.visibility= View.INVISIBLE
                        adsText.visibility= View.INVISIBLE

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                },4000)

            }



        }, 3000)

    }

    fun updateApp() {
        try {
            val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
                ) {
                    try {
                        appUpdateManager!!.startUpdateFlowForResult(
                            appUpdateInfo, IMMEDIATE, this, 101
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                } else {
                    homeScreen()
                }
            }.addOnFailureListener { e: java.lang.Exception ->
                e.printStackTrace()
                homeScreen()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode != RESULT_OK) {
                homeScreen()
            } else {
                homeScreen()
            }
        }
    }




}