package com.apploop.face.changer.app.views.premium

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.InAppBilling.SubscriptionBillingManager
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.databinding.ActivityPremiumBinding
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.manager.OnAdLoaded
import com.apploop.face.changer.app.utils.Constants
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.SharedPrefHelper
import com.apploop.face.changer.app.views.mainactivity.MainActivity

class PremiumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPremiumBinding
    var priceTag = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_premium)
        statusBarColor(R.color.white)

        com.apploop.face.changer.app.utils.SharedPrefHelper.writeBoolean(com.apploop.face.changer.app.utils.Constants.IN_APP_KEY,true)


        com.apploop.face.changer.app.InAppBilling.SubscriptionBillingManager(this, this)


        init()
    }

    private fun init() {



        binding.lvMonth.setOnClickListener {

            binding.ivCheck.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle));
            binding.ivCheck.visibility = View.VISIBLE
            binding.ivCheck1.visibility = View.INVISIBLE
            binding.ivCheck2.visibility = View.INVISIBLE
            priceTag = com.apploop.face.changer.app.utils.Constants.SKU_ITEM_ONE_MONTH

        }


        binding.lvSixMonth.setOnClickListener {

            binding.ivCheck1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle));
            binding.ivCheck1.visibility = View.VISIBLE
            binding.ivCheck.visibility = View.INVISIBLE
            binding.ivCheck2.visibility = View.INVISIBLE
            priceTag = com.apploop.face.changer.app.utils.Constants.SKU_ITEM_SIX_MONTH

        }

        binding.lvYearly.setOnClickListener {

            binding.ivCheck2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle));
            binding.ivCheck2.visibility = View.VISIBLE
            binding.ivCheck1.visibility = View.INVISIBLE
            binding.ivCheck.visibility = View.INVISIBLE
            priceTag = com.apploop.face.changer.app.utils.Constants.SKU_ITEM_ONE_YEAR


        }

        binding.ivButton.setOnClickListener {

            if (priceTag == "empty") {
                binding.ivCheck.visibility = View.VISIBLE
                binding.ivCheck1.visibility = View.INVISIBLE
                binding.ivCheck2.visibility = View.INVISIBLE
                binding.ivCheck.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle));

//                makePurchaseViewModel!!.buySku(this, Constants.SKU_ITEM_ONE_MONTH)
                com.apploop.face.changer.app.InAppBilling.SubscriptionBillingManager(this, this)
                    .subscribe(com.apploop.face.changer.app.utils.Constants.SKU_ITEM_ONE_MONTH)

            } else {
                com.apploop.face.changer.app.InAppBilling.SubscriptionBillingManager(this, this).subscribe(priceTag)

//                makePurchaseViewModel!!.buySku(this, priceTag)
            }

        }

        binding.ivCross.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            AdsManager.Companion.instance!!.showInterstitialAd(this, object :
                com.apploop.face.changer.app.manager.OnAdLoaded {
                override fun OnAdLoadedCallBack(loaded: Boolean?) {
                    binding.progressBar.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    finish()
                    startActivity(Intent(this@PremiumActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                }

            })
        }

    }

    override fun onBackPressed() {
//        AdsManager.instance?.showInterstitialAd(this)
//        finish()

                binding.progressBar.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                AdsManager.Companion.instance!!.showInterstitialAd(this, object :
                    com.apploop.face.changer.app.manager.OnAdLoaded {
                    override fun OnAdLoadedCallBack(loaded: Boolean?) {
                        binding.progressBar.visibility = View.GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        finish()
                        startActivity(Intent(this@PremiumActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    }

                })


    }
}