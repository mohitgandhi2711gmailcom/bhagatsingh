package com.mohi.in.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.HomeActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.BannerModel;
import com.mohi.in.model.CategoryModel;
import com.mohi.in.model.HomeScreenSliderModel;
import com.mohi.in.ui.adapter.BannerCategoryAdapter;
import com.mohi.in.ui.adapter.BottomSliderAdapter;
import com.mohi.in.ui.adapter.ItemViewedAdapter;
import com.mohi.in.ui.adapter.MiddleSliderAdapter;
import com.mohi.in.ui.adapter.MostSearchedAdapter;
import com.mohi.in.ui.adapter.OfferTypeOneAdapter;
import com.mohi.in.ui.adapter.OfferTypeTwoAdapter;
import com.mohi.in.ui.adapter.TopSliderAdapter;
import com.mohi.in.ui.adapter.TrendingNowAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.widgets.AutoScrollViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BannerDealsFragment extends Fragment implements View.OnClickListener, ServerCallBack, RefreshList, SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private LinearLayout signInLinearLayout;
    private TextView signInTextView;
    private AutoScrollViewPager topBannerScrollView;
    private RadioGroup topBannerRadioGroup;
    private SwipeRefreshLayout swipe_refresh_layout;
    //private ScrollView parent_scroll_view;
    private RecyclerView offerTypeOneRecyclerView;
    private RecyclerView offerTypeTwoRecyclerView;
    private RecyclerView mostSearchedRecyclerView;
    private RecyclerView trendingNowRecyclerView;
    private RecyclerView categoryRecyclerView;
    private AutoScrollViewPager middleBannerScrollView;
    private RadioGroup middleBannerRadioGroup;
    private RecyclerView itemViewedRecyclerView;
    private AutoScrollViewPager bottomBannerScrollView;
    private RadioGroup bottomBannerRadioGroup;
    private TopSliderAdapter topSliderAdapter;
    private MiddleSliderAdapter middleSliderAdapter;
    private BottomSliderAdapter bottomSliderAdapter;
    private OfferTypeOneAdapter offerTypeOneAdapter;
    private OfferTypeTwoAdapter offerTypeTwoAdapter;
    private MostSearchedAdapter mostSearchedAdapter;
    private TrendingNowAdapter trendingNowAdapter;
    private ItemViewedAdapter itemViewedAdapter;
    private BannerCategoryAdapter bannerCategoryAdapter;
    ArrayList<HomeScreenSliderModel> topBannerPagerList;
    ArrayList<HomeScreenSliderModel> middleBannerPagerList;
    ArrayList<HomeScreenSliderModel> bottomBannerPagerList;
    ArrayList<HomeScreenSliderModel> offerTypeOneList;
    ArrayList<HomeScreenSliderModel> offerTypeTwoList;
    ArrayList<BannerModel> mostSearchedList;
    ArrayList<BannerModel> trendingNowList;
    ArrayList<BannerModel> itemViewedList;
    ArrayList<CategoryModel> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner_deal, container, false);
        init(view);
        return view;
    }

    private void init(View root) {
        mContext = getActivity();
        //parent_scroll_view = root.findViewById(R.id.parent_scroll_view);
        swipe_refresh_layout = root.findViewById(R.id.swipe_refresh_layout);
        signInLinearLayout = root.findViewById(R.id.sign_in_ll);
        signInTextView = root.findViewById(R.id.sign_in_tv);
        topBannerScrollView = root.findViewById(R.id.top_banner_sv);
        topBannerRadioGroup = root.findViewById(R.id.top_banner_rg);
        offerTypeOneRecyclerView = root.findViewById(R.id.offertype1_rv);
        offerTypeTwoRecyclerView = root.findViewById(R.id.offertype2_rv);
        mostSearchedRecyclerView = root.findViewById(R.id.most_searched_rv);
        trendingNowRecyclerView = root.findViewById(R.id.trending_now_rv);
        categoryRecyclerView = root.findViewById(R.id.category_rv);
        middleBannerScrollView = root.findViewById(R.id.middle_banner_sv);
        middleBannerRadioGroup = root.findViewById(R.id.middle_banner_rg);
        itemViewedRecyclerView = root.findViewById(R.id.item_viewed_rv);
        bottomBannerScrollView = root.findViewById(R.id.bottom_banner_sv);
        bottomBannerRadioGroup = root.findViewById(R.id.bottom_banner_rg);
        topBannerPagerList = new ArrayList<>();
        middleBannerPagerList = new ArrayList<>();
        bottomBannerPagerList = new ArrayList<>();
        offerTypeOneList = new ArrayList<>();
        offerTypeTwoList = new ArrayList<>();
        mostSearchedList = new ArrayList<>();
        trendingNowList = new ArrayList<>();
        itemViewedList = new ArrayList<>();
        categoryList = new ArrayList<>();
        setValue();
    }


    private void setValue() {
        signInTextView.setOnClickListener(this);
        swipe_refresh_layout.setOnRefreshListener(this);
        offerTypeOneRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        offerTypeTwoRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mostSearchedRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        trendingNowRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemViewedRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

        offerTypeOneAdapter = new OfferTypeOneAdapter(mContext, offerTypeOneList);
        offerTypeOneRecyclerView.setAdapter(offerTypeOneAdapter);
        offerTypeTwoAdapter = new OfferTypeTwoAdapter(mContext, offerTypeTwoList);
        offerTypeTwoRecyclerView.setAdapter(offerTypeTwoAdapter);

        mostSearchedAdapter = new MostSearchedAdapter(mContext, mostSearchedList);
        mostSearchedRecyclerView.setAdapter(mostSearchedAdapter);
        trendingNowAdapter = new TrendingNowAdapter(mContext, trendingNowList);
        trendingNowRecyclerView.setAdapter(trendingNowAdapter);
        itemViewedAdapter = new ItemViewedAdapter(mContext, itemViewedList);
        itemViewedRecyclerView.setAdapter(itemViewedAdapter);
        bannerCategoryAdapter = new BannerCategoryAdapter(mContext, categoryList);
        categoryRecyclerView.setAdapter(bannerCategoryAdapter);
        setAllSlider();
    }

    private void setAllSlider() {
        topSliderAdapter = new TopSliderAdapter(mContext);
        topSliderAdapter.setPagerList(topBannerPagerList);
        topBannerScrollView.setAdapter(topSliderAdapter);
        topBannerScrollView.startAutoScroll();
        topBannerScrollView.setInterval(5000);
        topBannerScrollView.setCycle(true);
        topBannerScrollView.setStopScrollWhenTouch(true);
        topBannerScrollView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*
                 * Check When PAge is Scrolled
                 * */
            }

            @Override
            public void onPageSelected(int position) {
                if (topBannerRadioGroup.getChildAt(position) != null) {
                    ((RadioButton) topBannerRadioGroup.getChildAt(position)).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                 * Check the Sate of Scrolling
                 * */
            }
        });

        middleSliderAdapter = new MiddleSliderAdapter(mContext);
        middleSliderAdapter.setPagerList(middleBannerPagerList);
        middleBannerScrollView.setAdapter(middleSliderAdapter);
        middleBannerScrollView.startAutoScroll();
        middleBannerScrollView.setInterval(5000);
        middleBannerScrollView.setCycle(true);
        middleBannerScrollView.setStopScrollWhenTouch(true);
        middleBannerScrollView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*
                 * Check When PAge is Scrolled
                 * */
            }

            @Override
            public void onPageSelected(int position) {
                if (middleBannerRadioGroup.getChildAt(position) != null) {
                    ((RadioButton) middleBannerRadioGroup.getChildAt(position)).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                 * Check the Sate of Scrolling
                 * */
            }
        });

        bottomSliderAdapter = new BottomSliderAdapter(mContext);
        bottomSliderAdapter.setPagerList(bottomBannerPagerList);
        bottomBannerScrollView.setAdapter(bottomSliderAdapter);
        bottomBannerScrollView.startAutoScroll();
        bottomBannerScrollView.setInterval(5000);
        bottomBannerScrollView.setCycle(true);
        bottomBannerScrollView.setStopScrollWhenTouch(true);
        bottomBannerScrollView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*
                 * Check When PAge is Scrolled
                 * */
            }

            @Override
            public void onPageSelected(int position) {
                if (bottomBannerRadioGroup.getChildAt(position) != null) {
                    ((RadioButton) bottomBannerRadioGroup.getChildAt(position)).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                 * Check the Sate of Scrolling
                 * */
            }
        });

        attemptGetHomeScreenData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUserLogin()) {
            signInLinearLayout.setVisibility(View.GONE);
        } else {
            signInLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public boolean isUserLogin() {
        String userId = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID);
        String token = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN);
        return !(TextUtils.isEmpty(userId) && TextUtils.isEmpty(token) && userId == null && token == null);
    }

    private void attemptGetHomeScreenData() {
        try {
            WaitDialog.showDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getHomeScreenDetails", json, this);

        } catch (Exception e) {
            Log.e("Error", String.valueOf(e));
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        switch (strfFrom.trim()) {
            case "getHomeScreenDetails":
                if (result.optString("status").trim().equalsIgnoreCase("success")) {
                    setDataAfterResult(result);
                } else {
                    Methods.showToast(getActivity(), result.optString("msg"));
                }
                break;
            case "banner":
                break;
            default:
                Methods.showToast(mContext, "Invalid Result");
        }
    }

    private void setDataAfterResult(JSONObject result) {
        JSONObject dataObject = result.optJSONObject("data");
        JSONObject bannerObject = dataObject.optJSONObject("banners");

        //Top Slider
        JSONArray topBannerArray = bannerObject.optJSONArray("slider");
        topSliderAdapter.setPagerList(setSlider(topBannerArray, topBannerPagerList));
        addRadioButtons(topBannerRadioGroup, topBannerArray.length());

        //Middle Slider
        JSONArray middleBannerArray = bannerObject.optJSONArray("banners-middle");
        middleSliderAdapter.setPagerList(setSlider(middleBannerArray, middleBannerPagerList));
        addRadioButtons(middleBannerRadioGroup, middleBannerArray.length());

        //Bottom Slider
        JSONArray bottomBannerArray = bannerObject.optJSONArray("banner-bottom");
        bottomSliderAdapter.setPagerList(setSlider(bottomBannerArray, bottomBannerPagerList));
        addRadioButtons(bottomBannerRadioGroup, bottomBannerArray.length());

        //Set Type1 & Type2 Offers
        JSONObject offerObject = bannerObject.optJSONObject("offer");
        JSONArray type1Offer = offerObject.optJSONArray("type1");
        JSONArray type2Offer = offerObject.optJSONArray("type2");
        offerTypeOneAdapter.updateList(setSlider(type1Offer, offerTypeOneList));
        offerTypeTwoAdapter.updateList(setSlider(type2Offer, offerTypeTwoList));

        JSONArray categoryArray = dataObject.optJSONArray("categories");
        bannerCategoryAdapter.updateList(setCategoryList(categoryArray, categoryList));

        JSONArray mostSearchedArray = dataObject.optJSONArray("most_searched");
        mostSearchedAdapter.updateList(setList(mostSearchedArray, mostSearchedList));

        JSONArray trendingNowArray = dataObject.optJSONArray("trending_now");
        trendingNowAdapter.updateList(setList(trendingNowArray, trendingNowList));

        JSONArray itemViewedArray = dataObject.optJSONArray("recently_viewed");
        itemViewedAdapter.updateList(setList(itemViewedArray, itemViewedList));
    }

    private ArrayList<CategoryModel> setCategoryList(JSONArray categoryArray, ArrayList<CategoryModel> list) {
        list.clear();
        for (int i = 0; i < categoryArray.length(); i++) {
            CategoryModel obj = new CategoryModel();
            obj.setId(categoryArray.optJSONObject(i).optString("id"));
            obj.setName(categoryArray.optJSONObject(i).optString("name"));
            obj.setImage(categoryArray.optJSONObject(i).optString("image"));
            list.add(obj);
        }
        return list;
    }

    private ArrayList<HomeScreenSliderModel> setSlider(JSONArray sliderArray, ArrayList<HomeScreenSliderModel> list) {
        list.clear();
        for (int i = 0; i < sliderArray.length(); i++) {
            HomeScreenSliderModel obj = new HomeScreenSliderModel();
            obj.setImageUrl(sliderArray.opt(i).toString());
            list.add(obj);
        }
        return list;
    }

    private ArrayList<BannerModel> setList(JSONArray sliderArray, ArrayList<BannerModel> list) {
        list.clear();
        for (int i = 0; i < sliderArray.length(); i++) {
            BannerModel obj = new BannerModel();
            obj.setProduct_id(((JSONObject) sliderArray.opt(i)).optString("product_id"));
            obj.setProduct_name(((JSONObject) sliderArray.opt(i)).optString("product_name"));
            obj.setImage(((JSONObject) sliderArray.opt(i)).optString("image"));
            obj.setProduct_price(((JSONObject) sliderArray.opt(i)).optString("product_price"));
            obj.setProduct_special_price(((JSONObject) sliderArray.opt(i)).optString("product_special_price"));
            obj.setIs_wishlist(((JSONObject) sliderArray.opt(i)).optString("is_wishlist"));
            obj.setIs_add_to_cart(((JSONObject) sliderArray.opt(i)).optString("is_add_to_cart"));
            list.add(obj);
        }
        return list;
    }

    public void addRadioButtons(RadioGroup radioGroup, int number) {
        radioGroup.removeAllViews();
        for (int i = 0; i < number; i++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setButtonDrawable(R.drawable.radio_button_bg);
            rdbtn.setId(2 + i);
            rdbtn.setPadding(0, 0, 0, 0);
            rdbtn.setScaleX(0.50f);
            rdbtn.setScaleY(0.50f);
            radioGroup.addView(rdbtn);
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sign_in_tv) {
            if (!isUserLogin()) {
                ((HomeActivity) getActivity()).setParticulatTab(4);
            }

        } else {
            Methods.showToast(mContext, "Invalid Case");
        }
    }

    @Override
    public void refreshListSuccess() {
        attemptGetHomeScreenData();
    }

    @Override
    public void onRefresh() {
        swipe_refresh_layout.setRefreshing(false);
        attemptGetHomeScreenData();
    }
}

