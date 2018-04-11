package com.mohi.in.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.fragments.CartFragment;
import com.mohi.in.fragments.FavoriteFragment;
import com.mohi.in.fragments.HomeFragment;
import com.mohi.in.fragments.OrderFragmentNew;
import com.mohi.in.fragments.TimelinePasswordProfileAddressFragment;
import com.mohi.in.residing_menu.ResideMenu;
import com.mohi.in.residing_menu.ResideMenuItem;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.CartCountCallBack;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.widgets.UbuntuMediumTextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends FragmentActivity implements TabHost.OnTabChangeListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ServerCallBack, CartCountCallBack {

    public FragmentTabHost mTabHost;
    private Context mContext;
    //Residing Menu
    private ResideMenu resideMenu;
    private ResideMenuItem itemCategories;
    private ResideMenuItem itemHelp;
    private ResideMenuItem itemFaq;
    private ResideMenuItem itemAbout;
    private ResideMenuItem itemRateApp;
    LinearLayout drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        init();
    }

    private void init() {
        mContext = this;
        mTabHost = findViewById(R.id.tabhost);
        drawer_layout = findViewById(R.id.drawer_layout);
        //Set Margin acc to Soft Buttons
        if (ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );
            params.setMargins(0, 0, 0, getSoftButtonsBarHeight());
            drawer_layout.setLayoutParams(params);
        }
        setValue();
    }

    private void setValue() {
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.tab_home)).setIndicator(getTabIndicator(mTabHost.getContext(), R.drawable.ic_home_inactive)), HomeFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.tab_favourite)).setIndicator(getTabIndicator(mTabHost.getContext(), R.drawable.ic_love_inactive)), FavoriteFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.tab_cart)).setIndicator(getTabIndicator(mTabHost.getContext(), R.drawable.ic_cart_inactive)), CartFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.tab_transaction)).setIndicator(getTabIndicator(mTabHost.getContext(), R.drawable.ic_transaction_inactive)), OrderFragmentNew.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(getResources().getString(R.string.tab_my_Account)).setIndicator(getTabIndicator(mTabHost.getContext(), R.drawable.ic_user_inactive)), TimelinePasswordProfileAddressFragment.class, null);
        mTabHost.setOnTabChangedListener(this);

        //Set First Tab Active
        ImageView imageView = mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tab_icon);
        imageView.setImageResource(R.drawable.ic_home_active);
        setUpMenu();
    }

    private View getTabIndicator(Context context, int icon) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = view.findViewById(R.id.tab_icon);
        iv.setImageResource(icon);
        return view;
    }

    private void setUpMenu() {
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.navigation_menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemCategories = new ResideMenuItem(this, R.drawable.empty_shape, "CATEGORIES");
        itemHelp = new ResideMenuItem(this, R.drawable.empty_shape, "HELP");
        itemFaq = new ResideMenuItem(this, R.drawable.empty_shape, "FAQ");
        itemAbout = new ResideMenuItem(this, R.drawable.empty_shape, "ABOUT");
        itemRateApp = new ResideMenuItem(this, R.drawable.empty_shape, "RATE THIS APP");
        ResideMenuItem itemExtra1 = new ResideMenuItem(this, R.drawable.empty_shape, "");
        ResideMenuItem itemExtra2 = new ResideMenuItem(this, R.drawable.empty_shape, "");
        ResideMenuItem itemExtra3 = new ResideMenuItem(this, R.drawable.empty_shape, "");
        ResideMenuItem itemExtra4 = new ResideMenuItem(this, R.drawable.empty_shape, "");

        itemCategories.setOnClickListener(this);
        itemHelp.setOnClickListener(this);
        itemFaq.setOnClickListener(this);
        itemAbout.setOnClickListener(this);
        itemRateApp.setOnClickListener(this);

        resideMenu.addMenuItem(itemCategories, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHelp, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFaq, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRateApp, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemExtra1, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemExtra2, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemExtra3, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemExtra4, ResideMenu.DIRECTION_LEFT);

        //disable left right swipe menu open
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @SuppressLint("NewApi")
    private int getSoftButtonsBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        attemptTOGetUserInfo();
    }

    // Get user information
    public void attemptTOGetUserInfo() {
        try {
            if (isUserLogin()) {
                WaitDialog.showDialog(this);
                JsonObject json = new JsonObject();
                json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                ServerCalling.ServerCallingProductsApiPost(mContext, "viewCartCount", json, this);
                //ServerCalling.ServerCallingUserApiPost(HomeActivity.this, "getProfile", json, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUserLogin() {
        String user_id = SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID);
        String token = SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN);
        return !(TextUtils.isEmpty(user_id) && TextUtils.isEmpty(token) && user_id == null && token == null);
    }

    @Override
    public void onTabChanged(String s) {
        ArrayList<Integer> al = new ArrayList<>();
        al.add(R.drawable.ic_home_inactive);
        al.add(R.drawable.ic_love_inactive);
        al.add(R.drawable.ic_cart_inactive);
        al.add(R.drawable.ic_transaction_inactive);
        al.add(R.drawable.ic_user_inactive);
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            ImageView imageView = mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_icon);
            imageView.setImageResource(al.get(i));//for Selected Tab
        }
        switch (s) {
            case "Home":
                ImageView imageView0 = mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tab_icon);
                imageView0.setImageResource(R.drawable.ic_home_active);
                break;
            case "Favourite":
                ImageView imageView1 = mTabHost.getTabWidget().getChildAt(1).findViewById(R.id.tab_icon);
                imageView1.setImageResource(R.drawable.ic_love_active);
                break;
            case "Cart":
                ImageView imageView2 = mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.tab_icon);
                imageView2.setImageResource(R.drawable.ic_cart_active);
                break;
            case "Transaction":
                ImageView imageView3 = mTabHost.getTabWidget().getChildAt(3).findViewById(R.id.tab_icon);
                imageView3.setImageResource(R.drawable.ic_transaction_active);
                break;
            case "My Account":
                ImageView imageView4 = mTabHost.getTabWidget().getChildAt(4).findViewById(R.id.tab_icon);
                imageView4.setImageResource(R.drawable.ic_user_active);
                if (!isUserLogin()) {
                    showLoginPopupDailog();
                }
                break;
            default:
                ImageView imageViewDefault = mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tab_icon);
                imageViewDefault.setImageResource(R.drawable.ic_home_active);
                break;
        }
//        attemptTOGetUserInfo();
    }

    private void showLoginPopupDailog() {
        UbuntuMediumTextView SigninSignup_popup_Login, SigninSignup_popup_Signup;
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.login_signup_popup, null);
        dialog.setContentView(view);
        SigninSignup_popup_Login = view.findViewById(R.id.SigninSignup_popup_Login);
        SigninSignup_popup_Signup = view.findViewById(R.id.SigninSignup_popup_Signup);
        SigninSignup_popup_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent_login = new Intent(mContext, LoginActivityNew.class);
                startActivity(intent_login);
            }
        });
        SigninSignup_popup_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, SignupActivityNew.class);
                startActivity(intent);
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }

    // onNavigationItemSelected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_category:
                mTabHost.setCurrentTab(0);
                break;
            case R.id.nav_faq:
                intent = new Intent(mContext, ActivityFAQ.class);
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                break;
            case R.id.nav_about:
                intent = new Intent(mContext, ActivityAboutUs.class);
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                break;
            case R.id.nav_rate_app:
                Toast.makeText(mContext, "Please Rate the App", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_help:
                intent = new Intent(mContext, ActivityHelp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                break;
        }
        return true;
    }

    //Click Listener
    @Override
    public void onClick(View view) {
        Intent intent;

        if (view == itemCategories) {
            mTabHost.setCurrentTab(0);
            closeResideMenu();
        }
        if (view == itemHelp) {
            intent = new Intent(mContext, ActivityHelp.class);
            startActivity(intent);
            overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
            closeResideMenu();
        }
        if (view == itemFaq) {
            intent = new Intent(mContext, ActivityFAQ.class);
            startActivity(intent);
            overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
            closeResideMenu();
        }
        if (view == itemAbout) {
            intent = new Intent(mContext, ActivityAboutUs.class);
            startActivity(intent);
            overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
            closeResideMenu();
        }
        if (view == itemRateApp) {
            Toast.makeText(mContext, "Please Rate the App", Toast.LENGTH_SHORT).show();
            closeResideMenu();
        }

        switch (view.getId()) {
            case R.id.Header_Menu:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
        }
    }

    private void closeResideMenu() {
        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
            if (ViewConfiguration.get(mContext).hasPermanentMenuKey()) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                params.setMargins(0, 0, 0, getSoftButtonsBarHeight());
                drawer_layout.setLayoutParams(params);
            }
        }
    };

    @Override
    public void CartCountCallBackSuccess() {
        attemptTOGetUserInfo();
    }

    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        switch (strfFrom) {
            case "viewCartCount": {
                if (result.optString("status").trim().equalsIgnoreCase("success")) {
                    JSONObject jdata = result.optJSONObject("data");
                    setCartCount(jdata.optString("total_product"));
                } else {
                    Methods.showToast(mContext, result.optString("msg"));
                }
                break;
            }

            case "getProfile": {
                if (result.optString("status").trim().equalsIgnoreCase("success")) {
                    JSONObject data = result.optJSONObject("data");
                    String user_id = data.optString("user_id");
                    String token = data.optString("token");
                    String email = data.optString("email");
                    String mob_number = data.optString("mob_number");
                    String firstName = data.optString("firstname");
                    String lastName = data.optString("lastname");
                    String user_image = data.optString("user_image");
                    String currency = data.optString("currency");
                    String cntry_code = data.optString("cntry_code");
                    SessionStore.saveUserDetails(mContext, Common.userPrefName, user_id, token, email, mob_number, firstName, lastName, user_image, currency, cntry_code);
                } else {
                    Methods.showToast(mContext, result.optString("msg"));
                }
                break;
            }
        }
    }

    public void setCartCount(String value) {
        if (Integer.parseInt(value) == 0) {
            ((TextView) mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.txt_cart_count)).setText(" " + Methods.twoDigitFormat(value) + " ");
            mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.txt_cart_count).setVisibility(View.GONE);
        } else {
            ((TextView) mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.txt_cart_count)).setText(" " + Methods.twoDigitFormat(value) + " ");
            mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.txt_cart_count).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (resideMenu.isOpened()) {
            closeResideMenu();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("Are you sure you want to quit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
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

    public void setParticulatTab(int position)
    {
        mTabHost.setCurrentTab(position);
    }
}