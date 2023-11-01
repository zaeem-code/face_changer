package com.apploop.face.changer.app.manager

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.apploop.face.changer.app.bottomsheets.AdsLoadingDialog
import com.apploop.face.changer.app.utils.Constants
import com.apploop.face.changer.app.utils.SharedPrefHelper
import com.apploop.face.changer.app.R


class AdsManager {
    private var mInterstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var isEnabledNoAds = false
    private var context: Context? = null
    private var isButtonSelected = false
    private var mIsLoading = false


    fun initialize(context: Context) {
        this.context = context

        setEnabledNoAds(com.apploop.face.changer.app.utils.SharedPrefHelper.readBoolean(com.apploop.face.changer.app.utils.Constants.ADS_ID_KEY))

        if (isEnabledNoAds()) {
            return
        }
        MobileAds.initialize(context) {}

        loadInterstitial()
        loadRewardedAd()

    }

    fun isEnabledNoAds(): Boolean {
        return isEnabledNoAds
    }

    fun setEnabledNoAds(enabledNoAds: Boolean) {
        isEnabledNoAds = enabledNoAds
        com.apploop.face.changer.app.manager.OpenAdManager.getInstance().enabledNoAds = isEnabledNoAds
    }


    fun loadInterstitial() {
        try {
            if (!isNetworkAvailable(context) || isEnabledNoAds)
                return
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                context!!,
                context!!.getString(R.string.admob_interstial_ad_id),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd
                        Log.d("chk_add", "The ad was shown and load.")
//                        Toast.makeText(context, "inter onAdLoaded", Toast.LENGTH_SHORT).show()

                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error
                        mInterstitialAd = null
                    }
                })
        } catch (e: Exception) {

        }


    }

    fun showInterstitialAd(activity: Activity, onAdLoaded: com.apploop.face.changer.app.manager.OnAdLoaded) {
        if (isEnabledNoAds) {
            return
        }
        if (mInterstitialAd != null) {
            com.apploop.face.changer.app.bottomsheets.AdsLoadingDialog(activity, mInterstitialAd)
            if (mInterstitialAd == null) {
                return
            }
            mInterstitialAd!!.fullScreenContentCallback = object :
                FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("chk_add", "The ad was dismissed.")
                    mInterstitialAd = null
                    onAdLoaded.OnAdLoadedCallBack(true)
                    val adsChk= com.apploop.face.changer.app.utils.SharedPrefHelper.readBoolean("lastAds")
                    loadInterstitial()

                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when fullscreen content failed to show...
                    Log.d("chk_add", "The ad failed to show." + adError.message)
                    mInterstitialAd = null
                    onAdLoaded.OnAdLoadedCallBack(false)
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    Log.d("chk_add", "The ad was shown.")

                }
            }

        } else {
            loadInterstitial()
            onAdLoaded.OnAdLoadedCallBack(false)
        }
    }

    fun showInterstitialAd(activity: Activity) {
        if (isEnabledNoAds) {
            return
        }
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object :
                FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("chk_add", "The ad was dismissed.")
                    loadInterstitial()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when fullscreen content failed to show...
                    Log.d("chk_add", "The ad failed to show." + adError.message)
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    mInterstitialAd = null
                    Log.d("chk_add", "The ad was shown.")

                }
            }
            mInterstitialAd?.show(activity)

        } else {
            loadInterstitial()
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    fun loadRewardedAd() {
        if (rewardedAd == null) {
            mIsLoading = true
            val adRequest = AdManagerAdRequest.Builder().build()

            RewardedAd.load(
                context!!,
                context!!.getString(com.apploop.face.changer.app.R.string.reward_ad),
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d(TAG, adError.message)
                        mIsLoading = false
                        rewardedAd = null
                        loadRewardedAd()
                    }

                    override fun onAdLoaded(rewardedAd1: RewardedAd) {
                        Log.d(TAG, "Ad was loaded.")
                        rewardedAd = rewardedAd1
                        mIsLoading = false
                    }
                }
            )
        }
    }

    fun showRewardedVideo(activity: Activity, onAdLoaded: com.apploop.face.changer.app.manager.OnAdLoaded) {
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed.")
                        loadRewardedAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    }

                    override fun onAdShowedFullScreenContent() {
                        rewardedAd = null
                        Log.d(TAG, "Ad showed fullscreen content.")
                    }
                }

            rewardedAd?.show(activity) {
                onAdLoaded.OnAdLoadedCallBack(true)
            }

        }else{
            loadRewardedAd()
        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        try {
            // Set the media view.
            adView.mediaView = adView.findViewById<View>(R.id.ad_media) as MediaView

            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            val view = adView.findViewById<View>(R.id.ad_call_to_action)
            view.isSelected = isButtonSelected
            adView.callToActionView = view
            adView.iconView = adView.findViewById(R.id.ad_app_icon)
            adView.priceView = adView.findViewById(R.id.ad_price)
            adView.starRatingView = adView.findViewById(R.id.ad_stars)
            adView.storeView = adView.findViewById(R.id.ad_store)
            adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

            // The headline and mediaContent are guaranteed to be in every NativeAd.
            (adView.headlineView as TextView).text = nativeAd.headline
            adView.mediaView?.setMediaContent(nativeAd.mediaContent!!)


            // The headline and mediaContent are guaranteed to be in every NativeAd.
            (adView.headlineView as TextView).text = nativeAd.headline
            adView.mediaView?.setMediaContent(nativeAd.mediaContent!!)

            // These assets aren't guaranteed to be in every NativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.INVISIBLE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = "Ad - " + nativeAd.body
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.INVISIBLE
            } else {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as Button).text = nativeAd.callToAction
            }
            if (nativeAd.icon == null) {
                adView.iconView?.visibility = View.GONE
            } else {
                (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon?.drawable
                )
                adView.iconView?.visibility = View.VISIBLE
            }
            if (nativeAd.price == null) {
                adView.priceView?.visibility = View.INVISIBLE
            } else {
                adView.priceView?.visibility = View.VISIBLE
                (adView.priceView as TextView).text = nativeAd.price
            }
            if (nativeAd.store == null) {
                adView.storeView?.visibility = View.INVISIBLE
            } else {
                adView.storeView?.visibility = View.VISIBLE
                (adView.storeView as TextView).text = nativeAd.store
            }
            if (nativeAd.starRating == null) {
                adView.starRatingView?.visibility = View.INVISIBLE
            } else {
                (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                adView.starRatingView?.visibility = View.VISIBLE
            }
            if (nativeAd.advertiser == null) {
                adView.advertiserView?.visibility = View.INVISIBLE
            } else {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView?.visibility = View.VISIBLE
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            adView.setNativeAd(nativeAd)

            // Get the video controller for the ad. One will always be provided, even if the ad doesn't
            // have a video asset.
            val vc = nativeAd.mediaContent?.videoController

            // Updates the UI to say whether or not this ad has a video asset.
            if (vc?.hasVideoContent()!!) {

                // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
                // VideoController will call methods on this object when events occur in the video
                // lifecycle.
                vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                }


            } else {
                /* videoStatus.setText("Video status: Ad does not contain a video asset.");
            refresh.setEnabled(true);*/
            }
        } catch (e: Exception) {
        }
    }

    fun showNativeAd(
        frameLayout: FrameLayout,
        adContainer: View,
        layoutInflater: LayoutInflater,
        layout: Int,
        onAdLoaded: com.apploop.face.changer.app.manager.OnAdLoaded
    ) {
        if (!isNetworkAvailable(context) || isEnabledNoAds) {
            onAdLoaded.OnAdLoadedCallBack(false)
            return

        }
        isButtonSelected = false
        val builder =
            AdLoader.Builder(
                context!!,
                context!!.getString(R.string.admob_native_ad_unit)
            )

        // OnLoadedListener implementation.
        builder.forNativeAd { nativeAd: NativeAd ->
            val adView = layoutInflater.inflate(layout, null) as NativeAdView
            populateNativeAdView(nativeAd, adView)
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
            if (frameLayout.visibility == View.GONE) {
                frameLayout.visibility = View.VISIBLE
            }
        }
        val videoOptions = VideoOptions.Builder().setStartMuted(true).build()
        val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder
            .withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.d("chk_ad", "" + loadAdError.code)
                        onAdLoaded.OnAdLoadedCallBack(false)
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        if (adContainer.visibility == View.GONE) {
                            adContainer.visibility = View.VISIBLE
                        }
                        onAdLoaded.OnAdLoadedCallBack(true)

                    }
                })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }


    fun showNativeAd(
        frameLayout: FrameLayout,
        adContainer: View,
        layoutInflater: LayoutInflater,
        layout: Int
    ) {
        if (!isNetworkAvailable(context) || isEnabledNoAds) return
        isButtonSelected = false
        val builder =
            AdLoader.Builder(context!!, context!!.getString(R.string.admob_native_ad_unit))

        // OnLoadedListener implementation.
        builder.forNativeAd { nativeAd: NativeAd ->
            val adView = layoutInflater.inflate(layout, null) as NativeAdView
            populateNativeAdView(nativeAd, adView)
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
            if (frameLayout.visibility == View.GONE) {
                frameLayout.visibility = View.VISIBLE
            }
        }
        val videoOptions = VideoOptions.Builder().setStartMuted(true).build()
        val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder
            .withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.d("chk_ad", "" + loadAdError.code)
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        if (adContainer.visibility == View.GONE) {
                            adContainer.visibility = View.VISIBLE
                        }


                    }
                })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun showAdMobBanner(activity: Activity, frameLayout: FrameLayout?, onAdLoaded: com.apploop.face.changer.app.manager.OnAdLoaded) {
        if (!isNetworkAvailable(context) || isEnabledNoAds) {
            onAdLoaded.OnAdLoadedCallBack(false)

            return
        }
        if (frameLayout == null) return
        val adView = AdView(activity)
        adView.adUnitId = context!!.getString(R.string.admob_banner_ad_id)
        frameLayout.removeAllViews()
        frameLayout.addView(adView)
//        val adSize = getAdSize(activity)
//        adView.adSize = adSize
        adView.setAdSize(AdSize.FULL_BANNER)
        val adRequest = AdRequest.Builder().build()
        // Start loading the ad in the background.
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("chk_add", "load")
                onAdLoaded.OnAdLoadedCallBack(true)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                // Code to be executed when an ad request fails.
                onAdLoaded.OnAdLoadedCallBack(false)


            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }


    }

//
//    fun showAdMobBanner(activity: Activity, frameLayout: FrameLayout?) {
//        if (!isNetworkAvailable(context) || isEnabledNoAds) return
//        if (frameLayout == null) return
//        val adView = AdView(activity)
//        adView.adUnitId = context!!.getString(R.string.admob_banner_ad_id)
//        frameLayout.removeAllViews()
//        frameLayout.addView(adView)
//        val adSize = getAdSize(activity)
//        adView.adSize = adSize
//        val adRequest = AdRequest.Builder().build()
//        // Start loading the ad in the background.
//        adView.loadAd(adRequest)
//        adView.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                Log.d("chk_add", "load")
//
//            }
//
//            override fun onAdFailedToLoad(p0: LoadAdError) {
//                // Code to be executed when an ad request fails.
//
//
//            }
//
//            override fun onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            override fun onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            override fun onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        }
//
//
//    }


    fun loadAdaptiveBanner(activity: Activity?, frameLayout: FrameLayout?) {
        if (!isNetworkAvailable(context) || isEnabledNoAds) return
        if (frameLayout == null) return
        val adView = AdView(activity!!)
        adView.adUnitId = context!!.getString(R.string.admob_banner_ad_id)
        frameLayout.removeAllViews()
        frameLayout.addView(adView)
        adView.setAdSize(AdSize.FULL_BANNER)
        val adRequest = AdRequest.Builder().build()
        // Start loading the ad in the background.
        adView.loadAd(adRequest)

    }

    private fun getAdSize(activity: Activity): AdSize {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        val display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }


    companion object {
        private val TAG = AdsManager::class.java.name

        val instance: AdsManager? by lazy {
            AdsManager()
        }
    }


}