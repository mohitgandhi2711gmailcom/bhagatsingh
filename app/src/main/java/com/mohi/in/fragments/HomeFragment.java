package com.mohi.in.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ServerCallBack {

    private ViewPagerAdapter adapter;
    private ProductListFragment homeFragment;
    private ProductListFragment sareeFragment;
    private ProductListFragment bridalWearFragment;
    private ProductListFragment lehngaFragment;
    private ProductListFragment salwarFragment;

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
        homeFragment = new ProductListFragment();
        sareeFragment = new ProductListFragment();
        bridalWearFragment = new ProductListFragment();
        lehngaFragment = new ProductListFragment();
        salwarFragment = new ProductListFragment();
        adapter.addFrag(new BannerDealsFragment(), "Home");
        adapter.addFrag(homeFragment, "SAREE");
        adapter.addFrag(sareeFragment, "BRIDAL WEAR");
        adapter.addFrag(bridalWearFragment, "LEHANGA");
        adapter.addFrag(lehngaFragment, "SALWAR");
        adapter.addFrag(salwarFragment, "KURTA");
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    private void setFeaturedCategories(JSONArray dataArray) throws JSONException {

        Bundle bundle1 = new Bundle();
        String cat_id1 = dataArray.getJSONObject(0).getString("cat_id");
        String name1 = dataArray.getJSONObject(0).getString("name");
        String image1 = dataArray.getJSONObject(0).getString("image");
        bundle1.putString("cat_id", cat_id1);
        bundle1.putString("name", name1);
        bundle1.putString("image", image1);
        homeFragment.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        String cat_id2 = dataArray.getJSONObject(1).getString("cat_id");
        String name2 = dataArray.getJSONObject(1).getString("name");
        String image2 = dataArray.getJSONObject(1).getString("image");
        bundle2.putString("cat_id", cat_id2);
        bundle2.putString("name", name2);
        bundle2.putString("image", image2);
        sareeFragment.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        String cat_id3 = dataArray.getJSONObject(2).getString("cat_id");
        String name3 = dataArray.getJSONObject(2).getString("name");
        String image3 = dataArray.getJSONObject(2).getString("image");
        bundle3.putString("cat_id", cat_id3);
        bundle3.putString("name", name3);
        bundle3.putString("image", image3);
        bridalWearFragment.setArguments(bundle3);

        Bundle bundle4 = new Bundle();
        String cat_id4 = dataArray.getJSONObject(3).getString("cat_id");
        String name4 = dataArray.getJSONObject(3).getString("name");
        String image4 = dataArray.getJSONObject(3).getString("image");
        bundle4.putString("cat_id", cat_id4);
        bundle4.putString("name", name4);
        bundle4.putString("image", image4);
        lehngaFragment.setArguments(bundle4);

        Bundle bundle5 = new Bundle();
        String cat_id5 = dataArray.getJSONObject(4).getString("cat_id");
        String name5 = dataArray.getJSONObject(4).getString("name");
        String image5 = dataArray.getJSONObject(4).getString("image");
        bundle5.putString("cat_id", cat_id5);
        bundle5.putString("name", name5);
        bundle5.putString("image", image5);
        salwarFragment.setArguments(bundle5);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
