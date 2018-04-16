package com.mohi.in.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuLightTextView;
import com.mohi.in.widgets.UbuntuRegularButton;
import com.mohi.in.widgets.UbuntuRegularEditText;

import org.json.JSONObject;

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {


    private UbuntuRegularEditText et_name, et_phoneNo, et_pinCode, et_flat, et_colony, et_landmark, et_city, et_state, et_contryName;
    private UbuntuRegularButton but_submit;
    private Intent intent;
    private String strAddressId="", strName="", strMobile="", strPinCode="", strFlat="", strStreet="", strLandMark="", strCity="", strState="", strContry="";



    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        intent  = getIntent();


        if (intent.getStringExtra("AddressId")!=null){


          /*  intent.putExtra("AddressId", model.address_id);
            intent.putExtra("Flat", model.flat_no);
            intent.putExtra("Street", model.street);
            intent.putExtra("Landmark", model.landmark);
            intent.putExtra("Name", model.name);
            intent.putExtra("Mobile", model.mobile);
            intent.putExtra("PinCode", model.postcode);
            intent.putExtra("City", model.city);
            intent.putExtra("State", model.state);
            intent.putExtra("Contry", model.country);*/

            strAddressId = intent.getStringExtra("AddressId");
            strName = intent.getStringExtra("Name");
            strMobile = intent.getStringExtra("Mobile");
            strPinCode = intent.getStringExtra("PinCode");
            strFlat = intent.getStringExtra("Flat");
            strStreet = intent.getStringExtra("Street");
            strLandMark = intent.getStringExtra("Landmark");
            strCity = intent.getStringExtra("City");
            strState = intent.getStringExtra("State");
            strContry = intent.getStringExtra("Contry");


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
        mrl_menu = (MaterialRippleLayout)findViewById(R.id.Header_MenuLayOut);
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




        et_name.setText(strName.trim());
        et_name.setSelection(strName.trim().length());

        et_phoneNo.setText(strMobile.trim());
        et_phoneNo.setSelection(strMobile.trim().length());

        et_pinCode.setText(strPinCode.trim());
        et_pinCode.setSelection(strPinCode.trim().length());

        et_flat.setText(strFlat.trim());
        et_flat.setSelection(strFlat.trim().length());

        et_colony.setText(strStreet.trim());
        et_colony.setSelection(strStreet.trim().length());

        et_landmark.setText(strLandMark.trim());
        et_landmark.setSelection(strLandMark.trim().length());

        et_city.setText(strCity.trim());
        et_city.setSelection(strCity.trim().length());

        et_state.setText(strState.trim());
        et_state.setSelection(strState.trim().length());

        et_contryName.setText(strContry.trim());
        et_contryName.setSelection(strContry.trim().length());







    }

    @Override
    protected void onResume() {
        super.onResume();
        setValue();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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

    private void setAddressValue(){


    }


    private void attemptGetAddress(){
try {

    JsonObject json = new JsonObject();
    json.addProperty("user_id", SessionStore.getUserDetails(EditAddressActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
    json.addProperty("token", SessionStore.getUserDetails(EditAddressActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
    json.addProperty("address_id", strAddressId);


    ServerCalling.ServerCallingUserApiPost(EditAddressActivity.this, "addShippingAddress", json, this);


}catch (Exception e){

    Log.e("EditAddressActivity",""+e.getMessage());
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


        if (TextUtils.isEmpty(contry)) {
            et_contryName.setError("Please enter contry");
            focusView = et_contryName;
            cancel = true;
        }

        if (TextUtils.isEmpty(state)) {
            et_state.setError("Please enter state");
            focusView = et_state;
            cancel = true;
        }

        if (TextUtils.isEmpty(city)) {
            et_city.setError("Please enter city");
            focusView = et_city;
            cancel = true;
        }

        if (TextUtils.isEmpty(colony)) {
            et_colony.setError("Please enter Colony / Street / Locality");
            focusView = et_colony;
            cancel = true;
        }

        if (TextUtils.isEmpty(flat)) {
            et_flat.setError("Please enter Flat / House No. / Floor / Building");
            focusView = et_flat;
            cancel = true;
        }


        if (TextUtils.isEmpty(pinCode)) {
            et_pinCode.setError("Please enter pincode");
            focusView = et_pinCode;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneNo)) {
            et_phoneNo.setError("Please enter mobile no.");
            focusView = et_phoneNo;
            cancel = true;
        }else if(!Methods.isValidMobile(phoneNo)){

            et_phoneNo.setError("Please enter valid mobile no.");
            focusView = et_phoneNo;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
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
                JsonObject json = new JsonObject();
                json.addProperty("user_id", SessionStore.getUserDetails(EditAddressActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(EditAddressActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                json.addProperty("name", name);
                json.addProperty("country", contry);
                json.addProperty("city", city);
                json.addProperty("postcode", pinCode);
                json.addProperty("mobile", phoneNo);
                json.addProperty("state", state);
                json.addProperty("street",  colony);
                json.addProperty("flat_no",  flat);
                json.addProperty("landmark",  et_landmark.getText().toString());
                json.addProperty("address_id",  strAddressId);

                ServerCalling.ServerCallingUserApiPost(EditAddressActivity.this, "updateShippingAddress", json, this);


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
            if(strfFrom.trim().equalsIgnoreCase("updateShippingAddress")) {

                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(EditAddressActivity.this, result.getString("msg"));

                    onBackPressed();

                } else {

                    Methods.showToast(EditAddressActivity.this, result.getString("msg"));

                    Log.e("AddAddressActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }

            }else if(strfFrom.trim().equalsIgnoreCase("EditShippingAddress")) {

                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    setAddressValue();

                } else {

                    Methods.showToast(EditAddressActivity.this, result.getString("msg"));

                    Log.e("AddAddressActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }

            }



        }catch (Exception e){


            Log.e("AddAddressActivity", "Exception attemptTOGetUserInfo ServerCallBackSuccess: "+e.getMessage());
        }

    }





}
