package com.mohi.in.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONObject;

import java.io.File;


public class ProfileFragment extends Fragment implements View.OnClickListener, ServerCallBack {
    private EditText your_name_et;
    private EditText mail_et;
    private EditText mobile_number_et;
    private Context mContext;
    private ImageView back_btn_iv;
    private Button update_profile_btn;
    private Uri mUri = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        your_name_et = view.findViewById(R.id.your_name_et);
        mail_et = view.findViewById(R.id.mail_et);
        mobile_number_et = view.findViewById(R.id.mobile_number_et);
        back_btn_iv = view.findViewById(R.id.back_btn_iv);
        update_profile_btn = view.findViewById(R.id.update_profile_btn);
        update_profile_btn.setOnClickListener(this);
        back_btn_iv.setOnClickListener(this);
        setUserInfo();
    }

    private void setUserInfo() {
        your_name_et.setText(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_NAME));
        /*if(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_NAME).length()>0)
        {
            your_name_et.setSelection(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_NAME).length());
        }*/
        mobile_number_et.setText(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_MOBILENO));
        /*if(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_MOBILENO).length()>0)
        {
            mobile_number_et.setSelection(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_MOBILENO).length());
        }*/
        mail_et.setText(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_EMAIL));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn_iv:
                break;
            case R.id.update_profile_btn:
                attemptUpdateProfile();
                break;
        }
    }

    private void attemptUpdateProfile() {
        try {
            your_name_et.setError(null);
            mobile_number_et.setError(null);
            String userName = your_name_et.getText().toString();
            String mobileNo = mobile_number_et.getText().toString();
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(userName.trim())) {
                your_name_et.setError(getString(R.string.error_field_required));
                focusView = your_name_et;
                cancel = true;
            }
            if (TextUtils.isEmpty(mobileNo.trim())) {
                mobile_number_et.setError(getString(R.string.error_field_required));
                focusView = mobile_number_et;
                cancel = true;
            } else if (!Methods.isPhoneNoValid(mobileNo.trim())) {
                mobile_number_et.setError(getString(R.string.error_invalid_phoneno));
                focusView = mobile_number_et;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
            } else {
                WaitDialog.showDialog(mContext);
                File file = null;
                if (mUri != null) {

                    file = new File(mUri.getPath());
                }
                ServerCalling.ServerCallingUserApiImagePost(mContext, "updateProfile", this, SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID), SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN), userName, mobileNo, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        try {
            if (strfFrom.trim().equalsIgnoreCase("updateProfile")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    JSONObject data = result.getJSONObject("data");
                    String strAddress = "", strAddressName = "", strAddresId = "";
                    if (!data.isNull("address")) {
                        JSONObject addressData = data.getJSONObject("address");
                        String str = addressData.getString("flat_no") + ", " + addressData.getString("street");
                        if (!addressData.getString("landmark").equalsIgnoreCase("")) {
                            str = str + ", " + addressData.getString("landmark") + ", " + addressData.getString("city") + ", " + addressData.getString("state") + ", " + addressData.getString("postcode");
                        } else {
                            str = str + ", " + addressData.getString("city") + ", " + addressData.getString("state") + ", " + addressData.getString("postcode");
                        }
                        strAddressName = addressData.getString("name");
                        strAddresId = addressData.getString("address_id");
                        strAddress = str;
                    }
                    //Country Code may recive in future
                    String countryCode="";
                    SessionStore.save(mContext, Common.userPrefName, data.getString("user_id"), data.getString("token"), data.getString("email"), data.getString("mobile_number"), data.getString("name"), data.getString("user_image"), strAddresId, strAddressName, strAddress, data.getString("currency"),countryCode);
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

