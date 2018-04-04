package com.mohi.in.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.SubCategoriesModel;
import com.mohi.in.ui.adapter.AllProductListAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.PaginationScrollListener;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuLightTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllProductsListActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {

    private RecyclerView mCategoryRv;
    private Context mContext;
    private AllProductListAdapter mCategoryAdapter;
    private ArrayList<SubCategoriesModel> mCategoryList;
    private LinearLayout ll_sort;
    private LinearLayout ll_filter;
    private LinearLayout ll_sortFilter;
    private ImageView iv_menu;
    private ImageView iv_search;
    private ImageView iv_filter;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_filter;
    private MaterialRippleLayout mrl_rest;
    private UbuntuLightTextView tv_headerTilel;
    Intent intent;
    private String strTitleName = "", strSearch = "", strCatId = "", strType = "";
    private String cat_id = "", from_price = "", to_price = "", brand = "", color = "", strTypeValue = "", strSort = "";
    private int availabilty = 0;
    private View v_sortFilter;
    private boolean searchFlag = true;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        mCategoryAdapter = new AllProductListAdapter(AllProductsListActivity.this);
        intent = getIntent();
        if (intent.getStringExtra("Type") != null) {
            if (intent.getStringExtra("Type").trim().equalsIgnoreCase("Search")) {
                strSearch = intent.getStringExtra("Value");
                strTitleName = intent.getStringExtra("Value");
                strTypeValue = intent.getStringExtra("Type");
            } else if (intent.getStringExtra("Type").trim().equalsIgnoreCase("Filter")) {
                strTitleName = intent.getStringExtra("Type");
                strTypeValue = intent.getStringExtra("Type");
                cat_id = intent.getStringExtra("Categories");
                from_price = intent.getStringExtra("MinPrice");
                to_price = intent.getStringExtra("MaxPrice");
                availabilty = intent.getIntExtra("Availabilty", 0);
                brand = intent.getStringExtra("Brand");
                color = intent.getStringExtra("Colors");
            } else {
                strType = intent.getStringExtra("Type");
                strTitleName = intent.getStringExtra("Type");
                strTypeValue = intent.getStringExtra("Type");
            }
        } else if (intent.getStringExtra("cat_id") != null) {
            strCatId = intent.getStringExtra("cat_id");
            strTitleName = intent.getStringExtra("name");
        }
        getControls();
    }

    private void getControls() {
        mContext = AllProductsListActivity.this;
        mCategoryRv = findViewById(R.id.category_rv);
        v_sortFilter = findViewById(R.id.Category_SortFilterLayout);
        ll_sort = findViewById(R.id.Category_Sort);
        ll_filter = findViewById(R.id.Category_Filter);
        ll_sortFilter = findViewById(R.id.Category_SortFilter);
        mrl_filter = findViewById(R.id.Header_FilterLayOut);
        mrl_rest = findViewById(R.id.Header_restLayOut);
        iv_menu = findViewById(R.id.Header_Menu);
        iv_search = findViewById(R.id.Header_Search);
        iv_filter = findViewById(R.id.Header_Filter);
        tv_headerTilel = findViewById(R.id.Header_Title);
        ll_titellogo = findViewById(R.id.Header_Titel_Logo);
    }

    private void setValue() {
        currentPage = 1;
        v_sortFilter.setVisibility(View.VISIBLE);
        ll_sortFilter.setVisibility(View.VISIBLE);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        mCategoryRv.setLayoutManager(mLayoutManager);
        mCategoryList = new ArrayList<>();
        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);
        mrl_rest.setVisibility(View.GONE);
        mrl_filter.setVisibility(View.GONE);
        tv_headerTilel.setVisibility(View.VISIBLE);
        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);
        if (!strTitleName.trim().equalsIgnoreCase("")) {
            tv_headerTilel.setText(Methods.capitalizeWord(strTitleName.trim().replaceAll("_", " ")));
        } else {
            tv_headerTilel.setText("Products");
        }
        iv_menu.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        iv_filter.setOnClickListener(this);
        ll_sort.setOnClickListener(this);
        ll_filter.setOnClickListener(this);
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

    private void setFeaturedCategories(JSONArray dataArray) {
        try {
            mCategoryList.clear();
            int dataArraylength = dataArray.length();
            for (int i = 0; i < dataArraylength; i++) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                mCategoryList.add(new SubCategoriesModel(dataJson.getString("product_id"), dataJson.getString("product_name"), dataJson.getString("image"), dataJson.getString("product_price"), dataJson.getInt("is_wishlist"), dataJson.getDouble("rating"), dataJson.getInt("is_add_to_cart")));
            }
            mCategoryAdapter.addAll(mCategoryList);
            if (mCategoryList.size() >= TOTAL_PAGES) mCategoryAdapter.addLoadingFooter();
            else isLastPage = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        WaitDialog.hideDialog();
    }

    private void setFeaturedCategoriesNext(JSONArray dataArray) {
        try {
            mCategoryList.clear();
            int dataArraylength = dataArray.length();
            for (int i = 0; i < dataArraylength; i++) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                mCategoryList.add(new SubCategoriesModel(dataJson.getString("product_id"), dataJson.getString("product_name"), dataJson.getString("image"), dataJson.getString("product_price"), dataJson.getInt("is_wishlist"), dataJson.getDouble("rating"), dataJson.getInt("is_add_to_cart")));
            }
            mCategoryAdapter.removeLoadingFooter();
            isLoading = false;
            mCategoryAdapter.addAll(mCategoryList);
            if (mCategoryList.size() >= (TOTAL_PAGES)) mCategoryAdapter.addLoadingFooter();
            else isLastPage = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        WaitDialog.hideDialog();
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.Header_Menu:
                onBackPressed();
                break;
            case R.id.Header_Search:
                if (searchFlag) {
                    searchFlag = false;
                    intent = new Intent(AllProductsListActivity.this, SearchActivity.class);
                    intent.putExtra("Search", strSearch);
                    startActivity(intent);
                    if (!strSearch.trim().equalsIgnoreCase(""))
                        finish();
                }
                break;
            case R.id.Category_Filter:
                if (searchFlag) {
                    searchFlag = false;
                    intent = new Intent(AllProductsListActivity.this, FilterActivity.class);
                    intent.putExtra("Type", strType);
                    intent.putExtra("CatId", strCatId);
                    startActivity(intent);
                    if (!from_price.trim().equalsIgnoreCase(""))
                        finish();
                }
                break;
            case R.id.Category_Sort:
                showSortPricePopup();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
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
    public void onResume() {
        super.onResume();
        updateUI();
        setValue();
    }

    private void updateUI() {
        searchFlag = true;
    }

    // Get user information
    private void attemptGetFeaturedCategories() {
        try {
            if (currentPage == 1) {
                WaitDialog.showDialog(this);
            }
            if (strTypeValue.trim().equalsIgnoreCase("Filter")) {
                JSONArray jj = new JSONArray();
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
                json.put("user_id", SessionStore.getUserDetails(AllProductsListActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
                json.put("token", SessionStore.getUserDetails(AllProductsListActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));
                json.put("width", getResources().getDimension(R.dimen.home_allproduct_row_width));
                json.put("height", getResources().getDimension(R.dimen.home_allproduct_row_image_height));
                json.put("limit", TOTAL_PAGES);
                json.put("page", currentPage);
                json.put("cat_id", jj);
                json.put("from_price", from_price);
                json.put("to_price", to_price);
                json.put("availabilty", availabilty);
                json.put("brand", brand);
                json.put("color", color);
                json.put("sort", strSort);
                ServerCalling.ServerCallingProductsApiPost(AllProductsListActivity.this, "filterProduct", (JsonObject) new JsonParser().parse(json.toString()), this);

            } else {
                JsonObject json = new JsonObject();
                json.addProperty("limit", TOTAL_PAGES);
                json.addProperty("page", currentPage);
                json.addProperty("user_id", SessionStore.getUserDetails(AllProductsListActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(AllProductsListActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));
                json.addProperty("width", getResources().getDimension(R.dimen.home_allproduct_row_width));
                json.addProperty("height", getResources().getDimension(R.dimen.home_allproduct_row_image_height));
                json.addProperty("type", strType);
                json.addProperty("cat_id", strCatId);
                json.addProperty("search", strSearch);
                json.addProperty("sort", strSort);
                ServerCalling.ServerCallingProductsApiPost(AllProductsListActivity.this, "getProduct", json, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            if (strfFrom.trim().equalsIgnoreCase("getProduct")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.getJSONArray("data");
                    if (currentPage == 1) {
                        setFeaturedCategories(dataArray);
                    } else {
                        setFeaturedCategoriesNext(dataArray);
                    }
                } else {
                    Methods.showToast(AllProductsListActivity.this, result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("filterProduct")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.getJSONArray("data");
                    setFeaturedCategories(dataArray);
                } else {
                    Methods.showToast(AllProductsListActivity.this, result.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showSortPricePopup() {
        RadioButton rb_LTH, rb_HTL;
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.sort_popup, null);
        rb_LTH = view.findViewById(R.id.Sort_LTH);
        rb_HTL = view.findViewById(R.id.Sort_HTL);
        final Dialog dialog = new Dialog(AllProductsListActivity.this, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        if (strSort.equalsIgnoreCase("1")) {
            rb_LTH.setChecked(true);
        } else if (strSort.equalsIgnoreCase("2")) {
            rb_HTL.setChecked(true);
        }
        rb_LTH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    if (strSort.equalsIgnoreCase("2") || strSort.equalsIgnoreCase("")) {
                        strSort = "1";
                        currentPage = 1;
                        attemptGetFeaturedCategories();
                        dialog.dismiss();
                    }
            }
        });
        rb_HTL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    if (strSort.equalsIgnoreCase("1") || strSort.equalsIgnoreCase("")) {
                        strSort = "2";
                        currentPage = 1;
                        attemptGetFeaturedCategories();
                        dialog.dismiss();
                    }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = Common.getDeviceHeightWidth(AllProductsListActivity.this).widthPixels;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}