package com.mohi.in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.JsonObject;

import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.model.SubCategoriesModel;
import com.mohi.in.ui.adapter.AllProductListAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuLightTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 12/10/17.
 */

public class SearchProductsListActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {

    private RecyclerView mCategoryRv;
    private GridLayoutManager mLayoutManager;
    private Context mContext;
    private AllProductListAdapter mCategoryAdapter =new AllProductListAdapter(SearchProductsListActivity.this);
    private ArrayList<SubCategoriesModel> mCategoryList;

    private MaterialRippleLayout mFilterLayout;

    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu, mrl_rest;
    private UbuntuLightTextView tv_headerTilel;

    Intent intent;
    private String strTitleName="";
    private String strCatId="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        intent = getIntent();

        if(intent.getStringExtra("Type")!=null){

            strTitleName = intent.getStringExtra("Type");

        }



        getControls();

    }

    private void getControls()
    {
        mContext = SearchProductsListActivity.this;
        mCategoryRv = (RecyclerView) findViewById(R.id.category_rv);



        //Header
        mrl_menu = (MaterialRippleLayout)findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout)findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout)findViewById(R.id.Header_FilterLayOut);
        mrl_rest = (MaterialRippleLayout)findViewById(R.id.Header_restLayOut);

        iv_menu = (ImageView)findViewById(R.id.Header_Menu);
        iv_search = (ImageView)findViewById(R.id.Header_Search);
        iv_filter = (ImageView)findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView)findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);


       // setValue();


    }

    private void setValue(){


        mLayoutManager = new GridLayoutManager(mContext, 2);
        mCategoryRv.setLayoutManager(mLayoutManager);
        mCategoryList=new ArrayList<>();



        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);



        tv_headerTilel.setVisibility(View.VISIBLE);


        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);


        if(!strTitleName.trim().equalsIgnoreCase("")){

            tv_headerTilel.setText(Methods.capitalizeWord(strTitleName.trim().replaceAll("_"," ")));

        }else {

            tv_headerTilel.setText("Products");
        }


        iv_menu.setOnClickListener(this);

        attemptGetFeaturedCategories();

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

    /*
  * set dummy featured category data
  * */
    private void setFeaturedCategories(JSONArray dataArray ) {

        try {
            mCategoryList.clear();
            int dataArraylength = dataArray.length();
            for (int i=0; i<dataArraylength; i++){
                JSONObject dataJson = dataArray.getJSONObject(i);
                mCategoryList.add(new SubCategoriesModel(dataJson.getString("product_id"), dataJson.getString("product_name"), dataJson.getString("image"), dataJson.getString("product_price"), dataJson.getInt("is_wishlist"), dataJson.getDouble("rating"), dataJson.getInt("is_add_to_cart")));
            }
        mCategoryAdapter.setList(mCategoryList);
        mCategoryRv.setAdapter(mCategoryAdapter);
        }catch (Exception e){


            Log.e("AllProductsListActivity", "Exception attemptTOGetUserInfo: "+e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.Header_Menu:
                    finish();
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        setValue();
    }

    // Get user information
    private void attemptGetFeaturedCategories(){
        try {

            JsonObject json = new JsonObject();
            json.addProperty("limit", "");
            json.addProperty("page", "");
            json.addProperty("user_id", SessionStore.getUserDetails(SearchProductsListActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(SearchProductsListActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.addProperty("width", getResources().getDimension(R.dimen.home_allproduct_row_width));
            json.addProperty("height", getResources().getDimension(R.dimen.home_allproduct_row_image_height));
            json.addProperty("type", strTitleName);

            ServerCalling.ServerCallingProductsApiPost(SearchProductsListActivity.this,  "getProduct", json, this);


        }catch (Exception e){


            Log.e("AllProductsListActivity", "Exception attemptTOGetUserInfo: "+e.getMessage());
        }

    }




    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {


            if(strfFrom.trim().equalsIgnoreCase("getProduct")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    JSONArray dataArray = result.getJSONArray("data");

                    setFeaturedCategories(dataArray);

                } else {

                    Methods.showToast(SearchProductsListActivity.this, result.getString("msg"));

                    Log.e("AllProductsListActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            }

        }catch (Exception e){


            Log.e("AllProductsListActivity", "Exception attemptTOGetUserInfo ServerCallBackSuccess: "+e.getMessage());
        }

    }
}
