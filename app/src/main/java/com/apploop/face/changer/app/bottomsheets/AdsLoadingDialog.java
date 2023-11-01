package com.apploop.face.changer.app.bottomsheets;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.apploop.face.changer.app.R;

public class AdsLoadingDialog extends Dialog {
    InterstitialAd mInterstitialAd;
    public Activity c;

    public AdsLoadingDialog(Activity a, InterstitialAd mInterstitialAd) {
        super(a);
        this.c = a;
        this.mInterstitialAd = mInterstitialAd;
        showAds();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.adis_loading);
    }

    public void showAds() {
        show();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {

            if( c!=null && !c.isFinishing()) {
                dismiss();
                mInterstitialAd.show(c);

            }


        }, 500);
    }

}