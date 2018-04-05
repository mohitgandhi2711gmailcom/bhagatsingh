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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class ProfileFragment extends Fragment implements View.OnClickListener, ServerCallBack, AdapterView.OnItemSelectedListener {
    private EditText firstname_et;
    private EditText lastname_et;
    private EditText mail_et;
    private String country_code;
    private Spinner country_code_spinner;
    private EditText phone_number_et;
    private Context mContext;
    private Button update_profile_btn;
    private Uri mUri = null;
    private ArrayList<String> country_codes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        firstname_et = view.findViewById(R.id.first_name_et);
        lastname_et = view.findViewById(R.id.last_name_et);
        mail_et = view.findViewById(R.id.mail_et);
        country_code_spinner = view.findViewById(R.id.country_code_spinner);
        phone_number_et = view.findViewById(R.id.phone_number_et);
        update_profile_btn = view.findViewById(R.id.update_profile_btn);
        setValue();
    }

    private void setValue() {
        update_profile_btn.setOnClickListener(this);
        mail_et.setEnabled(false);
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, country_codes);
        country_code_spinner.setAdapter(dataAdapter);
        country_code_spinner.setOnItemSelectedListener(this);
        setUserInfo();
    }

    private void setUserInfo() {
        firstname_et.setText(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_FIRST_NAME));
        lastname_et.setText(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_LAST_NAME));
        phone_number_et.setText(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_MOBILENO));
        mail_et.setText(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_EMAIL));
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_profile_btn:
                attemptUpdateProfile();
                break;
        }
    }

    private void attemptUpdateProfile() {
        try {
            removeErrorFromAll();
            String firstname = firstname_et.getText().toString();
            String lastname = lastname_et.getText().toString();
            String mobileNo = phone_number_et.getText().toString();
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(firstname.trim())) {
                firstname_et.setError(getString(R.string.error_field_required));
                focusView = firstname_et;
                cancel = true;
            }
            if (TextUtils.isEmpty(lastname.trim())) {
                lastname_et.setError(getString(R.string.error_field_required));
                focusView = lastname_et;
                cancel = true;
            }
            if (country_code == null || TextUtils.isEmpty(country_code)) {
                Methods.showToast(mContext, "Please Select Country Code");
                cancel = true;
            }
            if (TextUtils.isEmpty(mobileNo.trim())) {
                phone_number_et.setError(getString(R.string.error_field_required));
                focusView = phone_number_et;
                cancel = true;
            } else if (!Methods.isPhoneNoValid(mobileNo.trim())) {
                phone_number_et.setError(getString(R.string.error_invalid_phoneno));
                focusView = phone_number_et;
                cancel = true;
            }
            if (cancel) {
                assert focusView != null;
                focusView.requestFocus();
            } else {
                WaitDialog.showDialog(mContext);
                File file = null;
                if (mUri != null) {

                    file = new File(mUri.getPath());
                }
                ServerCalling.ServerCallingUserApiImagePost(mContext, "updateProfile", this, firstname,lastname,SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_EMAIL),country_code,mobileNo, SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID),SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN), file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeErrorFromAll() {
        firstname_et.setError(null);
        lastname_et.setError(null);
        mail_et.setError(null);
        phone_number_et.setError(null);
    }

    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        try {
            if (strfFrom.trim().equalsIgnoreCase("updateProfile")) {
                if (result.getString("status").trim().equalsIgnoreCase("success")) {
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
                    SessionStore.save(mContext, Common.userPrefName, data.getString("user_id"), data.getString("token"), data.getString("email"), data.getString("mob_number"), data.getString("firstname"), data.getString("lastname"), data.getString("user_image"), strAddresId, strAddressName, strAddress, data.getString("currency"), data.optString("cntry_code"));
                    setUserInfo();
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        country_code = country_codes.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}

