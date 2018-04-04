package com.mohi.in.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;

import org.json.JSONObject;

public class ForGotPasswordActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {

    private EditText et_emailAddress;
    private Button but_resetPassword;
    private ImageView back_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_new);
        init();
    }

    private void init() {
        et_emailAddress = findViewById(R.id.mail_phonenumber_et);
        but_resetPassword = findViewById(R.id.reset_pass_btn);
        back_iv = findViewById(R.id.back_iv);
        setValue();
    }

    private void setValue() {
        back_iv.setOnClickListener(this);
        but_resetPassword.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_pass_btn:
                attemptForgotPassword();
                break;

            case R.id.back_iv:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = new Intent(ForGotPasswordActivity.this, LoginActivityNew.class);
        //startActivity(intent);
        //overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_down);
        //finish();
    }

    private void attemptForgotPassword() {
        // Reset errors.
        et_emailAddress.setError(null);

        String email = et_emailAddress.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            et_emailAddress.setError(getString(R.string.error_field_required));
            focusView = et_emailAddress;
            cancel = true;
        } else if (!Methods.isValidEmail(email)) {
            et_emailAddress.setError(getString(R.string.error_invalid_email));
            focusView = et_emailAddress;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("email", email);
            ServerCalling.ServerCallingUserApiPost(ForGotPasswordActivity.this, "forgot", json, this);
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject jobj, String strfFrom) {
        try {
            WaitDialog.hideDialog();
            if (jobj.getString("status").trim().equalsIgnoreCase("1")) {
                Methods.showToast(ForGotPasswordActivity.this, jobj.getString("msg"));
                onBackPressed();
            } else {
                Methods.showToast(ForGotPasswordActivity.this, jobj.getString("msg"));
                Log.e("Login", "login User log: " + jobj.getString("msg"));
            }
        } catch (Exception ee) {
            Log.e("Login", "Login User Exception: " + ee.getMessage());
        }
    }
}