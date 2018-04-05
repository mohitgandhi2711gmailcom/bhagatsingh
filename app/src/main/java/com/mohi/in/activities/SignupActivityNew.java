package com.mohi.in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONObject;

import java.util.ArrayList;

public class SignupActivityNew extends AppCompatActivity implements View.OnClickListener, ServerCallBack, AdapterView.OnItemSelectedListener {

    private Context mContext;
    private Intent intent;
    private ImageView back_iv;
    private EditText first_name_et;
    private EditText last_name_et;
    private EditText mail_et;
    private Spinner country_code_spinner;
    private EditText phone_number_et;
    private EditText pass_et;
    private Button register_btn;
    private TextView already_account_tv;
    private Button facebook_btn;
    private Button google_btn;
    private boolean isPasswordCoded = false;
    private String country_code;
    private ArrayList<String> country_codes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);
        init();
    }

    private void init() {
        mContext = SignupActivityNew.this;
        intent = getIntent();
        back_iv = findViewById(R.id.back_iv);
        first_name_et = findViewById(R.id.first_name_et);
        last_name_et = findViewById(R.id.last_name_et);
        mail_et = findViewById(R.id.mail_et);
        country_code_spinner = findViewById(R.id.country_code_spinner);
        phone_number_et = findViewById(R.id.phone_number_et);
        pass_et = findViewById(R.id.pass_et);
        register_btn = findViewById(R.id.register_btn);
        already_account_tv = findViewById(R.id.already_account_tv);
        facebook_btn = findViewById(R.id.facebook_btn);
        google_btn = findViewById(R.id.google_btn);
        setValue();
    }

    private void setValue() {

        back_iv.setOnClickListener(this);
        pass_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (pass_et.getRight() - pass_et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (isPasswordCoded) {
                            pass_et.setTransformationMethod(null);
                            pass_et.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            isPasswordCoded = false;
                        } else {
                            pass_et.setTransformationMethod(new PasswordTransformationMethod());
                            pass_et.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            isPasswordCoded = true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        register_btn.setOnClickListener(this);
        already_account_tv.setOnClickListener(this);
        facebook_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);

        //Spinner Items
        country_codes = new ArrayList<>();
        country_codes.add("+91");
        country_codes.add("+971");
        country_codes.add("+966");
        country_codes.add("+974");
        country_codes.add("+968");
        country_codes.add("+973");
        country_codes.add("+965");
        country_codes.add("+44");
        country_codes.add("+1");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_textview, country_codes);
        country_code_spinner.setAdapter(dataAdapter);
        country_code_spinner.setOnItemSelectedListener(this);
    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (intent.getStringExtra("From") != null) {
            Intent intent = new Intent(mContext, LoginActivityNew.class);
            startActivity(intent);
            overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        }
        finish();
    }


    // OnClickListener
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_iv:
                onBackPressed();
                break;

            case R.id.register_btn:
                attemptSignUp();
                break;

            case R.id.already_account_tv:
                Intent intent = new Intent(mContext, LoginActivityNew.class);
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
                finish();
                break;

            case R.id.facebook_btn:
                Methods.showToast(mContext, "Facebook Login");
                break;

            case R.id.google_btn:
                Methods.showToast(mContext, "Google Login");
                break;

            default:
                Methods.showToast(mContext, "Error on SignUp");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attemptSignUp() {
        // Initailly Remove all Errors
        removeAllError();

        String first_name = first_name_et.getText().toString();
        String last_name = last_name_et.getText().toString();
        String email = mail_et.getText().toString();

        String phone_number = phone_number_et.getText().toString();
        String password = pass_et.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(first_name)) {
            first_name_et.setError(getString(R.string.error_field_required));
            focusView = first_name_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(last_name)) {
            last_name_et.setError(getString(R.string.error_field_required));
            focusView = last_name_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mail_et.setError(getString(R.string.error_field_required));
            focusView = mail_et;
            cancel = true;
        } else if (!Methods.isValidEmail(email)) {
            mail_et.setError(getString(R.string.error_invalid_email));
            focusView = mail_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone_number)) {
            phone_number_et.setError(getString(R.string.error_field_required));
            focusView = phone_number_et;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            pass_et.setError(getString(R.string.error_field_required));
            focusView = pass_et;
            cancel = true;
        } else if (!Methods.isPasswordValid(password)) {
            pass_et.setError(getString(R.string.error_invalid_password));
            focusView = pass_et;
            cancel = true;
        }
        if (cancel) {
            // Show Error
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("firstname", first_name);
            json.addProperty("lastname", last_name);
            json.addProperty("email", email);
            json.addProperty("password", password);
            json.addProperty("cntry_code", country_code);
            json.addProperty("mob_number", phone_number);
            ServerCalling.ServerCallingUserApiPost(mContext, "signUp", json, this);
        }
    }

    private void removeAllError() {
        first_name_et.setError(null);
        last_name_et.setError(null);
        mail_et.setError(null);
        pass_et.setError(null);
        phone_number_et.setError(null);
    }


    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        try {
            if (result.getString("status").trim().equalsIgnoreCase("success")) {
                JSONObject data = result.getJSONObject("data");
                /*String strAddress = "", strAddressName = "", strAddresId = "";
                if (!data.isNull("address")) {
                    JSONObject addressData = data.getJSONObject("address");
                    String str = addressData.getString("flat_no") + ", " + addressData.getString("street");
                    if (!addressData.getString("landmark").equalsIgnoreCase("")) {
                        str = str + ", " + addressData.getString("landmark") + ", " + addressData.getString("city") + ", " + addressData.getString("state") + ", " +
                                addressData.getString("postcode");
                    } else {
                        str = str + ", " + addressData.getString("city") + ", " + addressData.getString("state") + ", " + addressData.getString("postcode");
                    }
                    strAddressName = addressData.getString("name");
                    strAddresId = addressData.getString("address_id");
                    strAddress = str;
                }*/

                String user_id = data.getString("user_id");
                String token = data.getString("token");
                String email = data.getString("email");
                String mob_number = data.getString("mob_number");
                String firstname = data.getString("firstname");
                String lastname = data.getString("lastname");
                String user_image = data.getString("user_image");
                String currency = data.getString("currency");
                String strAddresId = "";
                String strAddressName = "";
                String strAddress = "";
                String cntry_code = data.getString("cntry_code");

                //Not using Right Now
                String address = data.getString("address");

                SessionStore.save(mContext, Common.userPrefName, user_id, token, email, mob_number, firstname, lastname, user_image, strAddresId, strAddressName, strAddress, currency, cntry_code);
                overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                finish();
            } else {
                Methods.showToast(mContext, result.getString("msg"));
                Log.e("Signup", "Signup User log: " + result.getString("msg"));
            }
        } catch (Exception ee) {
            Log.e("Signup", "Signup User Exception: " + ee.getMessage());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        country_code = country_codes.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Methods.showToast(mContext, "Please Select Country Code");
    }
}