package com.example.administrator.smartclient;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

public class App extends Application {

    private static App instance;
    private SharedPreferences.Editor edit;
    private SharedPreferences data;

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        data = getSharedPreferences("data", MODE_PRIVATE);
        edit = data.edit();
    }

    public static App getInstance() {
        return instance;
    }

    public void removeAll(String key) {
        edit.remove(key).apply();
    }
    public void putString(String key, String value) {
        edit.putString(key, value).apply();
    }

    public String getString(String key) {
        return data.getString(key, "");
    }
}
