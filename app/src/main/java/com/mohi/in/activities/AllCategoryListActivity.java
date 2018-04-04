package com.mohi.in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.FeaturedCategoryModel;
import com.mohi.in.ui.adapter.CategoryListAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.widgets.UbuntuLightTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllCategoryListActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {

    private RecyclerView mCategoryRv;
    private Context mContext;
    private CategoryListAdapter mCategoryAdapter;
    private ArrayList<FeaturedCategoryModel> mCategoryList = new ArrayList<>();
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_filter;
    private MaterialRippleLayout mrl_rest;
    private UbuntuLightTextView tv_headerTilel;
    private boolean searchFlag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        getControls();
    }

    private void getControls() {
        mContext = AllCategoryListActivity.this;
        mCategoryRv = findViewById(R.id.category_rv);
        mrl_filter = findViewById(R.id.Header_FilterLayOut);
        mrl_rest = findViewById(R.id.Header_restLayOut);
        iv_menu = findViewById(R.id.Header_Menu);
        iv_search = findViewById(R.id.Header_Search);
        iv_filter = findViewById(R.id.Header_Filter);
        tv_headerTilel = findViewById(R.id.Header_Title);
        ll_titellogo = findViewById(R.id.Header_Titel_Logo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchFlag = true;
        setValue();
    }

    private void setValue() {
        mCategoryAdapter = new CategoryListAdapter(AllCategoryListActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mCategoryRv.setLayoutManager(mLayoutManager);
        mCategoryList = new ArrayList<>();
        mrl_rest.setVisibility(View.GONE);
        mrl_filter.setVisibility(View.GONE);
        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);
        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);
        tv_headerTilel.setText(R.string.categories);
        iv_menu.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        attemptGetFeaturedCategories();
    }

    // Get user information
    private void attemptGetFeaturedCategories() {
        try {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("limit", "");
            json.addProperty("page", "");
            json.addProperty("width", Common.getDeviceHeightWidth(AllCategoryListActivity.this).widthPixels);
            json.addProperty("height", getResources().getDimension(R.dimen.home_allcategories_row_height));
            ServerCalling.ServerCallingProductsApiPost(AllCategoryListActivity.this, "getCategories", json, this);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Header_Menu:
                finish();
                break;
            case R.id.Header_Search:
                if (searchFlag) {
                    searchFlag = false;
                    Intent intent = new Intent(AllCategoryListActivity.this, SearchActivity.class);
                    intent.putExtra("Search", "");
                    startActivity(intent);
                }
                break;
        }
    }

    private void setFeaturedCategories(JSONArray dataArray) {
        try {
            mCategoryList.clear();
            int dataArraylength = dataArray.length();
            for (int i = 0; i < dataArraylength; i++) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                mCategoryList.add(new FeaturedCategoryModel(dataJson.getString("cat_id"), dataJson.getString("name"), dataJson.getString("image")));
            }
            mCategoryAdapter.setList(mCategoryList);
            mCategoryRv.setAdapter(mCategoryAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WaitDialog.hideDialog();
    }

    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            if (strfFrom.trim().equalsIgnoreCase("getCategories")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.getJSONArray("data");
                    setFeaturedCategories(dataArray);
                } else {
                    Methods.showToast(AllCategoryListActivity.this, result.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
