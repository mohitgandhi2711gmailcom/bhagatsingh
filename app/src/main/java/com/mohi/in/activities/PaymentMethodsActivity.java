package com.mohi.in.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.CartModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuLightTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;
import com.mohi.in.widgets.UbuntuRegularButton;
import com.mohi.in.widgets.UbuntuRegularEditText;
import com.mohi.in.widgets.UbuntuRegularTextView;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class PaymentMethodsActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {


    private RadioButton rb_creditDebit, rb_COD;

    private UbuntuRegularEditText et_couponCode, et_giftCertificate;

    private UbuntuRegularButton but_apply, but_continue;

    private UbuntuRegularTextView tv_priceLable;

    private UbuntuMediumTextView tv_price, tv_deliveryPrice, tv_totlePrice;
    public static ArrayList<CartModel> cartList = new ArrayList<>();


    private String strPaymentId = "", strOrderId = "", strTxnId = "", strProductId = "", strProductPrice = "", strAddressId = "", strAddress = "", strAddresName = "", strQty = "0", strQuoteId = "", strType = "COD", strFrom = "", strProductName = "", strDeliveryPrice="0";
    private Intent intent;

    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;

    //PayUMoney
    private String merchantKey, userCredentials;

    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    // This sets the configuration
    private PayuConfig payuConfig;

    private Spinner environmentSpinner;

    // Used when generating hash from SDK
    private PayUChecksum checksum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);
        Payu.setInstance(this);
        intent = getIntent();


        if (intent.getStringExtra("ProductId") != null) {

            strProductId = intent.getStringExtra("ProductId");
            strProductPrice = intent.getStringExtra("ProductPrice");
            strAddressId = intent.getStringExtra("AddressId");
            strAddress = intent.getStringExtra("Address");
            strAddresName = intent.getStringExtra("AddressName");
            strQty = intent.getStringExtra("Qty");
            strFrom = intent.getStringExtra("From");
            strProductName = intent.getStringExtra("ProductName");
            strDeliveryPrice = intent.getStringExtra("DeliveryPrice");



            Log.e("sdfdsfdsf", "dfdfdfdf df: " + strProductName);
            if (intent.getStringExtra("Type") != null)
                strType = intent.getStringExtra("Type");

        }
        init();
    }

    private void init() {


        rb_creditDebit = (RadioButton) findViewById(R.id.PaymentMethod_CreditDebitCard);
        rb_COD = (RadioButton) findViewById(R.id.PaymentMethod_CashOnDelivery);


        et_couponCode = (UbuntuRegularEditText) findViewById(R.id.PaymentMethod_CouponCode);
        et_giftCertificate = (UbuntuRegularEditText) findViewById(R.id.PaymentMethod_GiftCertificate);

        tv_priceLable = (UbuntuRegularTextView) findViewById(R.id.PaymentMethod_PriceWithQty);

        tv_price = (UbuntuMediumTextView) findViewById(R.id.PaymentMethod_Price);
        tv_deliveryPrice = (UbuntuMediumTextView) findViewById(R.id.PaymentMethod_DeliveryCharge);
        tv_totlePrice = (UbuntuMediumTextView) findViewById(R.id.PaymentMethod_TotalPrice);

        but_apply = (UbuntuRegularButton) findViewById(R.id.PaymentMethod_Apply);
        but_continue = (UbuntuRegularButton) findViewById(R.id.PaymentMethod_Continue);


        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView) findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);
    }


    private void setValue() {


        Double itemPrice = Double.parseDouble(strProductPrice) - Integer.parseInt(strDeliveryPrice);
        tv_price.setText(Methods.getTwoDecimalVAlue("" + itemPrice) + " " + SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));

        String totlePrice = "" + (itemPrice + Integer.parseInt(strDeliveryPrice));
        tv_totlePrice.setText(Methods.getTwoDecimalVAlue("" + totlePrice) + " " + SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));

        tv_deliveryPrice.setText(strDeliveryPrice+" " + SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));

        tv_priceLable.setText(strQty);


        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.payments);


        iv_menu.setOnClickListener(this);
        but_continue.setOnClickListener(this);
        but_apply.setOnClickListener(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        setValue();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }


    // onclick

    @Override
    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()) {

            case R.id.Header_Menu:

                onBackPressed();

                break;

            case R.id.PaymentMethod_Apply:


                break;


            case R.id.PaymentMethod_Continue:
                if (rb_COD.isChecked()) {

                   /* intent = new Intent(PaymentMethodsActivity.this, HomeActivity.class);
                    startActivity(intent);

                    ActivityCompat.finishAffinity(this);*/

                    attemptPlaceOrder();

                } else {

                    String price = tv_totlePrice.getText().toString().trim().replaceAll(" " + SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE), "").trim();

                    //attemptToPlaceOrderUsingPayU(price, strProductName);
                    attemptPlaceOrder();


                }
                break;


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void attemptPlaceOrder() {
        try {

            WaitDialog.showDialog(this);

            int arrSize = cartList.size();
            JSONArray jarray = new JSONArray();

            for (int i = 0; i < arrSize; i++) {

                CartModel model = cartList.get(i);
                JSONObject jdata = new JSONObject();
                jdata.put("product_id", model.product_id);
                jdata.put("qty", model.qty);
                jarray.put(jdata);
                strQuoteId = model.quote_id;

            }

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("currency_id", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));
            jsonObj.put("quote_id", strQuoteId);
            jsonObj.put("user_id", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
            jsonObj.put("token", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));
            jsonObj.put("address_id", strAddressId);
            jsonObj.put("payment_method", "checkmo");
            jsonObj.put("items", jarray);

            JSONObject json = new JSONObject();
            json.put("order_data", jsonObj);

            ServerCalling.ServerCallingProductsApiPost(PaymentMethodsActivity.this, "placeOrder", (JsonObject) new JsonParser().parse(json.toString()), this);

        } catch (Exception e) {

        }
    }

    private void attemptAddPaymentTransaction(JSONObject payMentdata) {
        try {

//            {"user_id":"161","token":"XdT0NDzKS4o72el","order_id":"444","txn_id" : "hjjjhjhjhj5545d45d","payment_id" : "144","response_string" : "ddddd"}


            WaitDialog.showDialog(this);


            JSONObject json = new JSONObject();
            json.put("user_id", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
            json.put("token", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.put("order_id", strOrderId);
            json.put("txn_id", strTxnId);
            json.put("payment_id", strPaymentId);
            json.put("response_string", payMentdata);


            ServerCalling.ServerCallingProductsApiPost(PaymentMethodsActivity.this, "addPaymentTransaction", (JsonObject) new JsonParser().parse(json.toString()), this);

        } catch (Exception e) {

        }
    }


    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {

        try {

            WaitDialog.hideDialog();

            if (strfFrom.trim().equalsIgnoreCase("placeOrder")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    Log.e("sdsdsd", "dsdsdsd: " + result);

                    JSONObject jdata = result.getJSONObject("data");
                    strPaymentId = jdata.getString("payment_id");
                    strOrderId = jdata.getString("order_id");

                    if (rb_creditDebit.isChecked()) {

                        String price = tv_totlePrice.getText().toString().trim().replaceAll(" " + SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE), "").trim();

                        attemptToPlaceOrderUsingPayU(price, strProductName);

                    } else {
                        Methods.showToast(PaymentMethodsActivity.this, result.getString("msg"));

                        Intent intent = new Intent(PaymentMethodsActivity.this, HomeActivity.class);
                        startActivity(intent);

                        ActivityCompat.finishAffinity(this);
                    }
                } else {

                    Methods.showToast(PaymentMethodsActivity.this, result.getString("msg"));

                    Log.e("HomeFragment", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("getPaymentHash")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    setHashesFromServerTask(result.getJSONObject("data"));

                } else {

                    Methods.showToast(PaymentMethodsActivity.this, result.getString("msg"));

                    Log.e("HomeFragment", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addPaymentTransaction")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    Methods.showToast(PaymentMethodsActivity.this, result.getString("msg"));

                    Intent intent = new Intent(PaymentMethodsActivity.this, HomeActivity.class);
                    startActivity(intent);

                    ActivityCompat.finishAffinity(this);

                } else {

                    Methods.showToast(PaymentMethodsActivity.this, result.getString("msg"));

                    Log.e("HomeFragment", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            }

        } catch (Exception ee) {

            Log.e("Login", "Login User Exception: " + ee.getMessage());
        }

    }


    private void attemptToPlaceOrderUsingPayU(String amount, String prductInfo) {

        navigateToBaseActivity(amount, prductInfo);


    }


    public void navigateToBaseActivity(String amount, String prductInfo) {


        try {

            // WaitDialog.showDialog(this);
           /* $txnid = strip_tags(trim($parmete2['txnid']));
            $amount = strip_tags(trim($parmete2['amount']));
            $productinfo = strip_tags(trim($parmete2['productinfo']));
            $firstname = strip_tags(trim($parmete2['firstname']));
            $email = strip_tags(trim($parmete2['email']));
            $user_credentials = strip_tags(trim($parmete2['user_credentials']));
            $udf1 = strip_tags(trim($parmete2['udf1']));
            $udf2 = strip_tags(trim($parmete2['udf2']));
            $udf3 = strip_tags(trim($parmete2['udf3']));
            $udf4 = strip_tags(trim($parmete2['udf4']));
            $udf5 = strip_tags(trim($parmete2['udf5']));
            $offerKey = strip_tags(trim($parmete2['offerKey']));
            $cardBin = strip_tags(trim($parmete2['cardBin']));*/

            long txId = System.currentTimeMillis();
            strTxnId = "" + txId;

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("txnid", "" + txId);
            jsonObj.put("amount", amount);
            jsonObj.put("user_id", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
            jsonObj.put("token", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));
            jsonObj.put("productinfo", strProductName);
            jsonObj.put("firstname", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_NAME));
            jsonObj.put("email", SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_EMAIL));
            jsonObj.put("user_credentials", Common.KEY_PAYMENT_MERCHENT + ":" + SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
            jsonObj.put("udf1", "udf1");
            jsonObj.put("udf2", "udf2");
            jsonObj.put("udf3", "udf3");
            jsonObj.put("udf4", "udf4");
            jsonObj.put("udf5", "udf5");
            jsonObj.put("offerKey", "");
            jsonObj.put("cardBin", "");


            // ServerCalling.ServerCallingProductsApiPost(PaymentMethodsActivity.this, "getPaymentHash", (JsonObject) new JsonParser().parse(jsonObj.toString()), this);


            userCredentials = Common.KEY_PAYMENT_MERCHENT + ":" + SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_ID);

            //TODO Below are mandatory params for hash genetation
            mPaymentParams = new PaymentParams();
/*
*
         * For Test Environment, merchantKey = please contact mobile.integration@payu.in with your app name and registered email id
*/


            mPaymentParams.setKey(Common.KEY_PAYMENT_MERCHENT);
            mPaymentParams.setAmount(amount);
            mPaymentParams.setProductInfo(prductInfo);
            mPaymentParams.setFirstName(SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_NAME));
            mPaymentParams.setEmail(SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_EMAIL));
            mPaymentParams.setPhone(SessionStore.getUserDetails(PaymentMethodsActivity.this, Common.userPrefName).get(SessionStore.USER_MOBILENO));

      /*  * Transaction Id should be kept unique for each transaction.
        *
*/
            mPaymentParams.setTxnId("" + txId);

/**
 * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
 * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
 */

            mPaymentParams.setSurl("https://payu.herokuapp.com/success");
            mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
            mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay

       /*  * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * */

            mPaymentParams.setUdf1("udf1");
            mPaymentParams.setUdf2("udf2");
            mPaymentParams.setUdf3("udf3");
            mPaymentParams.setUdf4("udf4");
            mPaymentParams.setUdf5("udf5");


/**
 * These are used for store card feature. If you are not using it then user_credentials = "default"
 * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
 * here merchant_key = your merchant key,
 * user_id = unique id related to user like, email, phone number, etc.
 * */

            mPaymentParams.setUserCredentials(userCredentials);

            //TODO Pass this param only if using offer key
            //mPaymentParams.setOfferKey("cardnumber@8370");

            //TODO Sets the payment environment in PayuConfig object
            payuConfig = new PayuConfig();
            payuConfig.setEnvironment(Common.PAYMENT_ENVIRONMENT);
            //   payuConfig.setEnvironment(PayuConstants.MOBILE_STAGING_ENV);
            //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
            generateHashFromServer(mPaymentParams);

            // generateHashFromSDK(mPaymentParams,Common.KEY_PAYMENT_SALT);

/**
 * Below approach for generating hash is not recommended. However, this approach can be used to test in PRODUCTION_ENV
 * if your server side hash generation code is not completely setup. While going live this approach for hash generation
 * should not be used.
 * */

            //   String salt = "";
            //  generateHashFromSDK(mPaymentParams, salt);
        } catch (Exception e) {

        }

    }


    public void generateHashFromServer(PaymentParams mPaymentParams) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if (null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PaymentMethodsActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected PayuHashes doInBackground(String... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {

                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                Log.e("dsfdsfdsf", "ffffff: " + postParam);
                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());
                Log.e("dsfdsfdsf", "response: " + response);

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                        /**
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         *
                         */
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        /**
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         *
                         */
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        /**
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;

                        //TODO Below hashes only needs to be generated if you are using Store card feature
                        /**
                         * delete_user_card_hash is used while deleting a stored card.
                         * Below is formula for generating delete_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        /**
                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
                         * Below is formula for generating get_user_cards_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        /**
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        /**
                         * save_user_card_hash is used while saving card to the vault
                         * Below is formula for generating save_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;

                        //TODO This hash needs to be generated if you are using any offer key
                        /**
                         * check_offer_status_hash is used while using check_offer_status api
                         * Below is formula for generating check_offer_status_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be Offer Key.
                         *
                         */
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);

            progressDialog.dismiss();
            launchSdkUI(payuHashes);
        }
    }


    /**
     * This AsyncTask generates hash from server.
     */
    private void setHashesFromServerTask(JSONObject response) {


        PayuHashes payuHashes = new PayuHashes();
        try {

            //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
            URL url = new URL("https://payu.herokuapp.com/get_hash");

            // get the payuConfig first


            // JSONObject response = new JSONObject(responseStringBuffer.toString());
            Log.e("PayuHashes", "PayuHashes:  " + response);

            Iterator<String> payuHashIterator = response.keys();
            while (payuHashIterator.hasNext()) {
                String key = payuHashIterator.next();
                switch (key) {
                    //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                      /*  *
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         **/

                    case "payment_hash":
                        payuHashes.setPaymentHash(response.getString(key));

                        Log.e("ffdg", "payment_hash" + response.getString(key));
                      /*      break;
                        *
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         **/

                    case "vas_for_mobile_sdk_hash":
                        payuHashes.setVasForMobileSdkHash(response.getString(key));
                        Log.e("ffdg", "vas_for_mobile_sdk_hash" + response.getString(key));
                        break;
                      /*  *
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         **/

                    case "payment_related_details_for_mobile_sdk_hash":
                        payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                        Log.e("ffdg", "payment_related_details_for_mobile_sdk_hash: " + response.getString(key));
                        break;

                    //TODO Below hashes only needs to be generated if you are using Store card feature
                       /* *
                         * delete_user_card_hash is used while deleting a stored card.
                         * Below is formula for generating delete_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         **/

                    case "delete_user_card_hash":
                        payuHashes.setDeleteCardHash(response.getString(key));
                        Log.e("ffdg", "delete_user_card_hash: " + response.getString(key));
                        break;
                       /* *
                         * get_user_cards_hash is used while fetching all the cards corresponding to a user.
                         * Below is formula for generating get_user_cards_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */
                    case "get_user_cards_hash":
                        payuHashes.setStoredCardsHash(response.getString(key));
                        Log.e("ffdg", "get_user_cards_hash: " + response.getString(key));
                        break;
                      /*  *
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         **/

                    case "edit_user_card_hash":
                        payuHashes.setEditCardHash(response.getString(key));
                        Log.e("ffdg", "edit_user_card_hash: " + response.getString(key));
                        break;
                      /*  *
                         * save_user_card_hash is used while saving card to the vault
                         * Below is formula for generating save_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         **/

                    case "save_user_card_hash":
                        payuHashes.setSaveCardHash(response.getString(key));
                        Log.e("ffdg", "save_user_card_hash: " + response.getString(key));
                        break;

                    //TODO This hash needs to be generated if you are using any offer key
                      /*  *
                         * check_offer_status_hash is used while using check_offer_status api
                         * Below is formula for generating check_offer_status_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be Offer Key.
                         **/

                    case "check_offer_status_hash":
                        payuHashes.setCheckOfferStatusHash(response.getString(key));
                        Log.e("ffdg", "check_offer_status_hash: " + response.getString(key));
                        break;
                    default:
                        break;
                }
            }
            launchSdkUI(payuHashes);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * This method adds the Payuhashes and other required params to intent and launches the PayuBaseActivity.java
     *
     * @param payuHashes it contains all the hashes generated from merchant server
     */
    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);

        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);

        //Lets fetch all the one click card tokens first
        //fetchMerchantHashes(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {

                try {


                    /**
                     * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                     * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                     *
                     * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                     * for identifying status of transaction. There are two possible status like, success or failure
                     * */

                    Log.e("onActivityResult", "Payu's Data ::: " + data.getStringExtra("payu_response"));
                    Log.e("onActivityResult", "Merchant's Data ::: " + data.getStringExtra("result"));

                    attemptAddPaymentTransaction(new JSONObject(data.getStringExtra("payu_response")));

              /*  new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();*/
                } catch (Exception ex) {

                    Log.e("Exception", "onActivityResult Payment: " + ex.getMessage());
                }

            } else {
                Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }
    }


}
