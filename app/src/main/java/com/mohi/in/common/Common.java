package com.mohi.in.common;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.payu.india.Payu.PayuConstants;

public class Common {

    public static final String USER_PREFS_NAME = "userInfo";
    public static final String USER_QUOTE_ID = "quote_id";
    public static final String USER_PRODUCT_ID = "product_id";
    public static final String API_STATUS = "status";

    public static final String KEY_PAYMENT_MERCHENT = "gtKFFx";
    public static final String KEY_PAYMENT_SALT = "ShzwoBLFVb";
    public static final String KEY_PAYMENT_MID = "5970481";

    public static final boolean isLive = false;
    public static int KEY_PAYMENT_TEST = PayuConstants.STAGING_ENV;
    public static int KEY_PAYMENT_PRODUCTION = PayuConstants.PRODUCTION_ENV;

    public static final int PAYMENT_ENVIRONMENT = isLive ? KEY_PAYMENT_PRODUCTION:KEY_PAYMENT_TEST;

    public static String NEEDHELP = "http://www.mohi.ae/help.html";




    public static DisplayMetrics getDeviceHeightWidth(Context context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Log.i("getDeviceHeightWidth", "Height: " + displayMetrics.heightPixels + "  Width: " + displayMetrics.widthPixels);
        Log.i("getDeviceHeightWidth", "Height: " + displayMetrics.densityDpi + "  Width: " + displayMetrics.widthPixels);

        return displayMetrics;
    }

}
