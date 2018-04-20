package com.mohi.in.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.listeners.ServerCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ServerCallBack {

    private ViewPagerAdapter adapter;
    /*private ProductListFragment sareeFragment;
    private ProductListFragment bridalWearFragment;
    private ProductListFragment lehngaFragment;
    private ProductListFragment salwarFragment;
    private ProductListFragment kurtaFragment;*/
    private static final String CATEGORY_ID = "cat_id";
    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);
        return v;
    }

    private void init(View v) {
        mContext = getActivity();
        ViewPager viewPager = v.findViewById(R.id.viewpager);
        TabLayout tabLayout = v.findViewById(R.id.tabs);
        adapter = new ViewPagerAdapter(getFragmentManager());
        tabLayout.setupWithViewPager(viewPager);

//        sareeFragment = new ProductListFragment();
//        bridalWearFragment = new ProductListFragment();
//        lehngaFragment = new ProductListFragment();
//        salwarFragment = new ProductListFragment();
//        kurtaFragment = new ProductListFragment();
//        adapter.addFrag(new BannerDealsFragment(), "Home");
//        adapter.addFrag(sareeFragment, "SAREE");
//        adapter.addFrag(bridalWearFragment, "BRIDAL WEAR");
//        adapter.addFrag(lehngaFragment, "LEHANGA");
//        adapter.addFrag(salwarFragment, "SALWAR");
//        adapter.addFrag(kurtaFragment, "KURTA");
        adapter.addFrag(new BannerDealsFragment(), "Home");
        viewPager.setAdapter(adapter);
        attemptGetFeaturedCategories();
    }

    // Get user information
    private void attemptGetFeaturedCategories() {
        try {
            WaitDialog.showDialog(mContext);
            JsonObject json = new JsonObject();
            json.addProperty("limit", "");
            json.addProperty("page", "");
            json.addProperty("width", Common.getDeviceHeightWidth(mContext).widthPixels);
            json.addProperty("height", getResources().getDimension(R.dimen.home_allcategories_row_height));
            ServerCalling.ServerCallingProductsApiPost(mContext, "getCategories", json, this);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject jobj, String strfFrom) {
        try {
            if (strfFrom.trim().equalsIgnoreCase("getCategories")) {
                if (jobj.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = jobj.getJSONArray("data");
                    setFeaturedCategories(dataArray);
                    WaitDialog.hideDialog();
                } else {
                    Methods.showToast(mContext, jobj.getString("msg"));
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    private void setFeaturedCategories(JSONArray dataArray) throws JSONException {

        ArrayList<String> stringName = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            Bundle bundle = new Bundle();
            String catId = dataArray.optJSONObject(i).getString(CATEGORY_ID);
            String name = dataArray.optJSONObject(i).getString(NAME);
            String image = dataArray.optJSONObject(i).getString(IMAGE);
            if (!stringName.contains(name.toLowerCase())) {
                bundle.putString(CATEGORY_ID, catId);
                bundle.putString(NAME, name);
                bundle.putString(IMAGE, image);
                ProductListFragment dataFragment = new ProductListFragment();
                adapter.addFrag(dataFragment, name);
                dataFragment.setArguments(bundle);
            }
            stringName.add(name.toLowerCase());
        }
        adapter.notifyDataSetChanged();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
