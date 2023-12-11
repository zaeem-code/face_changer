package com.apploop.face.changer.app.manager;

import android.os.Bundle;

import com.apploop.face.changer.app.app.App;
import com.apploop.face.changer.app.app.MyApplication;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsManager {
    private static AnalyticsManager manager;
    private final FirebaseAnalytics firebaseAnalytics;

    private AnalyticsManager() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(MyApplication.Companion.applicationContext());
    }

    public static AnalyticsManager getInstance() {
        if (manager == null) {
            manager = new AnalyticsManager();
        }
        return manager;
    }

    public void sendAnalytics(String actionDetail, String actionName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, (actionDetail));
        bundle.putString("action_view", actionName);
        firebaseAnalytics.logEvent(actionName, bundle);
    }

}