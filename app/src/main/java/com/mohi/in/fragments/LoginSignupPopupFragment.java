package com.mohi.in.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohi.in.R;
import com.mohi.in.activities.LoginActivityNew;
import com.mohi.in.activities.SignupActivityNew;
import com.mohi.in.widgets.UbuntuMediumTextView;

public class LoginSignupPopupFragment extends Fragment implements View.OnClickListener
{
        private UbuntuMediumTextView SigninSignup_popup_Login,SigninSignup_popup_Signup;
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.login_signup_popup,container,false);
        init(view);
        return view;
    }


    private void init(View view)
    {
        mContext=getActivity();
        SigninSignup_popup_Login=view.findViewById(R.id.SigninSignup_popup_Login);
        SigninSignup_popup_Signup=view.findViewById(R.id.SigninSignup_popup_Signup);
        SigninSignup_popup_Login.setOnClickListener(this);
        SigninSignup_popup_Signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.SigninSignup_popup_Login:
                Intent intent_login = new Intent(mContext, LoginActivityNew.class);
                startActivity(intent_login);
                break;
            case R.id.SigninSignup_popup_Signup:
                Intent intent = new Intent(mContext, SignupActivityNew.class);
                startActivity(intent);
                break;
        }

    }
}
