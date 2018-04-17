package com.mohi.in.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mohi.in.R;
import com.mohi.in.activities.LoginActivityNew;
import com.mohi.in.activities.SignupActivityNew;
import com.mohi.in.common.Common;
import com.mohi.in.ui.adapter.TimelineProfileAddressPasswordAdapter;
import com.mohi.in.utils.Image_Picker;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuMediumTextView;

import java.util.HashMap;

public class TimelinePasswordProfileAddressFragment extends Fragment implements View.OnClickListener {

    private TimelineProfileAddressPasswordAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CircularImageView editProfile_image;
    private TextView user_name;
    private Context mContext;
    private Image_Picker image_picker;
    private View MyAccount_LoginSingup;
    private UbuntuMediumTextView SigninSignup_popup_Login;
    private UbuntuMediumTextView SigninSignup_popup_Signup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_timeline_profile_address_password, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        image_picker = new Image_Picker(mContext);
        editProfile_image = view.findViewById(R.id.EditProfile_Image);
        user_name = view.findViewById(R.id.user_name);
        viewPager = view.findViewById(R.id.pager_timleline);
        adapter = new TimelineProfileAddressPasswordAdapter(getChildFragmentManager());
        tabLayout = view.findViewById(R.id.tabs);
        MyAccount_LoginSingup = view.findViewById(R.id.MyAccount_LoginSingup);
        SigninSignup_popup_Login = view.findViewById(R.id.SigninSignup_popup_Login);
        SigninSignup_popup_Signup = view.findViewById(R.id.SigninSignup_popup_Signup);

        setValue();
    }

    private void setValue() {
        adapter.addFragment(new TimelineFragment(), "TIMELINE");
        adapter.addFragment(new ProfileFragment(), "PROFILE");
        adapter.addFragment(new FragmentEditAddress(), "SHIPPING ADDRESS");
        adapter.addFragment(new ChangePasswordFragment(), "CHANGE PASSWORD");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        editProfile_image.setOnClickListener(this);
        SigninSignup_popup_Login.setOnClickListener(this);
        SigninSignup_popup_Signup.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isUserLogin()) {
            MyAccount_LoginSingup.setVisibility(View.VISIBLE);
            //showLoginPopupDailog();
        } else {
            MyAccount_LoginSingup.setVisibility(View.GONE);
        }
        setUserData();
    }


    /*
    * Methos to show Login Signup Popup
    * */
    private void showLoginPopupDailog() {
        UbuntuMediumTextView popUpLogin;
        UbuntuMediumTextView popUpSignup;
        final Dialog dialog = new Dialog(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.login_signup_popup, null);
        dialog.setContentView(view);
        popUpLogin = view.findViewById(R.id.SigninSignup_popup_Login);
        popUpSignup = view.findViewById(R.id.SigninSignup_popup_Signup);
        popUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent logInIntent = new Intent(mContext, LoginActivityNew.class);
                startActivity(logInIntent);
            }
        });
        popUpSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent signUpIntent = new Intent(mContext, SignupActivityNew.class);
                startActivity(signUpIntent);
            }
        });
        dialog.show();
        dialog.setCancelable(true);
    }

    public boolean isUserLogin() {
        String userId = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID);
        String token = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN);
        return !(TextUtils.isEmpty(userId) && TextUtils.isEmpty(token) && userId == null && token == null);
    }

    private void setUserData() {
        HashMap<String, String> map = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME);
        if (map.get(SessionStore.USER_ID) == null || map.get(SessionStore.USER_ID).isEmpty()) {
            Methods.showToast(mContext, "User is not logged in");
        } else {
            String username = map.get(SessionStore.USER_FIRST_NAME) + " " + map.get(SessionStore.USER_LAST_NAME);
            user_name.setText(username);
            Glide.with(mContext).load(map.get(SessionStore.PROFILEPICTURE)).into(editProfile_image);
        }
    }

    private boolean checkLogin() {
        HashMap<String, String> map = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME);
        return !(map.get(SessionStore.USER_ID) == null || map.get(SessionStore.USER_ID).isEmpty());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.EditProfile_Image:
                if (checkLogin()) {
                    if (image_picker.hasPermissions(mContext, Image_Picker.PERMISSIONS)) {
                        image_picker.imageOptions();
                    } else {
                        image_picker.startDialog();
                    }
                }
                break;

            case R.id.SigninSignup_popup_Login:
                MyAccount_LoginSingup.setVisibility(View.GONE);
                Intent logInIntent = new Intent(mContext, LoginActivityNew.class);
                startActivity(logInIntent);
                break;

            case R.id.SigninSignup_popup_Signup:
                MyAccount_LoginSingup.setVisibility(View.GONE);
                Intent signUpIntent = new Intent(mContext, SignupActivityNew.class);
                startActivity(signUpIntent);
                break;

            default:
                Methods.showToast(mContext, "Error");

        }
    }
}