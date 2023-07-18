package com.frami.utils.helper;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsLogger {

    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_SPLASH = "splash";

    private FirebaseAnalytics mFirebaseAnalytics;

    public AnalyticsLogger(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    /**
     * Assign user id to analytics
     *
     * @param userId
     */
    public void setUserId(String userId) {
        mFirebaseAnalytics.setUserId(String.valueOf(userId));
    }

    /**
     * Assign user property
     *
     * @param key
     * @param value
     */
    public void setUserProperty(String key, String value) {
        mFirebaseAnalytics.setUserProperty(key, value);
    }

    /**
     * Log screen name
     *
     * @param screenName
     * @param activity
     */
    public void logScreenName(String screenName, Activity activity) {
        mFirebaseAnalytics.setCurrentScreen(activity, screenName, null);
    }
}