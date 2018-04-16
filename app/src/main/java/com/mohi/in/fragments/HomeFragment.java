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
    private ProductListFragment sareeFragment;
    private ProductListFragment bridalWearFragment;
    private ProductListFragment lehngaFragment;
    private ProductListFragment salwarFragment;
    private ProductListFragment kurtaFragment;
    private static final String CATEGORY_ID ="cat_id";
    private static final String NAME ="name";
    private static final String IMAGE ="image";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);
        return v;
    }

    private void init(View v) {
        WaitDialog.showDialog(getActivity());
        ViewPager viewPager = v.findViewById(R.id.viewpager);
        TabLayout tabLayout = v.findViewById(R.id.tabs);
        adapter = new ViewPagerAdapter(getFragmentManager());
        tabLayout.setupWithViewPager(viewPager);

        sareeFragment = new ProductListFragment();
        bridalWearFragment = new ProductListFragment();
        lehngaFragment = new ProductListFragment();
        salwarFragment = new ProductListFragment();
        kurtaFragment = new ProductListFragment();
        adapter.addFrag(new BannerDealsFragment(), "Home");
        adapter.addFrag(sareeFragment, "SAREE");
        adapter.addFrag(bridalWearFragment, "BRIDAL WEAR");
        adapter.addFrag(lehngaFragment, "LEHANGA");
        adapter.addFrag(salwarFragment, "SALWAR");
        adapter.addFrag(kurtaFragment, "KURTA");
        viewPager.setAdapter(adapter);
        attemptGetFeaturedCategories();
    }

    // Get user information
    private void attemptGetFeaturedCategories() {
        try {
            WaitDialog.showDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("limit", "");
            json.addProperty("page", "");
            json.addProperty("width", Common.getDeviceHeightWidth(getActivity()).widthPixels);
            json.addProperty("height", getResources().getDimension(R.dimen.home_allcategories_row_height));
            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getCategories", json, this);
        } catch (Exception e) {
            Log.e("Error",e.toString());
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
                    Methods.showToast(getActivity(), jobj.getString("msg"));
                }
            }
        } catch (Exception e) {
            Log.e("Error",e.toString());
        }
    }

    private void setFeaturedCategories(JSONArray dataArray) throws JSONException {

        Bundle bundle1 = new Bundle();
        String catId1 = dataArray.getJSONObject(0).getString(CATEGORY_ID);
        String name1 = dataArray.getJSONObject(0).getString(NAME);
        String image1 = dataArray.getJSONObject(0).getString(IMAGE);
        bundle1.putString(CATEGORY_ID, catId1);
        bundle1.putString(NAME, name1);
        bundle1.putString(IMAGE, image1);
        sareeFragment.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        String catId2 = dataArray.getJSONObject(1).getString(CATEGORY_ID);
        String name2 = dataArray.getJSONObject(1).getString(NAME);
        String image2 = dataArray.getJSONObject(1).getString(IMAGE);
        bundle2.putString(CATEGORY_ID, catId2);
        bundle2.putString(NAME, name2);
        bundle2.putString(IMAGE, image2);
        bridalWearFragment.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        String catId3 = dataArray.getJSONObject(2).getString(CATEGORY_ID);
        String name3 = dataArray.getJSONObject(2).getString(NAME);
        String image3 = dataArray.getJSONObject(2).getString(IMAGE);
        bundle3.putString(CATEGORY_ID, catId3);
        bundle3.putString(NAME, name3);
        bundle3.putString(IMAGE, image3);
        lehngaFragment.setArguments(bundle3);

        Bundle bundle4 = new Bundle();
        String catId4 = dataArray.getJSONObject(3).getString(CATEGORY_ID);
        String name4 = dataArray.getJSONObject(3).getString(NAME);
        String image4 = dataArray.getJSONObject(3).getString(IMAGE);
        bundle4.putString(CATEGORY_ID, catId4);
        bundle4.putString(NAME, name4);
        bundle4.putString(IMAGE, image4);
        salwarFragment.setArguments(bundle4);

        Bundle bundle5 = new Bundle();
        String catId5 = dataArray.getJSONObject(4).getString(CATEGORY_ID);
        String name5 = dataArray.getJSONObject(4).getString(NAME);
        String image5 = dataArray.getJSONObject(4).getString(IMAGE);
        bundle5.putString(CATEGORY_ID, catId5);
        bundle5.putString(NAME, name5);
        bundle5.putString(IMAGE, image5);
        kurtaFragment.setArguments(bundle5);
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
