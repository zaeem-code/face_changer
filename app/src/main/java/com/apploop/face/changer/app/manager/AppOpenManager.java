package com.apploop.face.changer.app.manager;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.apploop.face.changer.app.R;
import com.apploop.face.changer.app.app.App;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;


import java.util.Calendar;
import java.util.Date;




public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String LOG_TAG = "AppOpenManager";
    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;
    private Long lastTimeAdShown = 00L;
    private long loadTime = 0;

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private final App myApplication;
    public Boolean isFirstAdShown = false;
    public Boolean isFailedToLoad = false;
    private static AppOpenManager instance;


    public static AppOpenManager getInstance(App myApplication)
    {
        if (instance==null)
            instance = new AppOpenManager(myApplication);
        return instance;
    }
    /**
     * Constructor
     */
    public AppOpenManager(App myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
//        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
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
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Request an ad
     */
    public void fetchAd(OpenAdCallback openAdCallback) {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
//                        if (!isFirstAdShown)
                            showAdIfAvailable(openAdCallback);
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.d(LOG_TAG, "onAdFailedToLoad: ");
                        openAdCallback.onFailed();
                        // Handle the error.
                    }


                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, myApplication.getString(R.string.openAds), request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }
    public void fetchAd() {
        // Have unused ad, no need to fetch another.

        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
                        isFailedToLoad = false;
//                        if (!isFirstAdShown)
//                        showAdIfAvailable();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.d(LOG_TAG, "onAdFailedToLoad: ");
                        isFailedToLoad = true;
//                        openAdCallback.onFailed();
                        // Handle the error.
                    }


                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, myApplication.getString(R.string.openAds), request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }
    /**
     * ActivityLifecycleCallback methods
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

    private static boolean isShowingAd = false;

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
     * @param openAdCallback
     */
    public void showAdIfAvailable(OpenAdCallback openAdCallback) {
        if (5000 > (Calendar.getInstance().getTimeInMillis() - lastTimeAdShown)) {
            openAdCallback.onFailed();
            return;
        }

            // Only show ad if there is not already an app open ad currently showing
            // and an ad is available.
            if (!isShowingAd && isAdAvailable()) {
                Log.d(LOG_TAG, "Will show ad.");

                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Set the reference to null so isAdAvailable() returns false.
                                lastTimeAdShown = Calendar.getInstance().getTimeInMillis();
                                AppOpenManager.this.appOpenAd = null;
                                isShowingAd = false;
                                fetchAd();
                                openAdCallback.onDismiss();
                                Log.d(LOG_TAG, "onAdDismissedFullScreenContent: ");

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: ");
                                openAdCallback.onFailed();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                isShowingAd = true;
                            }
                        };

                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(currentActivity);

            } else {
                openAdCallback.onFailed();
            }
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
        showAdIfAvailable(new OpenAdCallback() {
                @Override
                public void onDismiss() {
                }

                @Override
                public void onFailed() {
                }
            });

    }

    public void loadAndShowOpenAd(OpenAdCallback openAdCallback) {
        showAdIfAvailable(openAdCallback);
    }
}