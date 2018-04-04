package com.mohi.in.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;

import org.json.JSONObject;

import java.io.File;

public class ServerCalling {

    public static void ServerCallingUserApiPost(final Context mContext, final String urlParameter, JsonObject jsonObj, final ServerCallBack callBack) {
        // WaitDialog.showDialog(mContext);
        Log.e("Url", "Url: " + Urls.USERBASEURL + urlParameter);
        Log.e("Parameter", "Parameter: " + jsonObj);
        Ion.with(mContext)
                .load(Urls.USERBASEURL + urlParameter)
                .setHeader(Urls.HEADERSRT, Methods.getVersionCode(mContext))
                .setHeader(Urls.DEVICEID, Methods.getDeviceId(mContext))
                .setTimeout(60 * 60 * 1000)
                .setJsonObjectBody(jsonObj)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            if (e != null) {
                                WaitDialog.hideDialog();
                                Log.e("ServerCallingUserApi", "ServerCalling error log: " + urlParameter + "   " + e.getMessage() + " :: " + result);
                                Methods.showToast(mContext, "Error");
                            } else {
                                Log.e("ServerCallingUserApi", "ServerCalling log: " + urlParameter + "   " + result);
                                JSONObject json = new JSONObject(result);
                                JSONObject jsonData = new JSONObject(json.toString());
                                Log.e("ServerCallingUserApi", urlParameter + "   " + jsonData);
                                if (jsonData.getString("status").equalsIgnoreCase("0")) {
                                    WaitDialog.hideDialog();
                                    Methods.showToast(mContext, jsonData.getString("msg"));
                                    Log.e("ServerCallingUserApi ", urlParameter + "   " + jsonData.getString("msg"));
                                    if (jsonData.get("error") != null) {
                                        if (jsonData.getString("error").equalsIgnoreCase("461")) {
                                            SessionStore.clear(mContext, Common.userPrefName);
                                        } else if (jsonData.getString("error").equalsIgnoreCase("462")) {
                                            new AlertDialog.Builder(mContext)
                                                    .setTitle((mContext.getResources()).getString(R.string.update_text))
                                                    .setMessage((mContext.getResources()).getString(R.string.update_app_message))
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            String url = "https://play.google.com/store/apps/details?id=com.mohi.in&hl=en";
                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.setData(Uri.parse(url));
                                                            mContext.startActivity(intent);
                                                        }
                                                    }).show();
                                        }
                                    }
                                } else {
                                    callBack.ServerCallBackSuccess(json, urlParameter);
                                }
                            }
                        } catch (Exception exc) {
                            Log.e("ServerCallingUserApi", "ServerCalling Exception log : " + urlParameter + "   " + exc.getMessage());
                        }
                    }
                });
    }

    public static void ServerCallingProductsApiPost(final Context mContext, final String urlParameter, JsonObject jsonObj, final ServerCallBack callBack) {

        // WaitDialog.showDialog(mContext);
        Log.e("Url", "Url: " + Urls.PRODUCTBASEURL + urlParameter);
        Log.e("Parameter", "Parameter: " + jsonObj);


        Ion.with(mContext)
                .load(Urls.PRODUCTBASEURL + urlParameter)
                .setHeader(Urls.HEADERSRT, Methods.getVersionCode(mContext))
                .setHeader(Urls.DEVICEID, Methods.getDeviceId(mContext))
                .setTimeout(60 * 60 * 1000)
                .setJsonObjectBody(jsonObj)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {
                            if (e != null) {

                                Log.e("ServerCallingProductsAp", "ServerCalling error log: " + urlParameter + "   " + e.getMessage());
                                Methods.showToast(mContext, "Error");
                                WaitDialog.hideDialog();
                            } else {


                                Log.e("ServerCallingProductsAp", "ServerCalling log: " + urlParameter + "   " + result);
                                JSONObject json = new JSONObject(result.toString());
                                JSONObject jsonData = new JSONObject(json.toString());

                                Log.e("ServerCallingUserApi", " TEST: " + urlParameter + "   " + jsonData);

                                if (jsonData.getString("status").equalsIgnoreCase("0")) {
                                    WaitDialog.hideDialog();
                                    Methods.showToast(mContext, jsonData.getString("msg"));
                                    callBack.ServerCallBackSuccess(json, urlParameter);
                                    Log.e("ServerCallingUs333", urlParameter + "   " + jsonData.getString("msg"));

                                    if (jsonData.get("error") != null) {

                                        if (jsonData.getString("error").equalsIgnoreCase("461")) {
                                          /*  Intent intent = new Intent(mContext, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            mContext.startActivity(intent);
                                            ((Activity) mContext).finish();*/
                                            SessionStore.clear(mContext, Common.userPrefName);
                                        } else if (jsonData.getString("error").equalsIgnoreCase("462")) {


                                            new AlertDialog.Builder(mContext)
                                                    .setTitle((mContext.getResources()).getString(R.string.update_text))
                                                    .setMessage((mContext.getResources()).getString(R.string.update_app_message))
                                                    .setCancelable(false)
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                            String url = "https://play.google.com/store/apps/details?id=com.mohi.in&hl=en";
                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.setData(Uri.parse(url));
                                                            mContext.startActivity(intent);
                                                        }
                                                    }).show();

                                        }
                                    }

                                } else {
                                    callBack.ServerCallBackSuccess(json, urlParameter);

                                }

                            }
                        } catch (Exception exc) {
                            Log.e("ServerCallingProductsAp", "ServerCalling Exception log : " + urlParameter + "   " + exc.getMessage());

                        }


                    }
                });


    }

    public static void ServerCallingProductsApiPost(final Context mContext, final String urlParameter) {
        Ion.with(mContext)
                .load("https://apnasweets.com/app_api/collections.php")
                .setHeader(Urls.HEADERSRT, Methods.getVersionCode(mContext))
                .setHeader(Urls.DEVICEID, Methods.getDeviceId(mContext))
                .setTimeout(60 * 60 * 1000)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            Log.e("ServerCallingProductsAp", "Test 123 : " + e + "   " + result);
                        } catch (Exception exc) {
                            Log.e("ServerCallingProductsAp", "ServerCalling Exception log : " + urlParameter + "   " + exc.getMessage());
                        }
                    }
                });
    }

    public static void ServerCallingUserApiImagePost(final Context mContext, final String urlParameter, final ServerCallBack callBack,
                                                     String userId, String userToken, String userName, String mobileNo, File file) {

        // WaitDialog.showDialog(mContext);
        Log.e("Url", "Url: " + Urls.USERBASEURL + urlParameter);

        if (file == null) {
            Ion.with(mContext)
                    .load(Urls.USERBASEURL + urlParameter)
                    .setHeader(Urls.HEADERSRT, Methods.getVersionCode(mContext))
                    .setHeader(Urls.DEVICEID, Methods.getDeviceId(mContext))
                    .setTimeout(60 * 60 * 1000)
                    .setMultipartParameter("user_id", userId)
                    .setMultipartParameter("token", userToken)
                    .setMultipartParameter("name", userName)
                    .setMultipartParameter("mobile_number", mobileNo)

                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error

                            // WaitDialog.hideDialog();
                            try {
                                if (e != null) {
                                    WaitDialog.hideDialog();
                                    Log.e("ServerCallingUserApi", "ServerCalling error log: " + urlParameter + "   " + e.getMessage() + " :: " + result);
                                    Methods.showToast(mContext, "Error");
                                } else {


                                    Log.e("ServerCallingUserApi", "ServerCalling log: " + urlParameter + "   " + result);
                                    JSONObject json = new JSONObject(result.toString());

                                    JSONObject jsonData = new JSONObject(json.toString());

                                    Log.e("ServerCallingUserApi", urlParameter + "   " + jsonData);

                                    if (jsonData.getString("status").equalsIgnoreCase("0")) {
                                        WaitDialog.hideDialog();
                                        Methods.showToast(mContext, jsonData.getString("msg"));

                                        Log.e("ServerCallingUserApi ", urlParameter + "   " + jsonData.getString("msg"));

                                        if (jsonData.get("error") != null) {

                                            if (jsonData.getString("error").equalsIgnoreCase("461")) {
                                            /*Intent intent = new Intent(mContext, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            mContext.startActivity(intent);
                                            ((Activity) mContext).finish();*/
                                                SessionStore.clear(mContext, Common.userPrefName);
                                            } else if (jsonData.getString("error").equalsIgnoreCase("462")) {


                                                new AlertDialog.Builder(mContext)
                                                        .setTitle((mContext.getResources()).getString(R.string.update_text))
                                                        .setMessage((mContext.getResources()).getString(R.string.update_app_message))
                                                        .setCancelable(false)
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                String url = "https://play.google.com/store/apps/details?id=com.mohi.in&hl=en";
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                intent.setData(Uri.parse(url));
                                                                mContext.startActivity(intent);
                                                            }
                                                        }).show();

                                            }
                                        }

                                    } else {
                                        callBack.ServerCallBackSuccess(json, urlParameter);

                                    }


                                }
                            } catch (Exception exc) {
                                Log.e("ServerCallingUserApi", "ServerCalling Exception log : " + urlParameter + "   " + exc.getMessage());

                            }


                        }
                    });

        } else {

            Ion.with(mContext)
                    .load(Urls.USERBASEURL + urlParameter)
                    .setHeader(Urls.HEADERSRT, Methods.getVersionCode(mContext))
                    .setHeader(Urls.DEVICEID, Methods.getDeviceId(mContext))
                    .setTimeout(60 * 60 * 1000)
                    .setMultipartParameter("user_id", userId)
                    .setMultipartParameter("token", userToken)
                    .setMultipartParameter("name", userName)
                    .setMultipartParameter("mobile_number", mobileNo)
                    .setMultipartFile("user_profile", file)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error

                            // WaitDialog.hideDialog();
                            try {
                                if (e != null) {
                                    WaitDialog.hideDialog();
                                    Log.e("ServerCallingUserApi", "ServerCalling error log: " + urlParameter + "   " + e.getMessage() + " :: " + result);
                                    Methods.showToast(mContext, "Error");
                                } else {


                                    Log.e("ServerCallingUserApi", "ServerCalling log: " + urlParameter + "   " + result);
                                    JSONObject json = new JSONObject(result.toString());

                                    JSONObject jsonData = new JSONObject(json.toString());

                                    Log.e("ServerCallingUserApi", urlParameter + "   " + jsonData);

                                    if (jsonData.getString("status").equalsIgnoreCase("0")) {
                                        WaitDialog.hideDialog();
                                        Methods.showToast(mContext, jsonData.getString("msg"));

                                        Log.e("ServerCallingUserApi ", urlParameter + "   " + jsonData.getString("msg"));

                                        if (jsonData.get("error") != null) {

                                            if (jsonData.getString("error").equalsIgnoreCase("461")) {
                                            /*Intent intent = new Intent(mContext, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            mContext.startActivity(intent);
                                            ((Activity) mContext).finish();*/
                                                SessionStore.clear(mContext, Common.userPrefName);
                                            } else if (jsonData.getString("error").equalsIgnoreCase("462")) {


                                                new AlertDialog.Builder(mContext)
                                                        .setTitle((mContext.getResources()).getString(R.string.update_text))
                                                        .setCancelable(false)
                                                        .setMessage((mContext.getResources()).getString(R.string.update_app_message))
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                String url = "https://play.google.com/store/apps/details?id=com.mohi.in&hl=en";
                                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                                intent.setData(Uri.parse(url));
                                                                mContext.startActivity(intent);
                                                            }
                                                        }).show();

                                            }
                                        }

                                    } else {
                                        callBack.ServerCallBackSuccess(json, urlParameter);

                                    }


                                }
                            } catch (Exception exc) {
                                Log.e("ServerCallingUserApi", "ServerCalling Exception log : " + urlParameter + "   " + exc.getMessage());

                            }


                        }
                    });
        }


    }
}
