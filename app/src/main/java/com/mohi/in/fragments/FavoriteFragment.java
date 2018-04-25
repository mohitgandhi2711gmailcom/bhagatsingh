package com.mohi.in.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.WishListModel;
import com.mohi.in.ui.adapter.WishListAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteFragment extends Fragment implements ServerCallBack {
    private Context mContext;
    HashMap<String, String> hashMap;
    private RecyclerView mCategoryRv;
    private ArrayList<WishListModel> mCategoryList;
    private WishListAdapter mCategoryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        hashMap = new HashMap<>();
        mCategoryList = new ArrayList<>();
        mCategoryRv = view.findViewById(R.id.category_rv);
        mCategoryAdapter = new WishListAdapter(mContext);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mCategoryRv.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        attemptTOGetWishList();
    }

    private void attemptTOGetWishList() {
        try {
            WaitDialog.showDialog(mContext);
            JsonObject json = new JsonObject();
            String userId = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID);
            String token = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN);
            json.addProperty("user_id", userId);
            json.addProperty("token", token);
            json.addProperty("width", getResources().getDimension(R.dimen.wishlist_row_image_width));
            json.addProperty("height", getResources().getDimension(R.dimen.wishlist_row_image_width));
            ServerCalling.ServerCallingProductsApiPost(mContext, "wishlist", json, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            WaitDialog.hideDialog();
            if (strfFrom.trim().equalsIgnoreCase("wishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.getJSONArray("data");
                    setFeaturedCategories(dataArray);

                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFeaturedCategories(JSONArray dataArray) {
        try {
            mCategoryList.clear();
            int dataArraylength = dataArray.length();
            for (int i = 0; i < dataArraylength; i++) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                mCategoryList.add(new WishListModel(dataJson.getString("product_id"), dataJson.getString("product_name"), dataJson.getString("image"), dataJson.getString("qty"), dataJson.getString("category"), dataJson.getInt("is_add_to_cart"), dataJson.getDouble("rating"), "129.000"));
            }
            mCategoryAdapter.setList(mCategoryList);
            mCategoryRv.setAdapter(mCategoryAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
