package com.mohi.in.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pankaj on 10/23/17.
 */

public class Methods {

    public static String getVersionCode(Context mContext) {
        String versionCode = "";
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionCode = ""+pInfo.versionCode;

            Log.i("ersion Code", "Version Code: " + versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    public static String getDeviceId(Context mContext){

        String android_id = Settings.Secure.getString(mContext.getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.i("ersion Code", "android_id Code: " + android_id);

        return android_id;
    }

    public static void showToast(Context mContext, String message) {


        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

    }

    public static String getTwoDecimalVAlue(String value) {


        return String.format("%.2f", Double.parseDouble(value));

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    // cheking mobile no is valid or not
    public static boolean isValidMobile(String phone) {
        if (phone.length() < 10) {

            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }


    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        String strContry = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                strContry = addresses.get(0).getCountryName();
                return strContry;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strContry;

    }


    public static String capitalizeWord(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString.trim());
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        int count = 0;
        if (password.matches(".*[A-Za-z0-9 ].*")){
            count+=1;
        }
        if (password.matches(".*[A-Z].*")){
            count+=1;
        }
        if (password.matches(".*\\d.*")){
            count+=1;
        }
        if (password.matches(".*[a-z].*")){
            count+=1;
        }
        return count >2;
    }

    public static boolean isPhoneNoValid(String phoneNo) {
        //TODO: Replace this with your own logic
        return (phoneNo.length() >= 10);
    }

    public static String twoDigitFormat(String value) {

        DecimalFormat formatter = new DecimalFormat("00");
        return formatter.format(Integer.valueOf(value));
    }




}
