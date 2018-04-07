package com.mohi.in.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.Urls;
import com.mohi.in.widgets.CenturyGothicBoldEditText;
import com.mohi.in.widgets.CenturyGothicRegularEditText;
import com.mohi.in.widgets.UbuntuRegularButton;
import com.mohi.in.widgets.UbuntuRegularTextView;

import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {



    private UbuntuRegularTextView tv_TermsAndConditions;
    private CenturyGothicRegularEditText  et_name, et_password, et_confirmPassword, et_phoneCode, et_phoneNo;
    private UbuntuRegularButton btn_signup;
    private CenturyGothicBoldEditText et_email;

    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        init();

    }


    //init method for maping widgets

    private void init() {

        intent = getIntent();

        tv_TermsAndConditions = (UbuntuRegularTextView) findViewById(R.id.Signup_TermsAndConditions);

        et_name = (CenturyGothicRegularEditText) findViewById(R.id.Signup_Name);
        et_email = (CenturyGothicBoldEditText) findViewById(R.id.Signup_Email);
        et_password = (CenturyGothicRegularEditText) findViewById(R.id.Signup_Password);
        et_confirmPassword = (CenturyGothicRegularEditText) findViewById(R.id.Signup_ConfirmPassword);
        et_phoneCode = (CenturyGothicRegularEditText) findViewById(R.id.Signup_PhoneCode);
        et_phoneNo = (CenturyGothicRegularEditText) findViewById(R.id.Signup_PhoneNo);

        btn_signup = (UbuntuRegularButton) findViewById(R.id.Signup_button);

        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);


        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);
        setValue();

    }

    //setValue method
    private void setValue() {


//        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((Common.getDeviceHeightWidth(SignupActivity.this).heightPixels) * 0.07));
//        space.setLayoutParams(spaceParams);

        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.INVISIBLE);

        mrl_search.setVisibility(View.INVISIBLE);
        mrl_filter.setVisibility(View.INVISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);



        SpannableString ss = new SpannableString(getResources().getString(R.string.terms_and_conditions));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

               // Toast.makeText(SignupActivity.this, "Click here", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignupActivity.this, WebViewActivity.class);
                intent.putExtra("URL", Urls.TEAMSANDCONDITIONURL);
                startActivity(intent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);

                Typeface face = Typeface.createFromAsset(getAssets(), "font/century_gothic_bold.ttf");
                ds.setTypeface(face);

                ds.setColor(getResources().getColor(R.color.termsandcondition_color));

                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 41, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        tv_TermsAndConditions.setText(ss);
        tv_TermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        tv_TermsAndConditions.setHighlightColor(Color.BLUE);


        et_phoneNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });


        btn_signup.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(intent.getStringExtra("From")!=null) {
            Intent intent = new Intent(SignupActivity.this, LoginActivityNew.class);
            startActivity(intent);
            overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        }
        finish();
    }


    // OnClickListener
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.Signup_button:

                attemptSignUp();

                break;
            case R.id.Header_Menu:

                onBackPressed();

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
    private void attemptSignUp() {


        // Reset errors.
        et_name.setError(null);
        et_email.setError(null);
        et_password.setError(null);
        et_confirmPassword.setError(null);
        et_phoneCode.setError(null);
        et_phoneNo.setError(null);


        // Store values at the time of the login attempt.
        String email = et_email.getText().toString();
        String name = et_name.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();
        String phoneNo = et_phoneNo.getText().toString();
        String phoneCode = et_phoneCode.getText().toString();

        boolean cancel = false;
        View focusView = null;









        if (TextUtils.isEmpty(phoneNo)) {
            et_phoneNo.setError(getString(R.string.error_field_required));
            focusView = et_phoneNo;
            cancel = true;
        }else if (!Methods.isPhoneNoValid(phoneNo)) {
            et_phoneNo.setError(getString(R.string.error_invalid_phoneno));
            focusView = et_phoneNo;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneCode)) {
            et_phoneCode.setError(getString(R.string.error_field_required));
            focusView = et_phoneCode;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            et_confirmPassword.setError(getString(R.string.error_field_required));
            focusView = et_confirmPassword;
            cancel = true;
        }else if(!confirmPassword.trim().equals(password.trim())){

            et_confirmPassword.setError(getString(R.string.error_notmatch_password));
            focusView = et_confirmPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            et_password.setError(getString(R.string.error_field_required));
            focusView = et_password;
            cancel = true;
        } else if (!Methods.isPasswordValid(password)) {
            et_password.setError(getString(R.string.error_invalid_password));
            focusView = et_password;
            cancel = true;
        }



        if (TextUtils.isEmpty(email)) {
            et_email.setError(getString(R.string.error_field_required));
            focusView = et_email;
            cancel = true;
        } else if (!Methods.isValidEmail(email)) {
            et_email.setError(getString(R.string.error_invalid_email));
            focusView = et_email;
            cancel = true;
        }


        if (TextUtils.isEmpty(name)) {
            et_name.setError(getString(R.string.error_field_required));
            focusView = et_name;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("email", email);
            json.addProperty("password", password);
            json.addProperty("name", name);
            json.addProperty("mobile_number", phoneCode+""+phoneNo);


            ServerCalling.ServerCallingUserApiPost(SignupActivity.this, "signUp", json, this);



        }

    }




    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {

        WaitDialog.hideDialog();
        try {
        if (result.getString("status").trim().equalsIgnoreCase("1")) {

            JSONObject data = result.getJSONObject("data");
            String address_id = "";
            String telephone = "";
            String street_1 = "";
            String street_2 = "";
            String city = "";
            String region = "";
            String postcode = "";
            String country_id = "";
            if (data.has("address")) {
                JSONObject addressData = data.optJSONObject("address");
                address_id = addressData.optString("address_id");
                telephone = addressData.optString("telephone");
                street_1 = addressData.optString("street_1");
                street_2 = addressData.optString("street_2");
                city = addressData.optString("city");
                region = addressData.optString("region");
                postcode = addressData.optString("postcode");
                country_id = addressData.optString("country_id");
            }
            String user_id = data.optString("user_id");
            String token = data.optString("token");
            String email = data.optString("email");
            String mob_number = data.optString("mob_number");
            String firstName = data.optString("firstname");
            String lastName = data.optString("lastname");
            String user_image = data.optString("user_image");
            String currency = data.optString("currency");
            String cntry_code = data.optString("cntry_code");

            SessionStore.saveUserDetails(this, Common.userPrefName, user_id, token, email, mob_number, firstName, lastName, user_image, currency, cntry_code, address_id, telephone, street_1, street_2, city, region, postcode, country_id);




           /* Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
            startActivity(intent);*/
            overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
            finish();


        } else {

            Methods.showToast(SignupActivity.this, result.getString("msg"));

            Log.e("Signup", "Signup User log: " +result.getString("msg"));
        }
        } catch (Exception ee) {

            Log.e("Signup", "Signup User Exception: " +ee.getMessage());
        }
    }
}
