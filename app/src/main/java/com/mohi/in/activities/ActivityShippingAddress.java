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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.AddressModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.ServerCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityShippingAddress extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ServerCallBack {

    private Context mContext;
    private EditText street_one_et;
    private EditText street_two_et;
    private EditText city_et;
    private EditText region_et;
    private EditText postcode_et;
    private EditText first_name_et;
    private EditText last_name_et;
    private EditText mob_no_et;
    private Spinner country_name_spinner;
    private ArrayList<String> country_names;
    private ArrayList<String> country_id;
    private RadioGroup default_shipping_radioGroup;
    private RadioButton default_shipping_true;
    private RadioButton default_shipping_false;
    private RadioGroup default_billing_radioGroup;
    private RadioButton default_billing_true;
    private RadioButton default_billing_false;
    private String cntry_id;
    private boolean isDefault_shipping;
    private boolean isDefault_billing;
    private Button address_btn;
    private String address_id;

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
        first_name_et = findViewById(R.id.first_name_et);
        last_name_et = findViewById(R.id.last_name_et);
        street_one_et = findViewById(R.id.street_one_et);
        street_two_et = findViewById(R.id.street_two_et);
        mob_no_et = findViewById(R.id.mob_no_et);
        city_et = findViewById(R.id.city_et);
        region_et = findViewById(R.id.region_et);
        postcode_et = findViewById(R.id.postcode_et);
        country_name_spinner = findViewById(R.id.country_name_spinner);
        default_shipping_radioGroup = findViewById(R.id.default_shipping_radioGroup);
        default_shipping_true = findViewById(R.id.default_shipping_true);
        default_shipping_false = findViewById(R.id.default_shipping_false);
        default_billing_radioGroup = findViewById(R.id.default_billing_radioGroup);
        default_billing_true = findViewById(R.id.default_billing_true);
        default_billing_false = findViewById(R.id.default_billing_false);
        address_btn = findViewById(R.id.address_btn);
        isDefault_shipping = true;
        isDefault_billing = true;
        address_id = "";
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

        if (getIntent().hasExtra("model")) {
            AddressModel obj = getIntent().getParcelableExtra("model");
            setObtainedValues(obj);
        }

        ArrayAdapter<String> countryNameAdapter = new ArrayAdapter<>(this, R.layout.spinner_textview, country_names);
        country_name_spinner.setAdapter(countryNameAdapter);
        country_name_spinner.setOnItemSelectedListener(this);
        address_btn.setOnClickListener(this);
    }

    private void setObtainedValues(AddressModel obj) {
        address_id = obj.getAddress_id();
        String firstname = obj.getFirstname();
        String lastname = obj.getLastname();
        String telephone = obj.getTelephone();
        String street_1 = obj.getStreet_1();
        String street_2 = obj.getStreet_2();
        String city = obj.getCity();
        String region = obj.getRegion();
        String postcode = obj.getPostcode();
        String country_id = obj.getCountry_id();
        Boolean default_shipping = obj.getDefault_shipping();
        Boolean default_billing = obj.getDefault_billing();

        first_name_et.setText(firstname);
        last_name_et.setText(lastname);
        street_one_et.setText(street_1);
        street_two_et.setText(street_2);
        mob_no_et.setText(telephone);
        city_et.setText(city);
        region_et.setText(region);
        postcode_et.setText(postcode);
        country_name_spinner.setSelection(getPositionOfSpinner(country_id));
        default_shipping_true.setChecked(default_shipping);
        default_shipping_false.setChecked(!default_shipping);
        default_billing_true.setChecked(default_billing);
        default_billing_false.setChecked(!default_billing);
        address_btn.setText("Update Address");
    }

    private int getPositionOfSpinner(String cntry_id) {
        int pos = 0;
        for (int i = 0; i < country_id.size(); i++) {
            if (country_id.get(i).trim().equalsIgnoreCase(cntry_id)) {
                pos = i;
            }
        }
        return pos;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_btn:
                attemptAddress();
                break;
        }
    }

    private void attemptAddress() {

        //Reset All Errors
        resetAllError();
        String firstName = first_name_et.getText().toString().trim();
        String lastName = last_name_et.getText().toString().trim();
        String line1Address = street_one_et.getText().toString().trim();
        String line2Address = street_two_et.getText().toString().trim();
        String phoneNmuber = mob_no_et.getText().toString().trim();
        String city = city_et.getText().toString().trim();
        String region = region_et.getText().toString().trim();
        String postCode = postcode_et.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstName)) {
            first_name_et.setError("Please enter first name");
            focusView = first_name_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName)) {
            last_name_et.setError("Please enter last name");
            focusView = last_name_et;
            cancel = true;
        }

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


        if (TextUtils.isEmpty(phoneNmuber.trim())) {
            mob_no_et.setError("Please enter phone number");
            focusView = mob_no_et;
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
                json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                json.addProperty("firstname", firstName);
                json.addProperty("lastname", lastName);
                json.addProperty("telephone", phoneNmuber);
                json.addProperty("street_1", line1Address);
                json.addProperty("street_2", line2Address);
                json.addProperty("city", city);
                json.addProperty("region", region);
                json.addProperty("postcode", postCode);
                json.addProperty("country_id", cntry_id);
                json.addProperty("default_shipping", isDefault_shipping);
                json.addProperty("default_billing", isDefault_billing);
                if (address_id != null && !TextUtils.isEmpty(address_id)) {
                    json.addProperty("address_id", address_id);
                    ServerCalling.ServerCallingUserApiPost(mContext, "updateaddress", json, this);
                } else {
                    ServerCalling.ServerCallingUserApiPost(mContext, "createaddress", json, this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resetAllError() {
        first_name_et.setError(null);
        last_name_et.setError(null);
        mob_no_et.setError(null);
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

        switch (strfFrom.trim()) {
            case "createaddress": {
                callSuccesfullyAddressCreateed(jobj);
                break;
            }
            case "updateAddress": {
                Methods.showToast(mContext,"Address Updated Successfully");
                finish();
                break;
            }
        }
    }

    private void callSuccesfullyAddressCreateed(JSONObject jobj) {
        if (jobj.optString("status").trim().equalsIgnoreCase("success")) {
            JSONObject data = jobj.optJSONObject("data");
            String address_id = data.optString("address_id");
            String telephone = data.optString("telephone");
            String street_1 = data.optString("street_1");
            String street_2 = data.optString("street_2");
            String city = data.optString("city");
            String region = data.optString("region");
            String postcode = data.optString("postcode");
            String country_id = data.optString("country_id");
            boolean default_shipping = data.optBoolean("default_shipping");
            boolean default_billing = data.optBoolean("default_billing");
            SessionStore.saveUserAddress(mContext, Common.USER_PREFS_NAME, address_id, telephone, street_1, street_2, city, region, postcode, country_id, default_shipping, default_billing);
            finish();
        }
    }
}