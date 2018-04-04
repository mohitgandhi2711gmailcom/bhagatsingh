package com.mohi.in.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.ui.adapter.TimelineProfileAddressPasswordAdapter;
import com.mohi.in.utils.Image_Picker;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.SessionStore;

import java.util.HashMap;

public class TimelinePasswordProfileAddressFragment extends Fragment implements View.OnClickListener {

    private TimelineProfileAddressPasswordAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CircularImageView editProfile_image;
    private TextView user_name;
    private Context mContext;
    private Image_Picker image_picker;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserData();
    }

    private void setUserData() {
        HashMap<String, String> map = SessionStore.getUserDetails(mContext, Common.userPrefName);
        if (map.get(SessionStore.USER_ID) == null || map.get(SessionStore.USER_ID).isEmpty()) {
            Methods.showToast(mContext, "User is not logged in");
        } else {
            String username = map.get(SessionStore.USER_FIRST_NAME) + " " + map.get(SessionStore.USER_LAST_NAME);
            user_name.setText(username);
            Glide.with(mContext).load(map.get(SessionStore.PROFILEPICTURE)).into(editProfile_image);
        }
    }

    private boolean checkLogin() {
        HashMap<String, String> map = SessionStore.getUserDetails(mContext, Common.userPrefName);
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
        }
    }
}