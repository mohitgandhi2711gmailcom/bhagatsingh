package com.mohi;

import android.app.Application;

import com.mohi.in.widgets.FontsOverride;


/**
 * Created by pankaj on 11/20/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//set Custom Typeface

        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/Ubuntu-R.ttf");

    }
}
