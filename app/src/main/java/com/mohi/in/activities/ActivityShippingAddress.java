package com.mohi.in.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityShippingAddress extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ServerCallBack {

    private Context mContext;
    private EditText street_one_et;
    private EditText street_two_et;
    private EditText city_et;
    private EditText region_et;
    private EditText postcode_et;
    private Spinner country_name_spinner;
    private ArrayList<String> country_names;
    private ArrayList<String> country_id;
    private RadioGroup default_shipping_radioGroup;
    private RadioGroup default_billing_radioGroup;
    private String cntry_id;
    private boolean isDefault_shipping;
    private boolean isDefault_billing;
    private Button update_address_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_new);
        init();
    }

    private void init() {
        mContext = this;
        country_names = new ArrayList<>();
        country_id = new ArrayList<>();
        street_one_et = findViewById(R.id.street_one_et);
        street_two_et = findViewById(R.id.street_two_et);
        city_et = findViewById(R.id.city_et);
        region_et = findViewById(R.id.region_et);
        postcode_et = findViewById(R.id.postcode_et);
        country_name_spinner = findViewById(R.id.country_name_spinner);
        default_shipping_radioGroup = findViewById(R.id.default_shipping_radioGroup);
        default_billing_radioGroup = findViewById(R.id.default_billing_radioGroup);
        update_address_btn = findViewById(R.id.update_address_btn);
        isDefault_shipping = true;
        isDefault_billing = true;
        setValue();

    }

    private void setValue() {
        //Country Name Spinner
        country_names.add("Bahrain");
        country_names.add("Canada");
        country_names.add("India");
        country_names.add("Kuwait");
        country_names.add("Oman");
        country_names.add("Qatar");
        country_names.add("Saudi Arabia");
        country_names.add("United Arab Emirated");
        country_names.add("United Kingdom");
        country_names.add("United States");

        //Corrosponding Country ID List
        country_id.add("BH");
        country_id.add("CA");
        country_id.add("IN");
        country_id.add("KW");
        country_id.add("OM");
        country_id.add("QA");
        country_id.add("SA");
        country_id.add("AE");
        country_id.add("GB");
        country_id.add("US");

        ArrayAdapter<String> countryNameAdapter = new ArrayAdapter<>(this, R.layout.spinner_textview, country_names);
        country_name_spinner.setAdapter(countryNameAdapter);
        country_name_spinner.setOnItemSelectedListener(this);
        update_address_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_address_btn:
                attemptSubmitAddress();
                break;
        }
    }

    private void attemptSubmitAddress() {
        //Reset All Errors
        resetAllError();

        String line1Address = street_one_et.getText().toString();
        String line2Address = street_two_et.getText().toString();
        String city = city_et.getText().toString();
        String region = region_et.getText().toString();
        String postCode = postcode_et.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(line1Address.trim())) {
            street_one_et.setError("Please enter Colony / Street / Locality");
            focusView = street_one_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(line2Address.trim())) {
            street_two_et.setError("Please enter Colony / Street / Locality");
            focusView = street_two_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(city.trim())) {
            city_et.setError("Please enter City");
            focusView = city_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(region.trim())) {
            region_et.setError("Please enter Region");
            focusView = region_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(postCode.trim())) {
            postcode_et.setError("Please enter PostCode");
            focusView = postcode_et;
            cancel = true;
        }

        if (default_shipping_radioGroup.getCheckedRadioButtonId() == R.id.default_shipping_false) {
            isDefault_shipping = false;
        }

        if (default_billing_radioGroup.getCheckedRadioButtonId() == R.id.default_billing_false) {
            isDefault_billing = false;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                WaitDialog.showDialog(this);
                JsonObject json = new JsonObject();
                json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                json.addProperty("firstname", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_FIRST_NAME));
                json.addProperty("lastname", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_LAST_NAME));
                json.addProperty("telephone", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_MOBILENO));
                json.addProperty("street_1", line1Address);
                json.addProperty("street_2", line2Address);
                json.addProperty("city", city);
                json.addProperty("region", region);
                json.addProperty("postcode", postCode);
                json.addProperty("country_id", cntry_id);
                json.addProperty("default_shipping", isDefault_shipping);
                json.addProperty("default_billing", isDefault_billing);

                ServerCalling.ServerCallingUserApiPost(mContext, "createaddress", json, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetAllError() {
        street_one_et.setError(null);
        street_two_et.setError(null);
        city_et.setError(null);
        region_et.setError(null);
        postcode_et.setError(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cntry_id = country_id.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void ServerCallBackSuccess(JSONObject jobj, String strfFrom) {
        WaitDialog.hideDialog();
        try {
            if (jobj.getString("status").trim().equalsIgnoreCase("success")) {
                if (strfFrom.equalsIgnoreCase("createaddress")) {
                    JSONObject data = jobj.getJSONObject("data");
                    String address_id = data.optString("address_id");
                    String telephone = data.optString("telephone");
                    String street_1 = data.optString("street_1");
                    String street_2 = data.optString("street_2");
                    String city = data.optString("city");
                    String region = data.optString("region");
                    String postcode = data.optString("postcode");
                    String country_id = data.optString("country_id");
                    boolean default_shipping=data.optBoolean("default_shipping");
                    boolean default_billing=data.optBoolean("default_billing");
                    SessionStore.saveUserAddress(mContext, Common.userPrefName, address_id, telephone, street_1, street_2, city, region, postcode, country_id,default_shipping,default_billing);
                    finish();
                } else {
                    Methods.showToast(mContext, "Error..");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}