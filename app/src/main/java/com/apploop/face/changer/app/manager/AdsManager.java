package com.apploop.face.changer.app.manager;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.apploop.face.changer.app.R;
import com.apploop.face.changer.app.app.App;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import java.util.Calendar;
import java.util.Collections;

public class AdsManager {
    public enum NativeAdType {
        MEDIUM_TYPE,
        SMALL_TYPE,
        REGULAR_TYPE,
        NOMEDIA_MEDIUM
    }

    public enum BannerAdSize {
        LARGE
    }

    private static AdsManager manager;
    private final String TAG = AdsManager.class.getName();
    private InterstitialAd admobInterstitialAd;
    private RewardedAd rewardedAd;
    private Long lastTimeAdShown = 00L;
    private Long lastTimeAdShown1 = 00L;
    private Context context;

    private AdsManager() {
        context = App.getContext();
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(context, initializationStatus -> {
            Log.d(TAG, "AdsManager: initializes");
        });


    }

    public static AdsManager getInstance() {
        if (manager == null) {
            manager = new AdsManager();
        }
        return manager;
    }

    private AdRequest prepareAdRequest() {
        AdRequest adRequest;
        adRequest = new AdRequest.Builder().build();
        return adRequest;
    }

    // **********  INTERSITITAL ADS ************ //

    public void loadInterstitialAdIfNotLoaded(Context context) {
        if (admobInterstitialAd == null )
        {
            Log.d("loadInterstitialAdIfNotLoaded", "loadInterstitialAdIfNotLoaded: ad is not loaded");
            loadInterstitialAd(context);
        }
    }
    public void loadInterstitialAd(Context context) {
            InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    Log.d(TAG, "onLoad: admob interstitial");
                    admobInterstitialAd = interstitialAd;
                    Log.d("loadInterstitialAdIfNotLoaded", "onLoad: admob interstitial");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.d(TAG, "onAdFailedToLoad: admob interstitial. Loading facebook ad");
                }
            };
            InterstitialAd.load(context, context.getString(R.string.admob_interstial_ad_id), prepareAdRequest(), interstitialAdLoadCallback);
    }


    public void showInterstitialAd(Activity activity,OnAdLoaded OnAdLoaded) {
            if (1000 > (Calendar.getInstance().getTimeInMillis() - lastTimeAdShown)) {
                OnAdLoaded.OnAdLoadedCallBack(false);
                return;
            }
            if (admobInterstitialAd != null) {
                admobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        OnAdLoaded.OnAdLoadedCallBack(true);
                        super.onAdDismissedFullScreenContent();
                        Log.d(TAG, "onAdDismissedFullScreenContent: ");
                        lastTimeAdShown = Calendar.getInstance().getTimeInMillis();
                        admobInterstitialAd = null;
//                        loadInterstitialAd(activity);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: ");
                        OnAdLoaded.OnAdLoadedCallBack(false);
                    }
                });
                admobInterstitialAd.show(activity);
            } else {
                OnAdLoaded.OnAdLoadedCallBack(false);
            }

    }
    // ********* REWARDED ADS ***********//
    public void loadRewardedAd(Context context) {
        //only load if user is Free e.g requirement (font show it to trail user )
        if (rewardedAd == null) {

            RewardedAd.load(context, context.getString(R.string.reward_ad),
                    prepareAdRequest(), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.toString());
                            rewardedAd = null;
                            Log.d(TAG, "Rewarded onAdFailedToLoad. "+loadAdError.toString());

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            rewardedAd = ad;
                            Log.d(TAG, "Rewarded Ad was loaded.");
                        }
                    });
        }else {
            Log.d(TAG, "Rewarded failed to meet check. ");
        }
    }
    public Boolean isRewardedAdLoaded(){
        return rewardedAd != null;
    }
    public void showRewardedAd(Activity activity,OnAdLoaded OnAdLoaded) {
        //only load if user is Free e.g requirement (font show it to trail user )
            if (rewardedAd != null) {
                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        Log.d(TAG, "Rewarded Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        Log.d(TAG, "Rewarded Ad dismissed fullscreen content.");
                        rewardedAd = null;
                        OnAdLoaded.OnAdLoadedCallBack(false);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.e(TAG, "Rewarded Ad failed to show fullscreen content.");
                        rewardedAd = null;
                        OnAdLoaded.OnAdLoadedCallBack(false);
                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Rewarded Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Rewarded Ad showed fullscreen content.");
                    }
                });
                rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d(TAG, "Rewarded The user earned the reward.");
                        Log.d(TAG, "Rewarded rewardItem.getAmount() "+rewardItem.getAmount());
                        Log.d(TAG, "Rewarded rewardItem.getType() "+rewardItem.getType());
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        OnAdLoaded.OnAdLoadedCallBack(true);
                        rewardedAd = null;
                    }
                });
            } else {
                OnAdLoaded.OnAdLoadedCallBack(false);
            }
    }
    // ********* BANNER ADS ***********//


    public void showCustomBanner(Activity activity, FrameLayout adContainerView, String size,OnAdLoaded onAdLoaded) {

            AdView adView = new AdView(activity);
            adView.setAdUnitId(activity.getString(R.string.admob_banner_ad_id));
            adContainerView.addView(adView);

            AdSize adSize = getAdSize(activity);

            if (size.equals("LARGE_BANNER")) {
                adView.setAdSize(AdSize.LARGE_BANNER);
            } else if (size.equals("MEDIUM_RECTANGLE")) {
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            } else {
                adView.setAdSize(adSize);
            }


            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    onAdLoaded.OnAdLoadedCallBack(true);
                    Log.d(TAG, "onAdLoaded: ");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    onAdLoaded.OnAdLoadedCallBack(false);
                    super.onAdFailedToLoad(loadAdError);
                    Log.d(TAG, "onAdFailedToLoad: " + loadAdError.toString());
                }
            });

            if (isNetWorkAvailable(activity)) {
                adView.loadAd(prepareAdRequest());
            }else {
                onAdLoaded.OnAdLoadedCallBack(false);
            }
        }

    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWorkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetWorkInfo != null && activeNetWorkInfo.isConnected();
    }

    private AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    // ********** NATIVE ADS *********//

    public void loadNativeAd(Activity activity, FrameLayout frameLayout, NativeAdType adType) {
         AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getString(R.string.admob_native_ad_unit));
            builder.forNativeAd(nativeAd -> {
                NativeAdView nativeAdView = null;
                if (adType == NativeAdType.MEDIUM_TYPE)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_with_media, null);
                if (adType== NativeAdType.SMALL_TYPE)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_media, null);

                populateUnifiedNativeAdView(nativeAd, nativeAdView, adType);
                frameLayout.removeAllViews();
                frameLayout.addView(nativeAdView);
            });

            VideoOptions videoOptions =
                    new VideoOptions.Builder().setStartMuted(true).build();
            com.google.android.gms.ads.nativead.NativeAdOptions adOptions =
                    new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
            builder.withNativeAdOptions(adOptions);
            AdLoader adLoader = builder.withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.d(TAG, "onAdFailedToLoad: " + loadAdError);
                        }
                    })
                    .build();

            adLoader.loadAd(prepareAdRequest());
    }

    public void loadNativeAdCallback(Activity activity, FrameLayout frameLayout, NativeAdType adType,OnAdLoaded onAdLoaded) {

            AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getString(R.string.admob_native_ad_unit));
            builder.forNativeAd(nativeAd -> {
                NativeAdView nativeAdView = null;
                if (adType == NativeAdType.MEDIUM_TYPE)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_media, null);
                if (adType== NativeAdType.SMALL_TYPE)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_media, null);
                 if (adType== NativeAdType.REGULAR_TYPE)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_with_media, null);

                populateUnifiedNativeAdView(nativeAd, nativeAdView,adType);
                frameLayout.removeAllViews();
                frameLayout.addView(nativeAdView);
            });

            VideoOptions videoOptions =
                    new VideoOptions.Builder().setStartMuted(true).build();
            com.google.android.gms.ads.nativead.NativeAdOptions adOptions =
                    new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
            builder.withNativeAdOptions(adOptions);
            AdLoader adLoader = builder.withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.d(TAG, "onAdFailedToLoad: " + loadAdError);
                            onAdLoaded.OnAdLoadedCallBack(false);
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            onAdLoaded.OnAdLoadedCallBack(true);
                        }
                    })
                    .build();

            adLoader.loadAd(prepareAdRequest());

    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView, NativeAdType adType) {
        // Set the media view.
        adView.setMediaView(adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if(adView.getMediaView()!=null)
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            Log.d(TAG, "populateUnifiedNativeAdView: " + nativeAd.getBody());
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());

        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }



        if (adType != NativeAdType.SMALL_TYPE) {
            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }

            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

}