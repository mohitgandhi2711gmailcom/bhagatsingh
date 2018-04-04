package com.mohi.in.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuLightTextView;
import com.mohi.in.widgets.UbuntuRegularButton;
import com.mohi.in.widgets.UbuntuRegularEditText;
import com.mohi.in.widgets.UbuntuRegularTextView;

import org.json.JSONObject;

public class WriteReviewActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {

    private ImageView iv_image;
    private UbuntuRegularTextView tv_name;
    private UbuntuRegularEditText et_review;
    private UbuntuRegularButton but_submit;
    private RatingBar rb_rating;

    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;



    private Intent intent;
    private String strProductId="", strProductName="", strProductImage="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        intent = getIntent();
        if(intent.getStringExtra("ProductId")!=null){

            strProductId = intent.getStringExtra("ProductId");
            strProductName = intent.getStringExtra("ProductName");
            strProductImage = intent.getStringExtra("ProductImage");

        }

        init();
    }

    //************** init method for maping ******************************************************
    private void init(){

        iv_image = (ImageView)findViewById(R.id.WriteReview_Image);

        tv_name = (UbuntuRegularTextView)findViewById(R.id.WriteReview_Name);

        et_review = (UbuntuRegularEditText)findViewById(R.id.WriteReview_Review);

        but_submit = (UbuntuRegularButton)findViewById(R.id.WriteReview_Submit);

        rb_rating = (RatingBar)findViewById(R.id.WriteReview_Rating);




        //Header
        mrl_menu = (MaterialRippleLayout)findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout)findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout)findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView)findViewById(R.id.Header_Menu);
        iv_search = (ImageView)findViewById(R.id.Header_Search);
        iv_filter = (ImageView)findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView)findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);



        setValue();
    }


    //************** setValue method for set value ******************************************************
    private void setValue(){


        tv_name.setText(strProductName);

        Glide.with(WriteReviewActivity.this).load(strProductImage).into(iv_image);

        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.your_review);

        iv_menu.setOnClickListener(this);
        but_submit.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.WriteReview_Submit:
                attemptSendReview();

                break;

            case R.id.Header_Menu:

                onBackPressed();
                break;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }


    private void attemptSendReview() {


        // Reset errors.
        et_review.setError(null);

        // Store values at the time of the login attempt.
        String review = et_review.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(review)) {
            et_review.setError(getString(R.string.writereview));
            focusView = et_review;
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
            json.addProperty("user_id", SessionStore.getUserDetails(WriteReviewActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(WriteReviewActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.addProperty("review", review);
            json.addProperty("rating", rb_rating.getRating());
            json.addProperty("name", SessionStore.getUserDetails(WriteReviewActivity.this, Common.userPrefName).get(SessionStore.USER_NAME));
            json.addProperty("product_id", strProductId);



            ServerCalling.ServerCallingProductsApiPost(WriteReviewActivity.this, "addReview", json, this);


        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject jobj, String strfFrom) {
        try {
            WaitDialog.hideDialog();

            if (jobj.getString("status").trim().equalsIgnoreCase("1")) {

                Methods.showToast(WriteReviewActivity.this, jobj.getString("msg"));
                onBackPressed();

            } else {

                Methods.showToast(WriteReviewActivity.this, jobj.getString("msg"));
                Log.e("WriteReviewActivity", "login User log: " + jobj.getString("msg"));
            }


        } catch (Exception ee) {

            Log.e("WriteReviewActivity", "Login User Exception: " + ee.getMessage());
        }
    }
}
