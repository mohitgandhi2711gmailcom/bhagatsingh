package com.mohi.in.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionStore {

    // User Info
    public static final String USER_ID = "user_id";
    public static final String USER_TOKEN = "token";
    public static final String USER_EMAIL = "email";
    public static final String USER_FIRST_NAME = "userFirstName";
    public static final String USER_LAST_NAME = "userLastName";
    public static final String PROFILEPICTURE = "profilePicture";
    public static final String USER_MOBILENO = "userMobileNo";
    public static final String USER_CURRENCYTYPE = "userCurrencyType";
    public static final String COUNTRY_CODE = "country_code";
    public static final String USER_ADDRESS_ID = "address_id";
    public static final String USER_TELEPHONE_NO = "telephone";
    public static final String USER_STREET_ONE = "street_1";
    public static final String USER_STREET_TWO = "street_2";
    public static final String USER_CITY = "city";
    public static final String USER_REGION = "region";
    public static final String USER_POSTCODE = "postcode";
    public static final String USER_COUNTRY_ID = "country_id";
    public static final String USER_DEFAULT_SHIPPING = "default_shipping";
    public static final String USER_DEFAULT_BILLING= "default_billing";
    public static final String USER_CART_COUNT= "total_product";



    public static void saveUserDetails(Context context, String name, String user_id, String token,
                                          String email, String userMobileNo, String firstName, String lastName, String profilePicture,
                                          String userCurrencyType, String country_code) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(USER_ID, user_id);
        editor.putString(USER_TOKEN, token);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_FIRST_NAME, firstName);
        editor.putString(USER_LAST_NAME, lastName);
        editor.putString(PROFILEPICTURE, profilePicture);
        editor.putString(USER_MOBILENO, userMobileNo);
        editor.putString(USER_CURRENCYTYPE, userCurrencyType);
        editor.putString(COUNTRY_CODE, country_code);
        editor.apply();
    }

    public static void saveUserAddress(Context context, String name, String address_id, String telephone,
                                          String street_1, String street_2, String city, String region, String postcode, String country_id,
                                          Boolean default_shipping, Boolean default_billing) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(USER_ADDRESS_ID, address_id);
        editor.putString(USER_TELEPHONE_NO, telephone);
        editor.putString(USER_STREET_ONE, street_1);
        editor.putString(USER_STREET_TWO, street_2);
        editor.putString(USER_CITY, city);
        editor.putString(USER_REGION, region);
        editor.putString(USER_POSTCODE, postcode);
        editor.putString(USER_COUNTRY_ID, country_id);
        editor.putBoolean(USER_DEFAULT_SHIPPING,default_shipping);
        editor.putBoolean(USER_DEFAULT_BILLING,default_billing);
        editor.apply();
    }

    public static HashMap<String, String> getUserDetails(Context context, String name) {
        SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, pref.getString(USER_ID, ""));
        user.put(USER_TOKEN, pref.getString(USER_TOKEN, ""));
        user.put(USER_EMAIL, pref.getString(USER_EMAIL, ""));
        user.put(USER_FIRST_NAME, pref.getString(USER_FIRST_NAME, ""));
        user.put(USER_LAST_NAME, pref.getString(USER_LAST_NAME, ""));
        user.put(PROFILEPICTURE, pref.getString(PROFILEPICTURE, ""));
        user.put(USER_MOBILENO, pref.getString(USER_MOBILENO, ""));
        user.put(USER_CURRENCYTYPE, pref.getString(USER_CURRENCYTYPE, ""));
        user.put(COUNTRY_CODE, pref.getString(COUNTRY_CODE, ""));
        user.put(USER_ADDRESS_ID, pref.getString(USER_ADDRESS_ID, ""));
        user.put(USER_TELEPHONE_NO, pref.getString(USER_TELEPHONE_NO, ""));
        user.put(USER_STREET_ONE, pref.getString(USER_STREET_ONE, ""));
        user.put(USER_STREET_TWO, pref.getString(USER_STREET_TWO, ""));
        user.put(USER_CITY, pref.getString(USER_CITY, ""));
        user.put(USER_REGION, pref.getString(USER_REGION, ""));
        user.put(USER_POSTCODE, pref.getString(USER_POSTCODE, ""));
        user.put(USER_COUNTRY_ID, pref.getString(USER_COUNTRY_ID, ""));
        return user;
    }

    public static void saveCartCount(Context context, String name, String count) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(USER_CART_COUNT, count);
        editor.apply();
    }

    public static String getCartCount(Context context, String name) {
        SharedPreferences pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return pref.getString(USER_CART_COUNT, "");
    }

    public static void clear(Context context, String name) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
