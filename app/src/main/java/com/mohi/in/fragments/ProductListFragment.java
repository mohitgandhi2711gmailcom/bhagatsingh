package com.mohi.in.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.common.GridSpacingItemDecoration;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.SubCategoriesModel;
import com.mohi.in.ui.adapter.AllProductListAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.PaginationScrollListener;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListFragment extends Fragment implements ServerCallBack {
    private RecyclerView mCategoryRv;
    private AllProductListAdapter mCategoryAdapter;
    private ArrayList<SubCategoriesModel> mCategoryList;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;
    private String strTypeValue;
    private String cat_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);
        mCategoryRv = v.findViewById(R.id.category_rv);
        new AllProductListAdapter(getContext());
        if(getArguments()!=null)
        {
            strTypeValue = getArguments().getString("name");
            cat_id = getArguments().getString("cat_id");
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setValue();
    }

    private void setValue() {
        mCategoryAdapter = new AllProductListAdapter(getContext());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mCategoryRv.setLayoutManager(mLayoutManager);
        mCategoryRv.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        mCategoryList = new ArrayList<>();
        mCategoryRv.setItemAnimator(new DefaultItemAnimator());
        mCategoryRv.setAdapter(mCategoryAdapter);
        attemptGetFeaturedCategories();
        mCategoryRv.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        attemptGetFeaturedCategories();
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

    private void attemptGetFeaturedCategories() {
        if(strTypeValue!=null&& !TextUtils.isEmpty(strTypeValue)&&cat_id!=null&&!TextUtils.isEmpty(cat_id))
        {
            try {
                if (currentPage == 1) {
                    WaitDialog.showDialog(getActivity());
                }
                if (strTypeValue.trim().equalsIgnoreCase("Filter")) {
                    JSONArray jj = new JSONArray();
                    assert cat_id != null;
                    if (!cat_id.trim().equalsIgnoreCase("")) {
                        String catValue[] = cat_id.trim().split(",");
                        int size = catValue.length;
                        if (size > 0) {
                            for (String aCatValue : catValue) {
                                jj.put(aCatValue);
                            }
                        }
                    }
                    JSONObject json = new JSONObject();
                    json.put("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
                    json.put("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
                    json.put("width", getResources().getDimension(R.dimen.home_allproduct_row_width));
                    json.put("height", getResources().getDimension(R.dimen.home_allproduct_row_image_height));
                    json.put("limit", TOTAL_PAGES);
                    json.put("page", currentPage);
                    json.put("cat_id", jj);
                    json.put("from_price", "");
                    json.put("to_price", "");
                    json.put("availabilty", "");
                    json.put("brand", "");
                    json.put("color", "");
                    json.put("sort", "");
                    ServerCalling.ServerCallingProductsApiPost(getActivity(), "filterProduct", (JsonObject) new JsonParser().parse(json.toString()), this);

                } else {
                    JsonObject json = new JsonObject();
                    json.addProperty("limit", TOTAL_PAGES);
                    json.addProperty("page", currentPage);
                    json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
                    json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
                    json.addProperty("width", getResources().getDimension(R.dimen.home_allproduct_row_width));
                    json.addProperty("height", getResources().getDimension(R.dimen.home_allproduct_row_image_height));
                    json.addProperty("type", strTypeValue);
                    json.addProperty("cat_id", cat_id);
                    json.addProperty("search", "");
                    json.addProperty("sort", "");
                    ServerCalling.ServerCallingProductsApiPost(getActivity(), "getProduct", json, this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Methods.showToast(getActivity(),"No data");
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            if (strfFrom.trim().equalsIgnoreCase("getProduct")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.getJSONArray("data");
                    if (currentPage == 1) {
                        setFeaturedCategories(dataArray,false);
                    } else {
                        setFeaturedCategories(dataArray,true);
                    }
                } else {
                    Methods.showToast(getContext(), result.getString("msg"));
                    Log.e("AllProductsListActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("filterProduct")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.getJSONArray("data");
                    setFeaturedCategories(dataArray,false);
                } else {
                    Methods.showToast(getContext(), result.getString("msg"));
                    Log.e("AllProductsListActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFeaturedCategories(JSONArray dataArray,boolean check) {
        try {
            mCategoryList.clear();
            int dataArraylength = dataArray.length();
            for (int i = 0; i < dataArraylength; i++) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                mCategoryList.add(new SubCategoriesModel(dataJson.getString("product_id"), dataJson.getString("product_name"), dataJson.getString("image"), dataJson.getString("product_price"), dataJson.getInt("is_wishlist"), dataJson.getDouble("rating"), dataJson.getInt("is_add_to_cart")));
            }
            if(check)
            {
                isLoading=false;
            }
            mCategoryAdapter.addAll(mCategoryList);
            if (mCategoryList.size() >= TOTAL_PAGES) mCategoryAdapter.addLoadingFooter();
            else isLastPage = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        WaitDialog.hideDialog();
    }
}
