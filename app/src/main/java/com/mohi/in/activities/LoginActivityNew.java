package com.mohi.in.activities;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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
import com.mohi.in.fragments.LoginOTPDialog;
import com.mohi.in.listener.OtpDialogDismissListener;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivityNew extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnClickListener, ServerCallBack, OtpDialogDismissListener, AdapterView.OnItemSelectedListener {

    private static final int REQUEST_READ_CONTACTS = 0;
    private Button btn_emailSignInButton;
    private TextView btn_signUpButton, tv_forgotPassword;
    Intent intent;
    private Context mContext;
    private EditText mPasswordView;  /*mEmailView*/
    private ImageView back_iv;
    private Button otp_btn;
    private Spinner country_code_spinner;
    private EditText phone_number_et;
    private String country_code;
    private boolean isPasswordCoded = true;
    private ArrayList<String> country_codes;
    private boolean isNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_new);
        mContext = LoginActivityNew.this;
        intent = getIntent();
        init();
    }

    private void init() {
        otp_btn = findViewById(R.id.otp_btn);
        phone_number_et = findViewById(R.id.phone_number_et);
        btn_emailSignInButton = findViewById(R.id.login_btn);
        country_code_spinner = findViewById(R.id.country_code_spinner);
        btn_signUpButton = findViewById(R.id.register_here_tv);
        mPasswordView = findViewById(R.id.pass_et);
//        mEmailView = findViewById(R.id.username_mail_id_et);
        back_iv = findViewById(R.id.back_iv);
        tv_forgotPassword = findViewById(R.id.forgot_tv);
        mPasswordView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mPasswordView.getRight() - mPasswordView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (isPasswordCoded) {
                            mPasswordView.setTransformationMethod(null);
                            mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            isPasswordCoded = false;
                        } else {
                            mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
                            mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            isPasswordCoded = true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        setValue();

        phone_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 2) {
                    if (TextUtils.isDigitsOnly(editable.toString())) {
                        isNumber = true;
                        country_code_spinner.setVisibility(View.VISIBLE);
                    } else {
                        isNumber = false;
                        country_code_spinner.setVisibility(View.GONE);
                    }
                } else {
                    isNumber = false;
                    country_code_spinner.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setValue() {
        populateAutoComplete();
        back_iv.setOnClickListener(this);
        btn_emailSignInButton.setOnClickListener(this);
        btn_signUpButton.setOnClickListener(this);
        tv_forgotPassword.setOnClickListener(this);
        otp_btn.setOnClickListener(this);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login_btn || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

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

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(phone_number_et, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA, ACCESS_NETWORK_STATE}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {
        removePreviousError();
        String email = phone_number_et.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            phone_number_et.setError(getString(R.string.error_field_required));
            focusView = phone_number_et;
            cancel = true;
        }
        if (!isNumber) {
            if (!Methods.isValidEmail(email)) {
                phone_number_et.setError(getString(R.string.error_invalid_email));
                focusView = phone_number_et;
                cancel = true;
            }
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!Methods.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!Methods.isValidEmail(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("email", email);
            json.addProperty("password", password);
            if (isNumber) {
                json.addProperty("cntry_code", country_code);
                json.addProperty("mob_number", email);

            } else {
                json.addProperty("email", email);
            }
            ServerCalling.ServerCallingUserApiPost(mContext, "login", json, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        //  mEmailView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.forgot_tv:
                // attemptLogin();

                intent = new Intent(mContext, ForGotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_up, R.anim.slide_out_down);
                finish();
                break;

            case R.id.login_btn:
                attemptLogin();
                break;

            case R.id.register_here_tv:
                intent = new Intent(mContext, SignupActivityNew.class);
                intent.putExtra("From", "login");
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                finish();
                break;

            case R.id.back_iv:
                onBackPressed();
                break;

            case R.id.otp_btn:
                attemptOTPLogin();
                break;
        }

    }

    private void attemptOTPLogin() {
        removePreviousError();
        String phone_no = phone_number_et.getText().toString();
        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(phone_no)) {
            phone_number_et.setError(getString(R.string.error_field_required));
            focusView = phone_number_et;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("cntry_code", country_code);
            json.addProperty("mob_number", phone_no);
            ServerCalling.ServerCallingUserApiPost(mContext, "sendOtp", json, this);
        }
    }

    private void removePreviousError() {
        phone_number_et.setError(null);
//        mEmailView.setError(null);
        mPasswordView.setError(null);
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            WaitDialog.hideDialog();
            if (result.getString("status").trim().equalsIgnoreCase("success")) {
                switch (strfFrom) {
                    case "login":
                        afterSuccessfullyLogin(result);
                        break;
                    case "sendOtp":
                        Methods.showToast(mContext, result.optString("message"));
                        String phone_no = phone_number_et.getText().toString();
                        LoginOTPDialog dialog = new LoginOTPDialog(mContext, country_code, phone_no);
                        dialog.show();
                        break;
                }
            } else {
                Methods.showToast(mContext, result.optString("message"));
                Methods.showToast(mContext, result.optString("msg"));
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    private void afterSuccessfullyLogin(JSONObject result) {
        JSONObject data = result.optJSONObject("data");

        if (data.has("address")) {
            JSONObject addressData = data.optJSONObject("address");
            String address_id = addressData.optString("address_id");
            String telephone = addressData.optString("telephone");
            String street_1 = addressData.optString("street_1");
            String street_2 = addressData.optString("street_2");
            String city = addressData.optString("city");
            String region = addressData.optString("region");
            String postcode = addressData.optString("postcode");
            String country_id = addressData.optString("country_id");
            Boolean default_shipping = addressData.optBoolean("default_billing");
            Boolean default_billing = addressData.optBoolean("default_billing");
            SessionStore.saveUserAddress(mContext, Common.userPrefName, address_id, telephone, street_1, street_2, city, region, postcode, country_id, default_shipping, default_billing);
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
        SessionStore.saveUserDetails(mContext, Common.userPrefName, user_id, token, email, mob_number, firstName, lastName, user_image, currency, cntry_code);
        finish();
    }

    @Override
    public void handleDialogClose() {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        country_code = country_codes.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private interface ProfileQuery {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

