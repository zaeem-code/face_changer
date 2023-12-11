package com.apploop.face.changer.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.apploop.face.changer.app.app.App;
import com.apploop.face.changer.app.app.MyApplication;


public class SharedPrefHelper {

    private static final int MODE = Context.MODE_PRIVATE;
    private static SharedPreferences preferences;

    private static SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = MyApplication.Companion.applicationContext().getSharedPreferences("private-prefs", MODE);
        }
        return preferences;
    }

    private static SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    public static void writeInteger(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public static int readInteger(String key) {
        return getPreferences().getInt(key, 0);
    }

    public static void writeString(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static String readString(String key) {
        return getPreferences().getString(key, "");
    }

    public static void writeBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    public static boolean readBoolean(String key) {
        return getPreferences().getBoolean(key, false);
    }

    public static void clearAll() {
        getEditor().clear().commit();
    }
}
