package com.apploop.face.changer.app.manager;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.apploop.face.changer.app.R;
import com.apploop.face.changer.app.app.App;
import com.apploop.face.changer.app.utils.Constants;
import com.apploop.face.changer.app.utils.SharedPrefHelper;
import com.apploop.face.changer.app.views.mainactivity.MainActivity;


import java.util.Date;

public class OpenAdManager {
    private static final String TAG = "OpenAdManager";

    private static final String LOG_TAG = "AppOpenManager";
    private AppOpenAd appOpenAd = null;

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private App myApplication;
    private Activity currentActivity;

    public static boolean isShowingAd = false;
    private long loadTime = 0;

    public boolean isOpenAdDisplayed;
    private static OpenAdManager manager;
    private boolean isAdsDisabled = false;

    /**
     * Constructor
     */

    public OpenAdManager(App myApplication) {
        this.myApplication = myApplication;
//        this.myApplication.registerActivityLifecycleCallbacks(this);
    }

    public void initialize(App myApplication) {
        this.myApplication = myApplication;
        isAdsDisabled = SharedPrefHelper.readBoolean(Constants.ADS_ID_KEY);
    }

    public void setEnabledNoAds(boolean isAdsDisabled) {
        this.isAdsDisabled = isAdsDisabled;
    }

    public boolean getEnabledNoAds() {
        return isAdsDisabled;
    }

    public OpenAdManager() {

    }

    public static OpenAdManager getInstance() {
        if (manager == null) {
            manager = new OpenAdManager();
        }
        return manager;
    }

    /**
     * LifecycleObserver methods
     */

    @OnLifecycleEvent(ON_START)
    public void onStart() {
//        showAdIfAvailable();
        Log.d("OPENAD", "onStart");
    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        if (!isAdsDisabled) {
            // We will implement this below.
            if (isAdAvailable()) {
                return;
            }

            loadCallback =
                    new AppOpenAd.AppOpenAdLoadCallback() {

                        @Override
                        public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                            super.onAdLoaded(appOpenAd);
                            OpenAdManager.this.appOpenAd = appOpenAd;
                            OpenAdManager.this.loadTime = (new Date()).getTime();
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError);
                        }
                    };

            AdRequest request = getAdRequest();
            AppOpenAd.load(
                    myApplication, this.myApplication.getString(R.string.openAds), request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
        }

    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        return appOpenAd != null;
//                && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    public void showAdIfAvailable(final Activity activity) {
        if (!isAdsDisabled) {
            // Only show ad if there is not already an app open ad currently showing
            // and an ad is available.
            if (!isShowingAd && isAdAvailable()) {
                Log.d(LOG_TAG, "Will show ad.");

                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Set the reference to null so isAdAvailable() returns false.
                                OpenAdManager.this.appOpenAd = null;
                                isShowingAd = false;

                                try {
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                } catch (Exception e) {
                                    Log.e(TAG, "onAdDismissedFullScreenContent: " + e.getMessage());
                                }

                                fetchAd();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                try {
                                    Intent i;
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                } catch (Exception e) {
                                    Log.e(TAG, "onAdDismissedFullScreenContent: " + e.getMessage());
                                }

                                fetchAd();

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                isShowingAd = true;
                            }
                        };

                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(activity);

            } else {
                Log.d(LOG_TAG, "Can not show ad.");
                try {
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                } catch (Exception e) {
                    Log.e(TAG, "onAdDismissedFullScreenContent: " + e.getMessage());
                }
                fetchAd();
            }
        }

    }
}