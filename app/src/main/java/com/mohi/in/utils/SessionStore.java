package com.mohi.in.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionStore {

    // User Info
    public static String USER_ID = "user_id";
    public static String USER_TOKEN = "token";
    public static String USER_EMAIL = "email";
    public static String USER_FIRST_NAME = "userFirstName";
    public static String USER_LAST_NAME = "userLastName";
    public static String USER_MOBILENO = "userMobileNo";
    public static String USER_CURRENCYTYPE = "userCurrencyType";
    public static String PROFILEPICTURE = "profilePicture";
    public static String COUNTRY_CODE = "country_code";
    public static String USER_ADDRESS = "userAddress";
    public static String USER_ADDRESSNAME = "userAddressName";
    public static String USER_ADDRESSID = "userAddressID";


    public static boolean save(Context context, String name, String user_id, String token,
                               String email, String userMobileNo, String firstName,String lastName, String profilePicture,
                               String userAddressID, String userAddressName, String userAddress,
                               String userCurrencyType,String country_code) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(USER_ID, user_id);
        editor.putString(USER_TOKEN, token);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_FIRST_NAME, firstName);
        editor.putString(USER_LAST_NAME, lastName);
        editor.putString(PROFILEPICTURE, profilePicture);
        editor.putString(USER_ADDRESS, userAddress);
        editor.putString(USER_ADDRESSNAME, userAddressName);
        editor.putString(USER_ADDRESSID, userAddressID);
        editor.putString(USER_MOBILENO, userMobileNo);
        editor.putString(USER_CURRENCYTYPE, userCurrencyType);
        editor.putString(COUNTRY_CODE,country_code);
        return editor.commit();
    }


    public static HashMap<String, String> getUserDetails(Context context, String name) {
        SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, pref.getString(USER_ID, null));
        user.put(USER_TOKEN, pref.getString(USER_TOKEN, null));
        user.put(USER_EMAIL, pref.getString(USER_EMAIL, null));
        user.put(USER_FIRST_NAME, pref.getString(USER_FIRST_NAME, null));
        user.put(USER_LAST_NAME, pref.getString(USER_LAST_NAME, null));
        user.put(PROFILEPICTURE, pref.getString(PROFILEPICTURE, null));
        user.put(USER_ADDRESS, pref.getString(USER_ADDRESS, null));
        user.put(USER_ADDRESSNAME, pref.getString(USER_ADDRESSNAME, null));
        user.put(USER_ADDRESSID, pref.getString(USER_ADDRESSID, null));
        user.put(USER_MOBILENO, pref.getString(USER_MOBILENO, null));
        user.put(USER_CURRENCYTYPE, pref.getString(USER_CURRENCYTYPE, ""));
        user.put(COUNTRY_CODE,pref.getString(COUNTRY_CODE,""));
        return user;
    }

    public static void saveCurrency(Context context, String name, String userCurrencyType) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(USER_CURRENCYTYPE, userCurrencyType);
        editor.apply();
    }

    public static void clear(Context context, String name) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
