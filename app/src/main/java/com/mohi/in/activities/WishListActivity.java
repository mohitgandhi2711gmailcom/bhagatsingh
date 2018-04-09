package com.mohi.in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
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
import com.mohi.in.widgets.UbuntuLightTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 12/10/17.
 */

public class WishListActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {

    private RecyclerView mCategoryRv;
    private LinearLayoutManager mLayoutManager;
    private Context mContext;
    private WishListAdapter mCategoryAdapter;
    private ArrayList<WishListModel> mCategoryList = new ArrayList<>();

    private MaterialRippleLayout mFilterLayout;
    HashMap<String, String> hashMap = new HashMap<>();


    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        hashMap = SessionStore.getUserDetails(WishListActivity.this, Common.userPrefName);

        getControls();
       attemptTOGetWishList();
    }

    private void getControls()
    {
        mContext = WishListActivity.this;
        mCategoryRv = (RecyclerView) findViewById(R.id.category_rv);




        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView) findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);

        setValue();


    }

    private void setValue(){
        mCategoryAdapter = new WishListAdapter(mContext);

        mLayoutManager = new LinearLayoutManager(mContext , LinearLayoutManager.VERTICAL , false);
        mCategoryRv.setLayoutManager(mLayoutManager);
        mCategoryList=new ArrayList<>();

        //header
        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.my_wish_list);




        iv_menu.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }



    /*
  * set dummy featured category data
  * */
    private void setFeaturedCategories(JSONArray dataArray ) {

        try {
            mCategoryList.clear();
            int dataArraylength = dataArray.length();

            for (int i=0; i<dataArraylength; i++){

                JSONObject dataJson = dataArray.getJSONObject(i);

                mCategoryList.add(new WishListModel(dataJson.getString("product_id"), dataJson.getString("product_name"), dataJson.getString("image"), dataJson.getString("qty"), dataJson.getString("category"), dataJson.getInt("is_add_to_cart"), dataJson.getDouble("rating"),"129.000"));
            }

            mCategoryAdapter.setList(mCategoryList);
            mCategoryRv.setAdapter(mCategoryAdapter);

        }catch (Exception e){


        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.Header_Menu:
                    finish();
                break;

            case R.id.Header_Search:

                Intent intent = new Intent(WishListActivity.this, SearchActivity.class);
                startActivity(intent);

                break;

        }
    }
    // Get user information
    private void attemptTOGetWishList(){
        try {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("user_id", hashMap.get(SessionStore.USER_ID));
            json.addProperty("token", hashMap.get(SessionStore.USER_TOKEN));
            json.addProperty("width", getResources().getDimension(R.dimen.wishlist_row_image_width));
            json.addProperty("height", getResources().getDimension(R.dimen.wishlist_row_image_width));

            ServerCalling.ServerCallingProductsApiPost(WishListActivity.this, "wishlist", json, this);


        }catch (Exception e){


            Log.e("WishListActivity Exce", "Exception attemptTOGetUserInfo: "+e.getMessage());
        }

    }

    //ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {


            if(strfFrom.trim().equalsIgnoreCase("wishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    JSONArray dataArray = result.getJSONArray("data");

                    setFeaturedCategories(dataArray);

                    WaitDialog.hideDialog();

                } else {

                    Methods.showToast(WishListActivity.this, result.getString("msg"));

                    Log.e("WishListActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            }


        }catch (Exception e){


            Log.e("WishListActivity Excep", "Exception attemptTOGetUserInfo ServerCallBackSuccess: "+e.getMessage());
        }
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
}
