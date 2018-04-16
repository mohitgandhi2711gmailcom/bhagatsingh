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
import com.mohi.in.model.ModifyAddressModel;
import com.mohi.in.ui.adapter.ModifyAdressRowAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.OnValueChangeListner;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuLightTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;
import com.mohi.in.widgets.UbuntuRegularButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ModifyYourAddressActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack, OnValueChangeListner, RefreshList {


    private LinearLayout ll_addAddress;
    private RecyclerView lv_listView;
    private UbuntuRegularButton btn_deliverHere;
    private ArrayList<ModifyAddressModel> addressList = new ArrayList<ModifyAddressModel>();
    private ModifyAdressRowAdapter mAddressAdapter ;
    private LinearLayoutManager mLayoutManager;
    private String tag = "ModifyYourAddressActivity", strAddressName="", strAddress="", strAddressId="";
    private boolean isShow=false;
    private UbuntuMediumTextView tv_noAddress;

    Intent intent;
    private boolean bool_ANA=true;



    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;
    private String strFrom="", strProductId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_your_address);

        intent = getIntent();

        if(intent.getStringExtra("From")!=null){
            isShow = true;
            strFrom = intent.getStringExtra("From");
            strProductId = intent.getStringExtra("ProductId");

            Log.e("dsfdsf","dsfdsf 123123: "+strFrom);

        }


        init();

    }

    private void init(){

        tv_noAddress = (UbuntuMediumTextView)findViewById(R.id.ModifyYourAddress_NoAddress);

        ll_addAddress = (LinearLayout)findViewById(R.id.ModifyYourAddress_AddNewAddressLayout);

        lv_listView = (RecyclerView) findViewById(R.id.ModifyYourAddress_Listview);

        btn_deliverHere = (UbuntuRegularButton) findViewById(R.id.ModifyYourAddress_DeliverHere);



        //Header
        mrl_menu = (MaterialRippleLayout)findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout)findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout)findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView)findViewById(R.id.Header_Menu);
        iv_search = (ImageView)findViewById(R.id.Header_Search);
        iv_filter = (ImageView)findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView)findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);

    }


    private void setValue(){

        bool_ANA =true;

        mAddressAdapter = new ModifyAdressRowAdapter(ModifyYourAddressActivity.this, false, this, strFrom, this);

        mLayoutManager = new LinearLayoutManager(ModifyYourAddressActivity.this , LinearLayoutManager.VERTICAL , false);
        lv_listView.setLayoutManager(mLayoutManager);

        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);


        iv_menu.setOnClickListener(this);
        ll_addAddress.setOnClickListener(this);
        btn_deliverHere.setOnClickListener(this);


        if(isShow){

           // btn_deliverHere.setVisibility(View.VISIBLE);
            tv_headerTilel.setText(R.string.delivery);

        }else{

           // btn_deliverHere.setVisibility(View.GONE);
            tv_headerTilel.setText(R.string.modify_address);

        }



        attemptGetAddress();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setValue();

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
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= null;

        Log.e("dfgdfgdf", "fgdfgdfg: "+addressList.size());

        if(addressList.size()==0) {
            if (strFrom.trim().equalsIgnoreCase("Detail")) {

                intent = new Intent(ModifyYourAddressActivity.this, ProductDetailActivity.class);
                intent.putExtra("ProductId", strProductId);
                startActivity(intent);

            }
        }


        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.Header_Menu:

                onBackPressed();
                break;

            case R.id.ModifyYourAddress_AddNewAddressLayout:

                if(bool_ANA) {
                    bool_ANA =false;
                    //Intent intent = new Intent(ModifyYourAddressActivity.this, AddAddressActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                }
                break;

            case R.id.ModifyYourAddress_DeliverHere:
                intent = new Intent();
                if(strAddressName.trim().equalsIgnoreCase("")){

                    if(addressList.size()>0){
                        ModifyAddressModel model = addressList.get(0);


                        String str = model.flat_no + ", " + model.street ;
                        if(!model.landmark.trim().equalsIgnoreCase("")){
                            str = str +model.landmark+", "+ model.city + ", " + model.state + ", " + model.postcode;

                        }else {
                            str = str + model.city + ", " + model.state + ", " + model.postcode;


                        }
                        intent.putExtra("Name", model.name);
                        intent.putExtra("Address", str);
                        intent.putExtra("Id", model.address_id);

                        setResult(RESULT_OK, intent);
                        finish();

                    }else {

                        onBackPressed();
                    }

                }else {
                    intent.putExtra("Name", strAddressName);
                    intent.putExtra("Address", strAddress);
                    intent.putExtra("Id", strAddressId);

                    setResult(RESULT_OK, intent);

                    finish();

                   onBackPressed();

                }


                break;


        }

    }


    private void attemptGetAddress(){



        try {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(ModifyYourAddressActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
            json.addProperty("token",  SessionStore.getUserDetails(ModifyYourAddressActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));

            ServerCalling.ServerCallingUserApiPost(ModifyYourAddressActivity.this, "getShippingAddress", json, this);


        }catch (Exception e){


            Log.e(tag, "Exception attemptTOGetUserInfo: "+e.getMessage());
        }

    }


    /*
  * set  featured category data
  * */
    private void setAddress(JSONArray dataArray) {
        WaitDialog.hideDialog();

        try {
            addressList.clear();
            int dataArraylength = dataArray.length();

            for (int i=0; i<dataArraylength; i++){

                JSONObject dataJson = dataArray.getJSONObject(i);
               // String address_id, String name, String mobile, String postcode, String city, String street, String state, boolean isOpen, boolean isSelected

                if(i==0){

                   /* addressList.add(new ModifyAddressModel(dataJson.getString("address_id"), dataJson.getString("name"), dataJson.getString("mobile"),
                            dataJson.getString("postcode"), dataJson.getString("city"),  dataJson.getString("street"), dataJson.getString("state"),
                            false, true));*/

                    addressList.add(new ModifyAddressModel(dataJson.getString("address_id"),dataJson.getString("name"),dataJson.getString("mobile"),
                            dataJson.getString("postcode"),dataJson.getString("city"),dataJson.getString("flat_no"),dataJson.getString("street"),
                            dataJson.getString("landmark"),dataJson.getString("state"),dataJson.getString("country"),false, true));

                }else {

                    addressList.add(new ModifyAddressModel(dataJson.getString("address_id"),dataJson.getString("name"),dataJson.getString("mobile"),
                            dataJson.getString("postcode"),dataJson.getString("city"),dataJson.getString("flat_no"),dataJson.getString("street"),
                            dataJson.getString("landmark"),dataJson.getString("state"),dataJson.getString("country"),false, false));

                }

            }

            mAddressAdapter.setList(addressList);
            lv_listView.setAdapter(mAddressAdapter);

            if(addressList.size()==0){

                tv_noAddress.setVisibility(View.VISIBLE);
                lv_listView.setVisibility(View.GONE);
                btn_deliverHere.setVisibility(View.GONE);

            }else{

                tv_noAddress.setVisibility(View.GONE);
                lv_listView.setVisibility(View.VISIBLE);
                if(isShow){
                    btn_deliverHere.setVisibility(View.VISIBLE);
                }else {
                    btn_deliverHere.setVisibility(View.GONE);
                }


            }


        }catch (Exception e){


        }
    }


    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {


            if(strfFrom.trim().equalsIgnoreCase("getShippingAddress")) {

                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    JSONArray dataArray = result.getJSONArray("data");

                    setAddress(dataArray);

                } else {

                    Methods.showToast(ModifyYourAddressActivity.this, result.getString("msg"));

                    Log.e(tag, "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }

            }


        }catch (Exception e){


            Log.e(tag, "Exception attemptTOGetUserInfo ServerCallBackSuccess: "+e.getMessage());
        }

    }





    @Override
    public void onValueChange(int listSize) {

        ModifyAddressModel model = addressList.get(listSize);

        String str = model.flat_no + ", " + model.street ;
        if(!model.landmark.trim().equalsIgnoreCase("")){
            str = str +model.landmark+", "+ model.city + ", " + model.state + ", " + model.postcode;

        }else {
            str = str + model.city + ", " + model.state + ", " + model.postcode;


        }
        strAddress =str;
        strAddressId = model.address_id;
        strAddressName = model.name;


    }

    @Override
    public void refreshListSuccess() {
        attemptGetAddress();
    }
}
