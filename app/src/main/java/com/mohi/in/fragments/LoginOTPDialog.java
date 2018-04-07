package com.mohi.in.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.listener.OtpDialogDismissListener;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONObject;

public class LoginOTPDialog extends Dialog implements android.view.View.OnClickListener, ServerCallBack {

    private Context mContext;
    private EditText otp_et;
    private Button login_otp_btn;
    String country_code;
    String phone_no;
    OtpDialogDismissListener listener;

    public LoginOTPDialog(@NonNull Context mContext, String country_code, String phone_no) {
        super(mContext);
        this.mContext = mContext;
        this.listener = (OtpDialogDismissListener) mContext;
        this.country_code = country_code;
        this.phone_no = phone_no;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_otp_dialog);
        init();
    }

    private void init() {
        otp_et = findViewById(R.id.otp_et);
        login_otp_btn = findViewById(R.id.login_otp_btn);
        setValue();
    }

    private void setValue() {
        login_otp_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_otp_btn:
                attemptLoginWithOPT();
                break;
        }

    }

    private void attemptLoginWithOPT() {
        otp_et.setError(null);
        String otp = otp_et.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(otp)) {
            otp_et.setError(mContext.getString(R.string.error_field_required));
            focusView = otp_et;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(mContext);
            JsonObject json = new JsonObject();
            json.addProperty("cntry_code", country_code);
            json.addProperty("mob_number", phone_no);
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
                SessionStore.saveUserDetails(mContext, Common.userPrefName, user_id, token, email, mob_number, firstName, lastName, user_image, currency, cntry_code, address_id, telephone, street_1, street_2, city, region, postcode, country_id);
                dismiss();
                listener.handleDialogClose();
            } else {
                Methods.showToast(mContext, result.optString("msg"));
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}