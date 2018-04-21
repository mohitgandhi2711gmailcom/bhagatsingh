package com.mohi.in.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.listeners.OtpDialogDismissListener;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONObject;

public class LoginOTPDialog extends Dialog implements android.view.View.OnClickListener, ServerCallBack {

    private Context mContext;
    private EditText otpEditText;
    private Button loginOtpButton;
    private TextView resendTextView;
    private ImageView crossImageView;
    private String countryCode;
    private String phoneNumber;
    private OtpDialogDismissListener listener;

    public LoginOTPDialog(@NonNull Context mContext, String countryCode, String phoneNumber) {
        super(mContext);
        this.mContext = mContext;
        this.listener = (OtpDialogDismissListener) mContext;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
    }

    private void startTimer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resendTextView.setVisibility(View.VISIBLE);
            }
        }, 30000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_otp_dialog);
        init();
    }

    private void init() {
        otpEditText = findViewById(R.id.otp_et);
        loginOtpButton = findViewById(R.id.login_otp_btn);
        resendTextView = findViewById(R.id.resend_tv);
        crossImageView = findViewById(R.id.cross_iv);
        startTimer();
        setValue();
    }

    private void setValue() {
        resendTextView.setVisibility(View.GONE);
        resendTextView.setOnClickListener(this);
        loginOtpButton.setOnClickListener(this);
        crossImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_otp_btn:
                attemptLoginWithOPT();
                break;
            case R.id.resend_tv:
                listener.handleDialogClose("resendOtp");
                dismiss();
                break;
            case R.id.cross_iv:
                dismiss();
                break;
            default:
                Methods.showToast(mContext, "Error");
        }

    }

    private void attemptLoginWithOPT() {
        otpEditText.setError(null);
        String otp = otpEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(otp)) {
            otpEditText.setError(mContext.getString(R.string.error_field_required));
            focusView = otpEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(mContext);
            JsonObject json = new JsonObject();
            json.addProperty("cntry_code", countryCode);
            json.addProperty("mob_number", phoneNumber);
            json.addProperty("otp", otp);
            ServerCalling.ServerCallingUserApiPost(mContext, "login", json, this);
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            WaitDialog.hideDialog();
            if (result.getString("status").trim().equalsIgnoreCase("success")) {
                JSONObject data = result.getJSONObject("data");
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
                dismiss();
                listener.handleDialogClose("success");
            } else {
                Methods.showToast(mContext, result.optString("msg"));
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}