package com.mohi.in.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.HomeActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.HomePagerModel;
import com.mohi.in.model.HomeProductModel;
import com.mohi.in.model.ProductModel;
import com.mohi.in.ui.adapter.HomePagerAdapter;
import com.mohi.in.ui.adapter.HomeProductsAdapter;
import com.mohi.in.ui.adapter.HomeSubProductAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.RefreshList;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.AutoScrollViewPager;
import com.mohi.in.widgets.MyLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class BannerDealsFragment extends Fragment implements View.OnClickListener, ServerCallBack, RefreshList {
    private Context mContext;
    private View root;
    private AutoScrollViewPager pagerView;
    private AutoScrollViewPager pagerView1;
    private AutoScrollViewPager pagerView2;
    private AutoScrollViewPager pagerView3;
    private MyLinearLayoutManager rv_featuredCategories, rv_featuredCategories1, rv_featuredCategories2, rv_featuredcategories3, rv_featuredcategories4;
    private ArrayList<HomePagerModel> mPagerList = new ArrayList<>();
    private RadioGroup mPagerRg;
    private RadioGroup mPagerRg1;
    private RadioGroup mPagerRg2;
    private RadioGroup mPagerRg3;
    private HashMap<String, ArrayList<HomeProductModel>> mProductHashmap;
    private ArrayList<ProductModel> productList = new ArrayList<>();
    private HomeProductsAdapter homeProductsAdapter;
    private JSONArray jArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_banner_deal, container, false);
        getControls();
        return root;
    }

    private void getControls() {
        mContext = getActivity();
        pagerView = root.findViewById(R.id.viewPagerhome);
        pagerView1 = root.findViewById(R.id.viewPagerhomenew1);
        pagerView2 = root.findViewById(R.id.viewPagerhomenew2);
        pagerView3 = root.findViewById(R.id.viewPagerhomenew3);
        mPagerRg = root.findViewById(R.id.rg_pager);
        mPagerRg1 = root.findViewById(R.id.rg_pagernew1);
        mPagerRg2 = root.findViewById(R.id.rg_pagernew2);
        mPagerRg3 = root.findViewById(R.id.rg_pagernew3);
        rv_featuredCategories = root.findViewById(R.id.HomeFragment_FeaturedCategories);
        rv_featuredCategories1 = root.findViewById(R.id.HomeFragment_FeaturedCategoriesnew1);
        rv_featuredCategories2 = root.findViewById(R.id.HomeFragment_FeaturedCategoriesnew2);
        rv_featuredcategories3 = root.findViewById(R.id.HomeFragment_FeaturedCategoriesnew3);
        rv_featuredcategories4 = root.findViewById(R.id.HomeFragment_FeaturedCategoriesnew4);
    }

    @Override
    public void onResume() {
        super.onResume();
        setValue();
    }

    private void setValue() {
        rv_featuredCategories.setHasFixedSize(true);
        rv_featuredCategories1.setHasFixedSize(true);
        rv_featuredCategories2.setHasFixedSize(true);
        rv_featuredcategories3.setHasFixedSize(true);
        rv_featuredcategories4.setHasFixedSize(true);

        homeProductsAdapter = new HomeProductsAdapter(mContext, HomeActivity.HomeActivity, this);
        LinearLayoutManager homeProductsLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_featuredCategories.setLayoutManager(homeProductsLayoutManager);
        rv_featuredCategories.setNestedScrollingEnabled(false);
        rv_featuredCategories.setHasFixedSize(true);
        rv_featuredCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }
            }
        });

        LinearLayoutManager homeProductsLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_featuredCategories1.setLayoutManager(homeProductsLayoutManager1);
        rv_featuredCategories1.setNestedScrollingEnabled(false);
        rv_featuredCategories1.setHasFixedSize(true);
        rv_featuredCategories1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }
            }
        });

        LinearLayoutManager homeProductsLayoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_featuredCategories2.setLayoutManager(homeProductsLayoutManager2);
        rv_featuredCategories2.setNestedScrollingEnabled(false);
        rv_featuredCategories2.setHasFixedSize(true);
        rv_featuredCategories2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }
            }
        });

        LinearLayoutManager homeProductsLayoutManager3 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_featuredcategories3.setLayoutManager(homeProductsLayoutManager3);
        rv_featuredcategories3.setNestedScrollingEnabled(false);
        rv_featuredcategories3.setHasFixedSize(true);
        rv_featuredcategories3.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }

            }
        });

        LinearLayoutManager homeProductsLayoutManager4 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_featuredcategories4.setLayoutManager(homeProductsLayoutManager4);
        rv_featuredcategories4.setNestedScrollingEnabled(false);
        rv_featuredcategories4.setHasFixedSize(true);
        rv_featuredcategories4.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }
            }
        });

        mProductHashmap = new HashMap<>();
        attemptGetProduct();
    }

    @Override
    public void refreshListSuccess() {
        attemptGetProduct();
    }

    private void attemptGetProduct() {
        try {
            WaitDialog.showDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.addProperty("width", getResources().getDimension(R.dimen.home_product_row_width));
            json.addProperty("height", getResources().getDimension(R.dimen.home_product_row_image_height));
            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getHomeScreenDetails", json, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            if (strfFrom.trim().equalsIgnoreCase("getHomeScreenDetails")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONObject dataObj = result.getJSONObject("data");
                    //Country Code may recive in future
                    String countryCode="";
                    SessionStore.save(getActivity(), Common.userPrefName, SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID), SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN), SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_EMAIL), SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_MOBILENO), SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_NAME), SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.PROFILEPICTURE), SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESSID), SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESSNAME), SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESS), result.getString("currency"),countryCode);
                    SessionStore.saveCurrency(getActivity(), Common.currencyPrefName, result.getString("currency"));
                    setProducts(dataObj);
                    dataObj = result.getJSONObject("banners");
                    JSONArray jsonArray = dataObj.getJSONArray("image");
                    setPager(jsonArray);
                    WaitDialog.hideDialog();
                } else {
                    Methods.showToast(getActivity(), result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("banner")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONObject jData = result.getJSONObject("data");
                    JSONArray dataObj = jData.getJSONArray("image");
                    setPager(dataObj);
                } else {
                    Methods.showToast(getActivity(), result.getString("msg"));
                }
            }

        } catch (Exception e) {
            WaitDialog.hideDialog();
        }
    }












    private void setPager(JSONArray dataObj) {

        try {


            mPagerList.clear();

            int size = dataObj.length();

            addRadioButtons(size);

            for (int i = 0; i < size; i++) {

                mPagerList.add(new HomePagerModel("", dataObj.getString(i)));

            }


            HomePagerAdapter mHomePagerAdapter = new HomePagerAdapter(mContext);
            mHomePagerAdapter.setPagerList(mPagerList);
            pagerView.setAdapter(mHomePagerAdapter);
            pagerView.startAutoScroll();
            pagerView.setInterval(5000);
            pagerView.setCycle(true);
            pagerView.setStopScrollWhenTouch(true);

            pagerView1.setAdapter(mHomePagerAdapter);
            pagerView1.startAutoScroll();
            pagerView1.setInterval(5000);
            pagerView1.setCycle(true);
            pagerView1.setStopScrollWhenTouch(true);

            pagerView2.setAdapter(mHomePagerAdapter);
            pagerView2.startAutoScroll();
            pagerView2.setInterval(5000);
            pagerView2.setCycle(true);
            pagerView2.setStopScrollWhenTouch(true);

            pagerView3.setAdapter(mHomePagerAdapter);
            pagerView3.startAutoScroll();
            pagerView3.setInterval(5000);
            pagerView3.setCycle(true);
            pagerView3.setStopScrollWhenTouch(true);


            pagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    Log.e("sdfdsfdsfdsf", "sfdsfdsfdsf Poooooo: " + position);
                    if (((RadioButton) mPagerRg.getChildAt(position)) != null) {
                        ((RadioButton) mPagerRg.getChildAt(position)).setChecked(true);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            pagerView1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    Log.e("sdfdsfdsfdsf", "sfdsfdsfdsf Poooooo: " + position);
                    if (((RadioButton) mPagerRg1.getChildAt(position)) != null) {
                        ((RadioButton) mPagerRg1.getChildAt(position)).setChecked(true);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            pagerView2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    Log.e("sdfdsfdsfdsf", "sfdsfdsfdsf Poooooo: " + position);
                    if (((RadioButton) mPagerRg2.getChildAt(position)) != null) {
                        ((RadioButton) mPagerRg2.getChildAt(position)).setChecked(true);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            pagerView3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    Log.e("sdfdsfdsfdsf", "sfdsfdsfdsf Poooooo: " + position);
                    if (((RadioButton) mPagerRg3.getChildAt(position)) != null) {
                        ((RadioButton) mPagerRg3.getChildAt(position)).setChecked(true);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {


        }

        WaitDialog.hideDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
        pagerView.stopAutoScroll();
        pagerView1.stopAutoScroll();
        pagerView2.stopAutoScroll();
        pagerView3.stopAutoScroll();
    }

    private void setProducts(JSONObject dataObj) {

        try {

            Log.v("CateAdapter", "imgUrlllll ********* dfdfdfdfdf 7777777777 ");

            productList.clear();


            Iterator<String> iter = dataObj.keys();


            while (iter.hasNext()) {
                String key = iter.next();
                final String headerKey = key;
                Log.e("fdsfdsfds", "dfdsfdsfdsf dsfdsf dsf ds: " + headerKey);


                if (dataObj.getJSONArray(headerKey).length() > 0) {

                    jArray = dataObj.getJSONArray(headerKey);
                    JSONObject jobj = jArray.getJSONObject(0);
                    //productList.add(new ProductModel(headerKey.trim().replaceAll("_", " "), jobj.getString("is_product"), jobj.getString("type"), dataObj.getJSONArray(headerKey)));

                }


            }


            sortInAse();
            productList.add(new ProductModel("Featured Categories", "0", "category", jArray));
            homeProductsAdapter.setList(productList);

            rv_featuredCategories.setAdapter(homeProductsAdapter);
            rv_featuredCategories1.setAdapter(homeProductsAdapter);
            rv_featuredCategories2.setAdapter(homeProductsAdapter);
            rv_featuredcategories3.setAdapter(homeProductsAdapter);
            rv_featuredcategories4.setAdapter(homeProductsAdapter);
            WaitDialog.hideDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Inflated all product category and items
    * */
    private void inflateData(int rowCount) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_feature_products_row, null);
        Object[] keys = mProductHashmap.keySet().toArray();
        ProductInflateView productInflateView = new ProductInflateView(view, rowCount, keys[rowCount].toString());
        view.setTag(productInflateView);

    }

    //inflate view
    class ProductInflateView {
        private LinearLayoutManager mLayoutManager;
        private HomeSubProductAdapter mAdapter;
        private RecyclerView mProductsRv;

        ProductInflateView(View view, int rowCount, String key) {
            mAdapter = new HomeSubProductAdapter(mContext);
            mProductsRv = (RecyclerView) view.findViewById(R.id.rv_products);
            if (mLayoutManager != null) {// Workaround: android.support.v7.widget.LinearLayoutManager is already attached to a RecyclerView
                mLayoutManager = null;
            }
            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mProductsRv.setLayoutManager(mLayoutManager);
            ArrayList<HomeProductModel> list = mProductHashmap.get(key);
            mAdapter.setProductList(list);
            mProductsRv.setAdapter(mAdapter);
        }
    }


    @Override
    public void onClick(View view) {

    }


    // Get user information
    private void attemptGetFeaturedProduct() {
        try {

            JsonObject json = new JsonObject();
            json.addProperty("limit", "5");
            json.addProperty("page", "1");
            json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.addProperty("width", getResources().getDimension(R.dimen.home_product_row_width));
            json.addProperty("height", getResources().getDimension(R.dimen.home_product_row_image_height));

            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getFeaturedProduct", json, this);


        } catch (Exception e) {


            Log.e("HomeFragment Exception", "Exception attemptTOGetUserInfo: " + e.getMessage());
        }

    }

    // Get user information
    private void attemptGetFeaturedCategories() {
        try {

            JsonObject json = new JsonObject();
            json.addProperty("limit", "5");
            json.addProperty("page", "1");
            json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.addProperty("width", getResources().getDimension(R.dimen.home_categouries_row_height));
            json.addProperty("height", getResources().getDimension(R.dimen.home_categouries_row_height));

            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getCategories", json, this);


        } catch (Exception e) {


            Log.e("HomeFragment Exception", "Exception attemptTOGetUserInfo: " + e.getMessage());
        }

    }

    private void attemptPager() {

        try {
            WaitDialog.showDialog(getActivity());
            JsonObject json = new JsonObject();
//            json.addProperty("limit", "5");
//            json.addProperty("page", "1");

            json.addProperty("width", Common.getDeviceHeightWidth(getActivity()).widthPixels);
            json.addProperty("height", getResources().getDimension(R.dimen.home_viewpager_row_height));


            ServerCalling.ServerCallingProductsApiPost(getActivity(), "banner", json, this);


        } catch (Exception e) {


            Log.e("HomeFragment Exception", "Exception attemptTOGetUserInfo: " + e.getMessage());
        }
    }

    //Sorting list
    public void sortInAse() {
        Comparator<ProductModel> comparator = new Comparator<ProductModel>() {

            @Override
            public int compare(ProductModel object1, ProductModel object2) {


                return object1.isProduct.compareToIgnoreCase(object2.isProduct);
            }
        };
        Collections.sort(productList, comparator);

    }


    public void addRadioButtons(int number) {

        mPagerRg.removeAllViews();

        for (int i = 1; i <= number; i++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setButtonDrawable(R.drawable.radio_button_bg);
            rdbtn.setId((1 * 2) + i);
            rdbtn.setPadding(0, 0, 0, 0);
            rdbtn.setScaleX(0.50f);
            rdbtn.setScaleY(0.50f);
            mPagerRg.addView(rdbtn);

            RadioButton rdbtn1 = new RadioButton(getActivity());
            rdbtn1.setButtonDrawable(R.drawable.radio_button_bg);
            rdbtn1.setId((1 * 2) + i);
            rdbtn1.setPadding(0, 0, 0, 0);
            rdbtn1.setScaleX(0.50f);
            rdbtn1.setScaleY(0.50f);
            mPagerRg1.addView(rdbtn1);

            RadioButton rdbtn2 = new RadioButton(getActivity());
            rdbtn2.setButtonDrawable(R.drawable.radio_button_bg);
            rdbtn2.setId((1 * 2) + i);
            rdbtn2.setPadding(0, 0, 0, 0);
            rdbtn2.setScaleX(0.50f);
            rdbtn2.setScaleY(0.50f);
            mPagerRg2.addView(rdbtn2);

            RadioButton rdbtn3 = new RadioButton(getActivity());
            rdbtn3.setButtonDrawable(R.drawable.radio_button_bg);
            rdbtn3.setId((1 * 2) + i);
            rdbtn3.setPadding(0, 0, 0, 0);
            rdbtn3.setScaleX(0.50f);
            rdbtn3.setScaleY(0.50f);
            mPagerRg3.addView(rdbtn3);

        }
        // mPagerRg.addView(ll);


    }
}

