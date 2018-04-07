package com.mohi.in.common;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.payu.india.Payu.PayuConstants;

/**
 * Created by pankaj on 9/20/17.
 */

public class Common {

    public static String userPrefName = "userInfo";
    public static String userPrefAddress = "userAddress";
    public static String currencyPrefName = "currency";
    public static String KEY_APP_VERSION = "version";
    public static String CART_COUNT = "00";
    public static String CART_CURRENCY = "INR";


   // public static String KEY_PAYMENT_MERCHENT = "w7yOV1c1";
    public static String KEY_PAYMENT_MERCHENT = "gtKFFx";
    public static String KEY_PAYMENT_SALT = "ShzwoBLFVb";
    public static String KEY_PAYMENT_MID = "5970481";

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
