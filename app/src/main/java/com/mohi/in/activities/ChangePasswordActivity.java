package com.mohi.in.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.mohi.in.widgets.CenturyGothicRegularEditText;
import com.mohi.in.widgets.UbuntuLightTextView;
import com.mohi.in.widgets.UbuntuRegularButton;

import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {


    private CenturyGothicRegularEditText et_oldPassword, et_newPassword, et_confirmPassword;
    private UbuntuRegularButton but_save;
    private String TAG="ChangePasswordActivity";


    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter, iv_rest;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        init();
    }


    private void init(){


        et_oldPassword = (CenturyGothicRegularEditText)findViewById(R.id.ChangePassword_OldPassword);
        et_newPassword = (CenturyGothicRegularEditText)findViewById(R.id.ChangePassword_Password);
        et_confirmPassword = (CenturyGothicRegularEditText)findViewById(R.id.ChangePassword_ConfirmPassword);

        but_save = (UbuntuRegularButton)findViewById(R.id.ChangePassword_Save);


        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);
        iv_rest = (ImageView) findViewById(R.id.Header_rest);
        tv_headerTilel = (UbuntuLightTextView) findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);


        setValue();
    }


    private void setValue(){

        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.GONE);
        iv_rest.setVisibility(View.GONE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.change_your_password);


        iv_menu.setOnClickListener(this);
        but_save.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setValue();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.Header_Menu:

                onBackPressed();

                break;

            case R.id.ChangePassword_Save:

                attemptChangePassword();

                break;
        }
    }


    private void attemptChangePassword() {

        et_oldPassword.setError(null);
        et_newPassword.setError(null);
        et_confirmPassword.setError(null);

        String oldPassord = et_oldPassword.getText().toString();
        String newPassword = et_newPassword.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;



        if (TextUtils.isEmpty(confirmPassword)) {
            et_confirmPassword.setError(getString(R.string.error_field_required));
            focusView = et_confirmPassword;
            cancel = true;
        }else if(!confirmPassword.trim().equals(newPassword.trim())){

            et_confirmPassword.setError(getString(R.string.error_notmatch_password));
            focusView = et_confirmPassword;
            cancel = true;
        }


        if (TextUtils.isEmpty(newPassword)) {
            et_newPassword.setError(getString(R.string.error_field_required));
            focusView = et_newPassword;
            cancel = true;
        } else if (!Methods.isPasswordValid(newPassword)) {
            et_newPassword.setError(getString(R.string.error_invalid_password));
            focusView = et_newPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(oldPassord)) {
            et_oldPassword.setError(getString(R.string.error_field_required));
            focusView = et_oldPassword;
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
            json.addProperty("old_password", oldPassord);
            json.addProperty("new_password", newPassword);

            json.addProperty("user_id", SessionStore.getUserDetails(ChangePasswordActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(ChangePasswordActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));


            ServerCalling.ServerCallingUserApiPost(ChangePasswordActivity.this, "changePassword", json, this);



        }

    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        try {
            if (result.getString("status").trim().equalsIgnoreCase("1")) {

                Methods.showToast(ChangePasswordActivity.this, result.getString("msg"));

                onBackPressed();

            } else {

                Methods.showToast(ChangePasswordActivity.this, result.getString("msg"));

                Log.e(TAG, "Signup User log: " +result.getString("msg"));
            }
        } catch (Exception ee) {

            Log.e(TAG, "Signup User Exception: " +ee.getMessage());
        }
    }
}
