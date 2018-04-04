package com.mohi.in.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.mohi.in.model.orderHistoryModel;
import com.mohi.in.ui.adapter.OrderHistoryListAdapter;
import com.mohi.in.utils.CartCountCallBack;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuLightTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack, CartCountCallBack {

    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;

    private RecyclerView rv_OrderHistory;
    private OrderHistoryListAdapter mAdapter;
    ArrayList<orderHistoryModel> orderHistoryList = new ArrayList<orderHistoryModel>();
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        init();
    }


    //Initialize all controls
    private void init(){

        rv_OrderHistory  = (RecyclerView)findViewById(R.id.OrderHistory_Listview);

        //Header
        mrl_menu = (MaterialRippleLayout)findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout)findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout)findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView)findViewById(R.id.Header_Menu);
        iv_search = (ImageView)findViewById(R.id.Header_Search);
        iv_filter = (ImageView)findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView)findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);

        setValue();

    }

    //setValue
    private void setValue(){

        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.order_history);


        mLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        rv_OrderHistory.setLayoutManager(mLayoutManager);
        mAdapter = new OrderHistoryListAdapter(this, this);




        iv_menu.setOnClickListener(this);


        attemptGetOrderHistory();

    }



    //OnClickListener
    @Override
    public void onClick(View view) {

        Intent intent=null;

        switch (view.getId()){

            case R.id.Header_Menu:

                onBackPressed();
                break;

        }//end of switch

    }//end of onClick

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
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }

    private void setOrderHistory(JSONArray jArray){

        try {

            orderHistoryList.clear();

            int size = jArray.length();


            for (int i = 0; i<size; i++){

                JSONObject jdata = jArray.getJSONObject(i);

                orderHistoryList.add(new orderHistoryModel(jdata.getString("order_id"), jdata.getString("status"), jdata.getString("customer_id"), jdata.getString("tax_amount"),
                        jdata.getString("subtotal"), jdata.getString("discount_amount"), jdata.getString("grand_total"), jdata.getString("total_qty_ordered"),
                        jdata.getString("payment_method"), jdata.getString("product_id"), jdata.getString("product_name"), jdata.getString("image"), jdata.getString("status_text")));

            }

            mAdapter.setList(orderHistoryList);
            rv_OrderHistory.setAdapter(mAdapter);

        }catch (Exception e){



        }

    }


    private void attemptGetOrderHistory(){

        try {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(OrderHistoryActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(OrderHistoryActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));

            ServerCalling.ServerCallingProductsApiPost(OrderHistoryActivity.this, "getOrderHistory", json, this);


        }catch (Exception e){


            Log.e("HomeActivity Exception", "Exception attemptTOGetUserInfo: "+e.getMessage());
        }
    }




    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {

        try {

        WaitDialog.hideDialog();
        if(strfFrom.trim().equalsIgnoreCase("getOrderHistory")) {
            if (result.getString("status").trim().equalsIgnoreCase("1")) {

                JSONArray jArray = result.getJSONArray("data");
                setOrderHistory(jArray);


            } else {

                Methods.showToast(OrderHistoryActivity.this, result.getString("msg"));

                Log.e("HomeActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
            }
        }


    }catch (Exception e){


        Log.e("HomeActivity Exception", "Exception attemptTOGetUserInfo ServerCallBackSuccess: "+e.getMessage());
    }

    }

    @Override
    public void CartCountCallBackSuccess() {
        attemptGetOrderHistory();
    }
}
