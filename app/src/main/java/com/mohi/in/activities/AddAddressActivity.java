package com.mohi.in.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.JsonObject;

import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuLightTextView;
import com.mohi.in.widgets.UbuntuRegularButton;
import com.mohi.in.widgets.UbuntuRegularEditText;

import org.json.JSONObject;

import java.util.HashMap;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {


    private UbuntuRegularEditText et_name, et_phoneNo, et_pinCode, et_flat, et_colony, et_landmark, et_city, et_state, et_contryName;
    private UbuntuRegularButton but_submit;
    private Intent intent;
    private String  strProductId="", strFromScreen="";


    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search;
    private MaterialRippleLayout mrl_filter;
    private UbuntuLightTextView tv_headerTilel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        intent  = getIntent();


        if (intent.getStringExtra("ProductId")!=null){

            strProductId = intent.getStringExtra("ProductId");
            strFromScreen = intent.getStringExtra("From");

            Log.e("dsfdsfds", "dsfdsfdsf: "+strFromScreen);

        }


        init();
    }

    private void init(){

        et_name = (UbuntuRegularEditText)findViewById(R.id.AddAddress_Name);
        et_phoneNo = (UbuntuRegularEditText)findViewById(R.id.AddAddress_PhoneNo);
        et_pinCode = (UbuntuRegularEditText)findViewById(R.id.AddAddress_Pincode);
        et_flat = (UbuntuRegularEditText)findViewById(R.id.AddAddress_FlatNo);
        et_colony = (UbuntuRegularEditText)findViewById(R.id.AddAddress_Colony);
        et_landmark = (UbuntuRegularEditText)findViewById(R.id.AddAddress_Landmark);
        et_city = (UbuntuRegularEditText)findViewById(R.id.AddAddress_City);
        et_state = (UbuntuRegularEditText)findViewById(R.id.AddAddress_State);
        et_contryName = (UbuntuRegularEditText)findViewById(R.id.AddAddress_CountryName);

        but_submit = (UbuntuRegularButton) findViewById(R.id.AddAddress_Submit);


        //Header
        MaterialRippleLayout mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout)findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout)findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView)findViewById(R.id.Header_Menu);
        iv_search = (ImageView)findViewById(R.id.Header_Search);
        iv_filter = (ImageView)findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView)findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);

    }


    private void setValue(){



        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.add_new_address);

        iv_menu.setOnClickListener(this);
        but_submit.setOnClickListener(this);





    }

    @Override
    protected void onResume() {
        super.onResume();
        setValue();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= null;

        if(strFromScreen.trim().equalsIgnoreCase("Detail")){

            intent = new Intent(AddAddressActivity.this, ProductDetailActivity.class);
            intent.putExtra("ProductId",strProductId);
            startActivity(intent);

        }


        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.Header_Menu:

                onBackPressed();
                break;

            case R.id.AddAddress_Submit:
                attemptSubmitAddress();

                break;


        }

    }


    private void attemptSubmitAddress(){

        // Reset errors.
        et_name.setError(null);
        et_phoneNo.setError(null);
        et_pinCode.setError(null);
        et_flat.setError(null);
        et_colony.setError(null);
        et_city.setError(null);
        et_state.setError(null);
        et_contryName.setError(null);

        // Store values at the time of the login attempt.
        String name = et_name.getText().toString();
        String phoneNo = et_phoneNo.getText().toString();
        String pinCode = et_pinCode.getText().toString();
        String flat = et_flat.getText().toString();
        String colony = et_colony.getText().toString();
        String city = et_city.getText().toString();
        String state = et_state.getText().toString();
        String contry = et_contryName.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(contry.trim())) {
            et_contryName.setError("Please enter contry");
            focusView = et_contryName;
            cancel = true;
        }

        if (TextUtils.isEmpty(state.trim())) {
            et_state.setError("Please enter state");
            focusView = et_state;
            cancel = true;
        }

        if (TextUtils.isEmpty(city.trim())) {
            et_city.setError("Please enter city");
            focusView = et_city;
            cancel = true;
        }

        if (TextUtils.isEmpty(colony.trim())) {
            et_colony.setError("Please enter Colony / Street / Locality");
            focusView = et_colony;
            cancel = true;
        }

        if (TextUtils.isEmpty(flat.trim())) {
            et_flat.setError("Please enter Flat / House No. / Floor / Building");
            focusView = et_flat;
            cancel = true;
        }


        if (TextUtils.isEmpty(pinCode.trim())) {
            et_pinCode.setError("Please enter pincode");
            focusView = et_pinCode;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneNo.trim())) {
            et_phoneNo.setError("Please enter mobile no.");
            focusView = et_phoneNo;
            cancel = true;
        }else if(!Methods.isValidMobile(phoneNo.trim())){

            et_phoneNo.setError("Please enter valid mobile no.");
            focusView = et_phoneNo;
            cancel = true;
        }

        if (TextUtils.isEmpty(name.trim())) {
            et_name.setError("Please enter your name");
            focusView = et_name;
            cancel = true;
        }


        Log.e("dsfdsfdsf","Valid phone no: "+Methods.isValidMobile(phoneNo));


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else {
            try {


                WaitDialog.showDialog(this);





//                strAddress = strAddress+", "+contry+", "+city+",  "+state+", "+pinCode;


                JsonObject json = new JsonObject();
                json.addProperty("user_id", SessionStore.getUserDetails(AddAddressActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(AddAddressActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));
                json.addProperty("name", name);
                json.addProperty("country", contry);
                json.addProperty("city", city);
                json.addProperty("postcode", pinCode);
                json.addProperty("mobile", phoneNo);
                json.addProperty("state", state);
                json.addProperty("street",  colony);
                json.addProperty("flat_no",  flat);
                json.addProperty("landmark",  et_landmark.getText().toString());



                /*{
                    "user_id" : "40",
                        "token" : "x3K9W3ICWrqZxRT",
                        "name" : "hemant sharma",
                        "country" : "IN",
                        "city" : "Indore",
                        "postcode" : "452009",
                        "mobile" : "123456789",
                        "state" : "Madhya Pradesh",
                        "street" : "lion street",
                        "flat_no" : "123",
                        "landmark" : "near HDFC Bank"
                }
*/

                ServerCalling.ServerCallingUserApiPost(AddAddressActivity.this, "addShippingAddress", json, this);


            } catch (Exception e) {


                Log.e("AddAddressActivity", "Exception attemptTOGetUserInfo: " + e.getMessage());
            }
        }

    }

    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {

            WaitDialog.hideDialog();
            if(strfFrom.trim().equalsIgnoreCase("addShippingAddress")) {

                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(AddAddressActivity.this, result.getString("msg"));

                    JSONObject jData = result.getJSONObject("data");
                    if (!strProductId.trim().equalsIgnoreCase("")) {

                        String str = jData.getString("flat_no")+", "+jData.getString("street");

                        if(!jData.getString("landmark").trim().equalsIgnoreCase("")){


                            str = str +jData.getString("landmark")+", "+", "+jData.getString("city")+", "+jData.getString("state")+" - "+jData.getString("postcode");

                        }else{
                            str = str +", "+jData.getString("city")+", "+jData.getString("state")+" - "+jData.getString("postcode");


                        }


                        intent = new Intent(AddAddressActivity.this, ShippingAddressActivity.class);
                        intent.putExtra("ProductId",strProductId);
                        intent.putExtra("Address", str);
                        intent.putExtra("AddressId", jData.getString("address_id"));
                        intent.putExtra("AddressName", jData.getString("name"));

                        intent.putExtra("From", strFromScreen);

                        HashMap<String, String> hash = new HashMap<>();
                        hash = SessionStore.getUserDetails(AddAddressActivity.this, Common.userPrefName);

                        SessionStore.save(AddAddressActivity.this, Common.userPrefName, hash.get(SessionStore.USER_ID), hash.get(SessionStore.USER_TOKEN), hash.get(SessionStore.USER_EMAIL)
                                , hash.get(SessionStore.USER_MOBILENO)   , hash.get(SessionStore.USER_FIRST_NAME),hash.get(SessionStore.USER_LAST_NAME), hash.get(SessionStore.PROFILEPICTURE), jData.getString("address_id"),jData.getString("name"),str,
                                hash.get(SessionStore.USER_CURRENCYTYPE),hash.get(SessionStore.COUNTRY_CODE));

                        startActivity(intent);
                    }



                    overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
                    finish();

                } else {

                    Methods.showToast(AddAddressActivity.this, result.getString("msg"));

                    Log.e("AddAddressActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }

            }


        }catch (Exception e){


            Log.e("AddAddressActivity", "Exception attemptTOGetUserInfo ServerCallBackSuccess: "+e.getMessage());
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


}
