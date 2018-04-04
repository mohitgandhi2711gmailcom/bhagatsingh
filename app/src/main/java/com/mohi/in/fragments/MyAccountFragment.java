package com.mohi.in.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mohi.in.R;
import com.mohi.in.activities.ChangePasswordActivity;
import com.mohi.in.activities.EditProfileActivity;
import com.mohi.in.activities.LoginActivityNew;
import com.mohi.in.activities.ModifyYourAddressActivity;
import com.mohi.in.activities.SignupActivityNew;
import com.mohi.in.activities.WishListActivity;
import com.mohi.in.common.Common;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.CenturyGothicLightTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;


public class MyAccountFragment extends Fragment implements View.OnClickListener {


    private View root;
    private FrameLayout fl_userImage;
    private CircularImageView iv_usetImage;
    private CenturyGothicLightTextView tv_userName;
    private LinearLayout ll_EYAI, ll_CYP, ll_MYA, ll_MYWL, ll_pushNotification, ll_signout;
    private ToggleButton tb_pushNotification;
    private boolean bool_EYAI = true;


    private  View v_loginSignup;
    private ScrollView sv_myAccountView;
    private UbuntuMediumTextView tv_login, tv_signup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_my_account, container, false);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(getActivity());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void init() {

        fl_userImage = (FrameLayout) root.findViewById(R.id.MyAccount_UserImage);
        iv_usetImage = (CircularImageView) root.findViewById(R.id.MyAccount_Image);
        tv_userName = (CenturyGothicLightTextView) root.findViewById(R.id.MyAccount_UserName);

        ll_EYAI = (LinearLayout) root.findViewById(R.id.MyAccount_EYAI);
        ll_CYP = (LinearLayout) root.findViewById(R.id.MyAccount_CYP);
        ll_MYA = (LinearLayout) root.findViewById(R.id.MyAccount_MYA);
        ll_MYWL = (LinearLayout) root.findViewById(R.id.MyAccount_MYWL);
        ll_pushNotification = (LinearLayout) root.findViewById(R.id.MyAccount_PushNotification);
        ll_signout = (LinearLayout) root.findViewById(R.id.MyAccount_Signout);


        tb_pushNotification = (ToggleButton) root.findViewById(R.id.MyAccount_PushNotification_ToggleButton);


        sv_myAccountView = (ScrollView)root.findViewById(R.id.MyAccount_Parent);
        v_loginSignup = (View)root.findViewById(R.id.MyAccount_LoginSingup);

        tv_login = (UbuntuMediumTextView)root.findViewById(R.id.SigninSignup_popup_Login);
        tv_signup = (UbuntuMediumTextView)root.findViewById(R.id.SigninSignup_popup_Signup);


    }


    private void setValue() {


        if (SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID) == null) {

            //sv_myAccountView.setVisibility(View.GONE);
            v_loginSignup.setVisibility(View.VISIBLE);
            bool_EYAI = false;

        }else {

            //sv_myAccountView.setVisibility(View.VISIBLE);
            v_loginSignup.setVisibility(View.GONE);
            bool_EYAI = true;

        }




        Glide.with(getActivity())
                .load(SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.PROFILEPICTURE))
                .into(iv_usetImage);
        tv_userName.setText(SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_FIRST_NAME)+SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_LAST_NAME));


        ll_MYA.setOnClickListener(this);
        ll_EYAI.setOnClickListener(this);
        ll_CYP.setOnClickListener(this);
        ll_signout.setOnClickListener(this);

        ll_MYWL.setOnClickListener(this);
        ll_pushNotification.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);




    }


    @Override
    public void onResume() {
        super.onResume();
        setValue();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.MyAccount_MYA:

                if (bool_EYAI) {

                    bool_EYAI = false;

                    intent = new Intent(getActivity(), ModifyYourAddressActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                }
                break;

            case R.id.MyAccount_Signout:
                if (bool_EYAI) {

                    bool_EYAI = false;


                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.app_name)
                            .setMessage("Are you sure you want to logout of this app?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SessionStore.clear(getActivity(), Common.userPrefName);

                                    Intent intent = new Intent(getActivity(), LoginActivityNew.class);
                                    startActivity(intent);
                                    //ActivityCompat.finishAffinity(getActivity());

                                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }

                break;

            case R.id.MyAccount_CYP:

                if (bool_EYAI) {

                    bool_EYAI = false;

                    intent = new Intent(getActivity(), ChangePasswordActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                }
                break;

            case R.id.MyAccount_EYAI:

                if (bool_EYAI) {

                    bool_EYAI = false;
                    intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                }
                break;


            case R.id.MyAccount_MYWL:

                if (bool_EYAI) {

                    bool_EYAI = false;
                    intent = new Intent(getActivity(), WishListActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                }
                break;



            case R.id.SigninSignup_popup_Login:


                    intent = new Intent(getActivity(), LoginActivityNew.class);
                    intent.putExtra("From","MyAccount");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

                break;



            case R.id.SigninSignup_popup_Signup:


                    intent = new Intent(getActivity(), SignupActivityNew.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

                break;

        }

    }


}
