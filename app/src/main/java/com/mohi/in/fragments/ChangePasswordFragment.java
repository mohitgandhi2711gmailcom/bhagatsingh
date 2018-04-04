package com.mohi.in.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONObject;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener, ServerCallBack {
    private EditText current_pass_et;
    private EditText new_pass_et;
    private EditText confirm_pass_et;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        current_pass_et = view.findViewById(R.id.current_pass_et);
        new_pass_et = view.findViewById(R.id.new_pass_et);
        confirm_pass_et = view.findViewById(R.id.confirm_pass_et);
        ImageView back_btn_iv = view.findViewById(R.id.back_btn_iv);
        Button update_pass_btn = view.findViewById(R.id.update_pass_btn);
        back_btn_iv.setOnClickListener(this);
        update_pass_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn_iv:
                break;

            case R.id.update_pass_btn:
                attemptChangePassword();
                break;
        }
    }

    private void attemptChangePassword() {
        current_pass_et.setError(null);
        new_pass_et.setError(null);
        confirm_pass_et.setError(null);
        String oldPassword = current_pass_et.getText().toString();
        String newPassword = new_pass_et.getText().toString();
        String confirmPassword = confirm_pass_et.getText().toString();
        boolean cancel = false;
        View focusView = null;

        //Confirm Password
        if (TextUtils.isEmpty(confirmPassword)) {
            confirm_pass_et.setError(getString(R.string.error_field_required));
            focusView = confirm_pass_et;
            cancel = true;
        } else if (!confirmPassword.trim().equals(newPassword.trim())) {
            confirm_pass_et.setError(getString(R.string.error_notmatch_password));
            focusView = confirm_pass_et;
            cancel = true;
        }

        //New Password
        if (TextUtils.isEmpty(newPassword)) {
            new_pass_et.setError(getString(R.string.error_field_required));
            focusView = new_pass_et;
            cancel = true;
        } else if (!Methods.isPasswordValid(newPassword)) {
            new_pass_et.setError(getString(R.string.error_invalid_password));
            focusView = new_pass_et;
            cancel = true;
        }

        //Current Password
        if (TextUtils.isEmpty(oldPassword)) {
            current_pass_et.setError(getString(R.string.error_field_required));
            focusView = current_pass_et;
            cancel = true;
        }

        //If true Means Error else Login Attempt
        if (cancel) {
            focusView.requestFocus();
        } else {
            WaitDialog.showDialog(mContext);
            JsonObject json = new JsonObject();
            json.addProperty("old_password", oldPassword);
            json.addProperty("new_password", newPassword);
            json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
            ServerCalling.ServerCallingUserApiPost(mContext, "changePassword", json, this);
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        try {
            if (result.getString("status").trim().equalsIgnoreCase("1")) {
                Methods.showToast(mContext, result.getString("msg"));
            } else {
                Methods.showToast(mContext, result.getString("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}