package com.mohi.in.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.HomeActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.BannerModel;
import com.mohi.in.model.HomeScreenSliderModel;
import com.mohi.in.ui.adapter.BannerDealAdapter;
import com.mohi.in.ui.adapter.BottomSliderAdapter;
import com.mohi.in.ui.adapter.MiddleSliderAdapter;
import com.mohi.in.ui.adapter.OfferTypeOneAdapter;
import com.mohi.in.ui.adapter.OfferTypeTwoAdapter;
import com.mohi.in.ui.adapter.TopSliderAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.widgets.AutoScrollViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BannerDealsFragment extends Fragment implements View.OnClickListener, ServerCallBack, RefreshList {
    private Context mContext;
    private LinearLayout sign_in_ll;
    private TextView sign_in_tv;
    private AutoScrollViewPager top_banner_sv;
    private RadioGroup top_banner_rg;
    private RecyclerView offertype1_rv;
    private RecyclerView offertype2_rv;
    private RecyclerView most_searched_rv;
    private RecyclerView trending_now_rv;
    private AutoScrollViewPager middle_banner_sv;
    private RadioGroup middle_banner_rg;
    private RecyclerView item_viewed_rv;
    private AutoScrollViewPager bottom_banner_sv;
    private RadioGroup bottom_banner_rg;
    ArrayList<HomeScreenSliderModel> topBannerPagerList;
    ArrayList<HomeScreenSliderModel> middleBannerPagerList;
    ArrayList<HomeScreenSliderModel> bottomBannerPagerList;
    private TopSliderAdapter topSliderAdapter;
    private MiddleSliderAdapter middleSliderAdapter;
    private BottomSliderAdapter bottomSliderAdapter;
    private OfferTypeOneAdapter offerTypeOneAdapter;
    private OfferTypeTwoAdapter offerTypeTwoAdapter;
    ArrayList<HomeScreenSliderModel> offerTypeOneList;
    ArrayList<HomeScreenSliderModel> offerTypeTwoList;
    ArrayList<BannerModel> mostSearchedList;
    ArrayList<BannerModel> trendingNowList;
    ArrayList<BannerModel> itemViewedList;
    private BannerDealAdapter mostSearchedAdapter;
    private BannerDealAdapter trendingNowAdapter;
    private BannerDealAdapter itemViewedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner_deal, container, false);
        init(view);
        return view;
    }

    private void init(View root) {
        mContext = getActivity();
        sign_in_ll = root.findViewById(R.id.sign_in_ll);
        sign_in_tv = root.findViewById(R.id.sign_in_tv);
        top_banner_sv = root.findViewById(R.id.top_banner_sv);
        top_banner_rg = root.findViewById(R.id.top_banner_rg);
        offertype1_rv = root.findViewById(R.id.offertype1_rv);
        offertype2_rv = root.findViewById(R.id.offertype2_rv);
        most_searched_rv = root.findViewById(R.id.most_searched_rv);
        trending_now_rv = root.findViewById(R.id.trending_now_rv);
        middle_banner_sv = root.findViewById(R.id.middle_banner_sv);
        middle_banner_rg = root.findViewById(R.id.middle_banner_rg);
        item_viewed_rv = root.findViewById(R.id.item_viewed_rv);
        bottom_banner_sv = root.findViewById(R.id.bottom_banner_sv);
        bottom_banner_rg = root.findViewById(R.id.bottom_banner_rg);
        topBannerPagerList = new ArrayList<>();
        middleBannerPagerList = new ArrayList<>();
        bottomBannerPagerList = new ArrayList<>();
        offerTypeOneList = new ArrayList<>();
        offerTypeTwoList = new ArrayList<>();
        mostSearchedList=new ArrayList<>();
        trendingNowList=new ArrayList<>();
        itemViewedList=new ArrayList<>();
        setValue();
    }

    private void setValue() {
        sign_in_tv.setOnClickListener(this);
        offertype1_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        offertype2_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        most_searched_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        trending_now_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        item_viewed_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        offerTypeOneAdapter = new OfferTypeOneAdapter(mContext, offerTypeOneList);
        offertype1_rv.setAdapter(offerTypeOneAdapter);
        offerTypeTwoAdapter = new OfferTypeTwoAdapter(mContext, offerTypeTwoList);
        offertype2_rv.setAdapter(offerTypeTwoAdapter);

        mostSearchedAdapter = new BannerDealAdapter(mContext,mostSearchedList);
        most_searched_rv.setAdapter(mostSearchedAdapter);
        trendingNowAdapter = new BannerDealAdapter(mContext,trendingNowList);
        trending_now_rv.setAdapter(trendingNowAdapter);
        itemViewedAdapter = new BannerDealAdapter(mContext,itemViewedList);
        item_viewed_rv.setAdapter(itemViewedAdapter);
        setAllSlider();
    }

    private void setAllSlider() {
        topSliderAdapter = new TopSliderAdapter(mContext);
        topSliderAdapter.setPagerList(topBannerPagerList);
        top_banner_sv.setAdapter(topSliderAdapter);
        top_banner_sv.startAutoScroll();
        top_banner_sv.setInterval(5000);
        top_banner_sv.setCycle(true);
        top_banner_sv.setStopScrollWhenTouch(true);
        top_banner_sv.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (top_banner_rg.getChildAt(position) != null) {
                    ((RadioButton) top_banner_rg.getChildAt(position)).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        middleSliderAdapter = new MiddleSliderAdapter(mContext);
        middleSliderAdapter.setPagerList(middleBannerPagerList);
        middle_banner_sv.setAdapter(middleSliderAdapter);
        middle_banner_sv.startAutoScroll();
        middle_banner_sv.setInterval(5000);
        middle_banner_sv.setCycle(true);
        middle_banner_sv.setStopScrollWhenTouch(true);
        middle_banner_sv.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (middle_banner_rg.getChildAt(position) != null) {
                    ((RadioButton) middle_banner_rg.getChildAt(position)).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        bottomSliderAdapter = new BottomSliderAdapter(mContext);
        bottomSliderAdapter.setPagerList(bottomBannerPagerList);
        bottom_banner_sv.setAdapter(bottomSliderAdapter);
        bottom_banner_sv.startAutoScroll();
        bottom_banner_sv.setInterval(5000);
        bottom_banner_sv.setCycle(true);
        bottom_banner_sv.setStopScrollWhenTouch(true);
        bottom_banner_sv.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (bottom_banner_rg.getChildAt(position) != null) {
                    ((RadioButton) bottom_banner_rg.getChildAt(position)).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        attemptGetProduct();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUserLogin()) {
            sign_in_ll.setVisibility(View.GONE);
        } else {
            sign_in_ll.setVisibility(View.VISIBLE);
        }
    }

    public boolean isUserLogin() {
        String user_id = SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID);
        String token = SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN);
        return !(TextUtils.isEmpty(user_id) && TextUtils.isEmpty(token) && user_id == null && token == null);
    }

    private void attemptGetProduct() {
        try {
            WaitDialog.showDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getHomeScreenDetails", json, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        switch (strfFrom.trim()) {
            case "getHomeScreenDetails": {
                if (result.optString("status").trim().equalsIgnoreCase("success")) {
                    setDataAfterResult(result);
                } else {
                    Methods.showToast(getActivity(), result.optString("msg"));
                }
                break;
            }
            case "banner": {
                break;
            }
        }
    }

    private void setDataAfterResult(JSONObject result) {
        JSONObject dataObject = result.optJSONObject("data");
        JSONObject bannerObject = dataObject.optJSONObject("banners");

        //Top Slider
        JSONArray topBannerArray = bannerObject.optJSONArray("slider");
        topSliderAdapter.setPagerList(setSlider(topBannerArray, topBannerPagerList));
        addRadioButtons(top_banner_rg, topBannerArray.length());

        //Middle Slider
        JSONArray middleBannerArray = bannerObject.optJSONArray("banners-middle");
        middleSliderAdapter.setPagerList(setSlider(middleBannerArray, middleBannerPagerList));
        addRadioButtons(middle_banner_rg, middleBannerArray.length());

        //Bottom Slider
        JSONArray bottomBannerArray = bannerObject.optJSONArray("banner-bottom");
        bottomSliderAdapter.setPagerList(setSlider(bottomBannerArray, bottomBannerPagerList));
        addRadioButtons(bottom_banner_rg, bottomBannerArray.length());

        //Set Type1 & Type2 Offers
        JSONObject offerObject = bannerObject.optJSONObject("offer");
        JSONArray type1Offer = offerObject.optJSONArray("type1");
        JSONArray type2Offer = offerObject.optJSONArray("type2");
        offerTypeOneAdapter.updateList(setSlider(type1Offer, offerTypeOneList));
        offerTypeTwoAdapter.updateList(setSlider(type2Offer, offerTypeTwoList));

        JSONArray categoryArray = bannerObject.optJSONArray("categories");

        JSONArray mostSearchedArray=dataObject.optJSONArray("most_searched");
        mostSearchedAdapter.updateList(setList(mostSearchedArray,mostSearchedList));

        JSONArray trendingNowArray=dataObject.optJSONArray("trending_now");
        trendingNowAdapter.updateList(setList(trendingNowArray,trendingNowList));

        JSONArray itemViewedArray=dataObject.optJSONArray("recently_viewed");
        itemViewedAdapter.updateList(setList(itemViewedArray,itemViewedList));
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
        // mPagerRg.addView(ll);
    }


    /*private void setPager(JSONArray dataObj) {
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
    }*/

    @Override
    public void onPause() {
        super.onPause();
        /*pagerView.stopAutoScroll();
        pagerView1.stopAutoScroll();
        pagerView2.stopAutoScroll();
        pagerView3.stopAutoScroll();*/
    }

    /*private void setProducts(JSONObject dataObj) {

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
            bannerDealAdapter.setList(productList);

            rv_featuredCategories.setAdapter(bannerDealAdapter);
            rv_featuredCategories1.setAdapter(bannerDealAdapter);
            rv_featuredCategories2.setAdapter(bannerDealAdapter);
            rv_featuredcategories3.setAdapter(bannerDealAdapter);
            rv_featuredcategories4.setAdapter(bannerDealAdapter);
            WaitDialog.hideDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*
    * Inflated all product category and items
    * */
    /*private void inflateData(int rowCount) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_feature_products_row, null);
        Object[] keys = mProductHashmap.keySet().toArray();
        ProductInflateView productInflateView = new ProductInflateView(view, rowCount, keys[rowCount].toString());
        view.setTag(productInflateView);

    }*/

    //inflate view
    /*class ProductInflateView {
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
    }*/


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_tv: {
                if (!isUserLogin()) {
                    ((HomeActivity) getActivity()).setParticulatTab(4);
                }
                break;
            }
        }
    }


    // Get user information
    /*private void attemptGetFeaturedProduct() {
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

    }*/

    // Get user information
    /*private void attemptGetFeaturedCategories() {
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

    }*/

    /*private void attemptPager() {

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
    }*/

    //Sorting list
    /*public void sortInAse() {
        Comparator<ProductModel> comparator = new Comparator<ProductModel>() {

            @Override
            public int compare(ProductModel object1, ProductModel object2) {


                return object1.isProduct.compareToIgnoreCase(object2.isProduct);
            }
        };
        Collections.sort(productList, comparator);

    }*/


    /*public void addRadioButtons(int number) {

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


    }*/

    @Override
    public void refreshListSuccess() {
        attemptGetProduct();
    }
}

