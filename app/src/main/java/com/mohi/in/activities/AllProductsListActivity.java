package com.mohi.in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.common.GridSpacingItemDecoration;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.SubCategoriesModel;
import com.mohi.in.ui.adapter.AllProductListAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.PaginationScrollListener;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.ServerCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllProductsListActivity extends AppCompatActivity implements ServerCallBack {

    private RecyclerView mCategoryRv;
    private Context mContext;
    private AllProductListAdapter mCategoryAdapter;
    private ArrayList<SubCategoriesModel> mCategoryList;
    private String strSearch = "";
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static final int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        Intent intent = getIntent();
        if (intent.getStringExtra("action") != null) {
            strSearch = intent.getStringExtra("action");
        }
        init();
    }

    private void init() {
        mContext = this;
        mCategoryRv = findViewById(R.id.category_rv);
        setValue();
    }

    private void setValue() {
        mCategoryAdapter = new AllProductListAdapter(mContext);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        mCategoryRv.setLayoutManager(mLayoutManager);
        mCategoryRv.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        mCategoryList = new ArrayList<>();
        mCategoryRv.setItemAnimator(new DefaultItemAnimator());
        mCategoryRv.setAdapter(mCategoryAdapter);
        attemptGetCategories();
        mCategoryRv.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        attemptGetCategories();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void attemptGetCategories() {
        if (strSearch != null && !TextUtils.isEmpty(strSearch)) {
            try {
                if (currentPage == 1) {
                    WaitDialog.showDialog(mContext);
                }
                JsonObject json = new JsonObject();
                json.addProperty("limit", TOTAL_PAGES);
                json.addProperty("page", currentPage);
                json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                json.addProperty("width", getResources().getDimension(R.dimen.home_allproduct_row_width));
                json.addProperty("height", getResources().getDimension(R.dimen.home_allproduct_row_image_height));
                json.addProperty("type", "");
                json.addProperty("cat_id", "");
                json.addProperty("search", strSearch);
                json.addProperty("sort", "");
                ServerCalling.ServerCallingProductsApiPost(mContext, "getProduct", json, this);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        } else
        {
            Methods.showToast(mContext, "No data Found");
            finish();
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            if (strfFrom.trim().equalsIgnoreCase("getProduct")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.getJSONArray("data");
                    if (currentPage == 1) {
                        setFeaturedCategories(dataArray, false);
                    } else {
                        setFeaturedCategories(dataArray, true);
                    }
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                    Log.e("AllProductsListActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("filterProduct")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.getJSONArray("data");
                    setFeaturedCategories(dataArray, false);
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                    Log.e("AllProductsListActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    private void setFeaturedCategories(JSONArray dataArray, boolean check) {
        try {
            mCategoryList.clear();
            int dataArraylength = dataArray.length();
            for (int i = 0; i < dataArraylength; i++) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                mCategoryList.add(new SubCategoriesModel(dataJson.getString("product_id"), dataJson.getString("product_name"), dataJson.getString("image"), dataJson.getString("product_price"), dataJson.getInt("is_wishlist"), dataJson.getDouble("rating"), dataJson.getInt("is_add_to_cart")));
            }
            if (check) {
                isLoading = false;
            }
            mCategoryAdapter.addAll(mCategoryList);
            if (mCategoryList.size() >= TOTAL_PAGES) mCategoryAdapter.addLoadingFooter();
            else isLastPage = true;
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        WaitDialog.hideDialog();
    }
}