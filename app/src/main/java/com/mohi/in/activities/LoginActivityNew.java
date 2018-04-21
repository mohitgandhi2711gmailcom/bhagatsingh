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
import android.util.Log;
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
import com.mohi.in.utils.listeners.OtpDialogDismissListener;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
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
    private Button emailSignInButton;
    private TextView signUpTextView;
    private TextView forgotPasswordTextView;
    Intent intent;
    private Context mContext;
    private EditText mPasswordView;
    private ImageView backImageView;
    private Button otpButton;
    private Spinner countryCodeSpinner;
    private EditText phoneNumberEditText;
    private String countryCode;
    private boolean isPasswordCoded = true;
    private ArrayList<String> countryCodes;
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
        otpButton = findViewById(R.id.otp_btn);
        phoneNumberEditText = findViewById(R.id.phone_number_et);
        emailSignInButton = findViewById(R.id.login_btn);
        countryCodeSpinner = findViewById(R.id.country_code_spinner);
        signUpTextView = findViewById(R.id.register_here_tv);
        mPasswordView = findViewById(R.id.pass_et);
        backImageView = findViewById(R.id.back_iv);
        forgotPasswordTextView = findViewById(R.id.forgot_tv);
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

        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                 * Before Text Change Listener
                 * */
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                 * OnText Chnage Listener
                 * */
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 2) {
                    if (TextUtils.isDigitsOnly(editable.toString())) {
                        isNumber = true;
                        countryCodeSpinner.setVisibility(View.VISIBLE);
                    } else {
                        isNumber = false;
                        countryCodeSpinner.setVisibility(View.GONE);
                    }
                } else {
                    isNumber = false;
                    countryCodeSpinner.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setValue() {
        populateAutoComplete();
        backImageView.setOnClickListener(this);
        emailSignInButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
        otpButton.setOnClickListener(this);

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
        countryCodes = new ArrayList<>();
        countryCodes.add("+91");
        countryCodes.add("+971");
        countryCodes.add("+966");
        countryCodes.add("+974");
        countryCodes.add("+968");
        countryCodes.add("+973");
        countryCodes.add("+965");
        countryCodes.add("+44");
        countryCodes.add("+1");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_textview, countryCodes);
        countryCodeSpinner.setAdapter(dataAdapter);
        countryCodeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(this);
        } catch (Exception e) {
            Log.e("Error", e.toString());
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
            Snackbar.make(phoneNumberEditText, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
        String email = phoneNumberEditText.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            phoneNumberEditText.setError(getString(R.string.error_field_required));
            focusView = phoneNumberEditText;
            cancel = true;
        }
        if (!isNumber && !Methods.isValidEmail(email)) {
            phoneNumberEditText.setError(getString(R.string.error_invalid_email));
            focusView = phoneNumberEditText;
            cancel = true;
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
        if (cancel) {
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("email", email);
            json.addProperty("password", password);
            if (isNumber) {
                json.addProperty("cntry_code", countryCode);
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
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.forgot_tv:
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
        String phoneNumber = phoneNumberEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError(getString(R.string.error_field_required));
            focusView = phoneNumberEditText;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("cntry_code", countryCode);
            json.addProperty("mob_number", phoneNumber);
            ServerCalling.ServerCallingUserApiPost(mContext, "sendOtp", json, this);
        }
    }

    private void removePreviousError() {
        phoneNumberEditText.setError(null);
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
                        String phoneNumber = phoneNumberEditText.getText().toString();
                        LoginOTPDialog dialog = new LoginOTPDialog(mContext, countryCode, phoneNumber);
                        dialog.show();
                        dialog.setCancelable(false);
                        break;
                }
            } else {
                Methods.showToast(mContext, result.optString("message"));
                Methods.showToast(mContext, result.optString("msg"));
            }
        } catch (Exception ee) {
            Log.e("Error", ee.toString());
        }
    }

    private void afterSuccessfullyLogin(JSONObject result) {
        JSONObject data = result.optJSONObject("data");

        if (data.has("address")) {
            JSONObject addressData = data.optJSONObject("address");
            String addressId = addressData.optString("address_id");
            String telephone = addressData.optString("telephone");
            String street1 = addressData.optString("street_1");
            String street2 = addressData.optString("street_2");
            String city = addressData.optString("city");
            String region = addressData.optString("region");
            String postcode = addressData.optString("postcode");
            String countryId = addressData.optString("country_id");
            Boolean defaultBilling = addressData.optBoolean("default_billing");
            Boolean defaultShipping = addressData.optBoolean("default_shipping");
            SessionStore.saveUserAddress(mContext, Common.USER_PREFS_NAME, addressId, telephone, street1, street2, city, region, postcode, countryId, defaultShipping, defaultBilling);
        }
        String userId = data.optString("user_id");
        String token = data.optString("token");
        String email = data.optString("email");
        String mobNumber = data.optString("mob_number");
        String firstName = data.optString("firstname");
        String lastName = data.optString("lastname");
        String userImage = data.optString("user_image");
        String currency = data.optString("currency");
        String cntryCode = data.optString("cntry_code");
        SessionStore.saveUserDetails(mContext, Common.USER_PREFS_NAME, userId, token, email, mobNumber, firstName, lastName, userImage, currency, cntryCode);
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void handleDialogClose(String value) {
        switch (value) {
            case "resendOtp":
                attemptOTPLogin();
                break;
            case "success":
                finish();
                startActivity(new Intent(this, HomeActivity.class));
                break;
            default:
                Methods.showToast(mContext, "Error..");
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        countryCode = countryCodes.get(position);
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

