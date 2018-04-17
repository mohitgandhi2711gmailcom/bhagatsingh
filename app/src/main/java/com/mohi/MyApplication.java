package com.mohi;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.mohi.in.widgets.FontsOverride;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //set Custom Typeface
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/Ubuntu-R.ttf");
    }
}
