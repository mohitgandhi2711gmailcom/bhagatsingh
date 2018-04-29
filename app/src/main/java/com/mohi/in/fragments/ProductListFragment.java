package com.mohi.in.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.mohi.in.activities.FilterActivity;
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
import com.mohi.in.widgets.ItemOffsetDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListFragment extends Fragment implements ServerCallBack, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mCategoryRv;
    private AllProductListAdapter mCategoryAdapter;
    private ArrayList<SubCategoriesModel> mCategoryList;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static final int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;
    private String strTypeValue;
    private String catId;
    private static final String CATEGORY_ID = "cat_id";
    private Context mContext;
    private FloatingActionButton filterFloatingButton;
    private FloatingActionButton sortFloatingButton;
    private static final int REQUEST_CODE = 7;
    private SwipeRefreshLayout swipe_refresh_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attemptGetData();
    }

    private void init(View view) {
        swipe_refresh_layout = view.findViewById(R.id.swipe_refresh_layout);
        mCategoryRv = view.findViewById(R.id.category_rv);
        mCategoryAdapter = new AllProductListAdapter(mContext);
        filterFloatingButton = view.findViewById(R.id.filter_btn);
        sortFloatingButton = view.findViewById(R.id.sort_btn);
        if (getArguments() != null) {
            strTypeValue = getArguments().getString("name");
            catId = getArguments().getString(CATEGORY_ID);
        }
        setValue();
    }

    private void setValue() {
        swipe_refresh_layout.setOnRefreshListener(this);
        mCategoryAdapter = new AllProductListAdapter(mContext);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        mCategoryRv.setLayoutManager(mLayoutManager);
//        mCategoryRv.addItemDecoration(new ItemOffsetDecorator(1));
        mCategoryRv.addItemDecoration(new GridSpacingItemDecoration(2, 00, false));
        mCategoryList = new ArrayList<>();
        mCategoryRv.setItemAnimator(new DefaultItemAnimator());
        mCategoryRv.setAdapter(mCategoryAdapter);
        mCategoryRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    filterFloatingButton.show();
                    sortFloatingButton.show();
                } else {
                    filterFloatingButton.hide();
                    sortFloatingButton.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy != 0) {
                    filterFloatingButton.hide();
                    sortFloatingButton.hide();
                }
            }
        });

        mCategoryRv.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        attemptGetData();
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

        filterFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FilterActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        sortFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.showToast(mContext, "Sort Button Clicked");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onRefresh() {
        swipe_refresh_layout.setRefreshing(false);
        attemptGetData();
    }

    private void attemptGetData() {
        if (strTypeValue != null && !TextUtils.isEmpty(strTypeValue) && catId != null && !TextUtils.isEmpty(catId)) {
            try {
                if (currentPage == 1) {
                    WaitDialog.showDialog(mContext);
                }
                JsonObject json = new JsonObject();
                json.addProperty("limit", TOTAL_PAGES);
                json.addProperty("page", currentPage);
                json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                int totalWidth = mContext.getResources().getDisplayMetrics().widthPixels;
                Log.d("Total Width", totalWidth + "");
                json.addProperty("width", totalWidth / 2);
                json.addProperty("height", (374 * totalWidth) / 560);
                json.addProperty("type", strTypeValue);
                json.addProperty(CATEGORY_ID, catId);
                json.addProperty("search", "");
                json.addProperty("sort", "");
                ServerCalling.ServerCallingProductsApiPost(mContext, "getProduct", json, this);

            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        } else {
            Methods.showToast(mContext, "No data");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode!= getActivity().RESULT_CANCELED) {
            String minPrice = data.getStringExtra("MinPrice");
            String maxPrice = data.getStringExtra("MaxPrice");
            String colors = data.getStringExtra("Colors");
            int availability = data.getIntExtra("Availabilty", 1);
            String categories = data.getStringExtra("Categories");
            String brand = data.getStringExtra("Brand");

            JSONArray jj = new JSONArray();
            assert categories != null;
            if (!categories.trim().equalsIgnoreCase("")) {
                String catValue[] = categories.trim().split(",");
                int size = catValue.length;
                if (size > 0) {
                    for (String aCatValue : catValue) {
                        jj.put(aCatValue);
                    }
                }
            }
            JSONObject json = new JSONObject();
            try {
                json.put("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                json.put("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                json.put("limit", TOTAL_PAGES);
                json.put("page", currentPage);
                json.put(CATEGORY_ID, catId);
                json.put("from_price", minPrice);
                json.put("to_price", maxPrice);
                json.put("availabilty", availability);
                json.put("brand", brand);
                json.put("color", colors);
                json.put("sort", "");
                ServerCalling.ServerCallingProductsApiPost(mContext, "filterProduct", (JsonObject) new JsonParser().parse(json.toString()), this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}