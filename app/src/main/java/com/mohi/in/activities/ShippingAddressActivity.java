package com.mohi.in.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.balysv.materialripple.MaterialRippleLayout;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.CartModel;
import com.mohi.in.ui.adapter.ShippingRowAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.OnValueChangeListner;
import com.mohi.in.utils.RefreshList;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.ExpandableHeightdListView;
import com.mohi.in.widgets.UbuntuLightTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;
import com.mohi.in.widgets.UbuntuRegularButton;
import com.mohi.in.widgets.UbuntuRegularTextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShippingAddressActivity extends AppCompatActivity implements ServerCallBack, View.OnClickListener, OnValueChangeListner, RefreshList {

    private UbuntuMediumTextView tv_shippingAddress, tv_itemPrice, tv_deliveryPrice, tv_totalPrice, tv_finalTotalPrice, tv_currencyType;
    private UbuntuRegularButton but_chnageOrAddAddress, but_continue;

    private UbuntuRegularTextView tv_itemPriceLable;

    private String strProductName = "", strProductId = "", strProductPrice = "", strImage = "", strQuoteId = "", strAddressId = "", strAddress = "", strAddresName = "", strQty = "1", strFrom = "", strDeliveryPrice="0";
    private Intent intent;
    private int ADDRESSRESULTCODE = 100;
    private ScrollView sv_scrollView;

    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;
    private ShippingRowAdapter mShippingAdapter;
    public static ArrayList<CartModel> itemList = new ArrayList<>();
    private ExpandableHeightdListView rv_listView;
    private LinearLayoutManager mCartLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        intent = getIntent();


        if (intent.getStringExtra("ProductId") != null) {


            strProductId = intent.getStringExtra("ProductId");
            strAddressId = intent.getStringExtra("AddressId");
            strAddress = intent.getStringExtra("Address");
            strAddresName = intent.getStringExtra("AddressName");
            strProductName = intent.getStringExtra("ProductName");

            strFrom = intent.getStringExtra("From");

        }
        init();

    }

    private void init() {

        sv_scrollView = (ScrollView) findViewById(R.id.ShippingAddress_scrollView);

        rv_listView = (ExpandableHeightdListView) findViewById(R.id.ShippingAddress_ListItem);

        tv_currencyType = (UbuntuMediumTextView)findViewById(R.id.ShippingAddress_CurrencyType);

        tv_shippingAddress = (UbuntuMediumTextView) findViewById(R.id.ShippingAddress_Address);
        tv_itemPrice = (UbuntuMediumTextView) findViewById(R.id.ShippingAddress_Price);
        tv_deliveryPrice = (UbuntuMediumTextView) findViewById(R.id.ShippingAddress_DeliveryCharge);
        tv_totalPrice = (UbuntuMediumTextView) findViewById(R.id.ShippingAddress_TotalPrice);
        tv_finalTotalPrice = (UbuntuMediumTextView) findViewById(R.id.ShippingAddress_FinalTotalPrice);


        but_chnageOrAddAddress = (UbuntuRegularButton) findViewById(R.id.ShippingAddress_ChangeOrAddAddress);
        but_continue = (UbuntuRegularButton) findViewById(R.id.ShippingAddress_Continue);

        tv_itemPriceLable = (UbuntuRegularTextView) findViewById(R.id.ShippingAddress_PriceWithQty);


        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView) findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);
    }


    private void setValue() {


        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText("Deliver");

        tv_currencyType.setText(" "+SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));

        tv_shippingAddress.setText(strAddresName + "\n" + strAddress);

        mShippingAdapter = new ShippingRowAdapter(ShippingAddressActivity.this, this, this);


        rv_listView.setAdapter(mShippingAdapter);



        sv_scrollView.setSmoothScrollingEnabled(true);

        sv_scrollView.smoothScrollTo(0, 0);

        iv_menu.setOnClickListener(this);
        but_continue.setOnClickListener(this);
        but_chnageOrAddAddress.setOnClickListener(this);





        if (SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_ADDRESS_ID) != null
                && !SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_ADDRESS_ID).equalsIgnoreCase("")) {


            attemptgetDeliveryPrice();


        }else {

            strDeliveryPrice = "0";
            tv_deliveryPrice.setText(strDeliveryPrice+" "+SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));

Log.e("xzczxcxzc","TEST USER");
            mShippingAdapter.setList(itemList);
        }



    }

    private void attemptgetDeliveryPrice(){
        try {

            WaitDialog.showDialog(this);

            if(itemList.size()>0){

                strQuoteId = itemList.get(0).quote_id;


            }
            JsonObject json = new JsonObject();
            json.addProperty("quote_id", strQuoteId);
            json.addProperty("address_id", strAddressId);


            json.addProperty("user_id", SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_TOKEN));

//           {"user_id":"209" ,"token" : "dddd", "pincode" : "452001", "quote_id" : "3"}

            ServerCalling.ServerCallingProductsApiPost(ShippingAddressActivity.this, "getShippingPrice", json, this);


        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValue();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

       /* if (strFrom.trim().equalsIgnoreCase("Detail")) {

            Intent intent = new Intent(ShippingAddressActivity.this, ProductDetailActivity.class);
            intent.putExtra("ProductId", strProductId);

            startActivity(intent);
        }*/
        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }


    // onclick

    @Override
    public void onClick(View view) {

        Intent intent = null;

        switch (view.getId()) {

            case R.id.Header_Menu:

                onBackPressed();

                break;

            case R.id.ShippingAddress_Continue:
                if (SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_ADDRESS_ID) == null || SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_ADDRESS_ID).equalsIgnoreCase("")) {

                    Methods.showToast(ShippingAddressActivity.this, "Please add shipping address");


                }else {
                    PaymentMethodsActivity.cartList.clear();
                    PaymentMethodsActivity.cartList.addAll(itemList);


                    intent = new Intent(ShippingAddressActivity.this, PaymentMethodsActivity.class);

                    intent.putExtra("Address", strAddress);
                    intent.putExtra("AddressId", strAddressId);
                    intent.putExtra("AddressName", strAddresName);
                    intent.putExtra("ProductId", strProductId);
                    intent.putExtra("ProductPrice", tv_finalTotalPrice.getText().toString());
                    intent.putExtra("DeliveryPrice", strDeliveryPrice);

                    intent.putExtra("Qty", tv_itemPriceLable.getText().toString());
                    intent.putExtra("From", strFrom);
                    intent.putExtra("ProductName", strProductName);

                    startActivity(intent);
                    // finish();
                }

                break;

            case R.id.ShippingAddress_ChangeOrAddAddress:

                Log.e("sadas", "dsfsdf: " + strFrom);
                if (SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_ADDRESS_ID) == null || SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_ADDRESS_ID).equalsIgnoreCase("")) {
                    intent = new Intent(ShippingAddressActivity.this, AddAddressActivity.class);
                    intent.putExtra("ProductId", strProductId);
                    intent.putExtra("From", "Detail");
                    intent.putExtra("ProductName", strProductName);
                    startActivity(intent);
            }else {


                    intent = new Intent(ShippingAddressActivity.this, ModifyYourAddressActivity.class);
                    intent.putExtra("From", strFrom);
                    intent.putExtra("ProductId", strProductId);

                    startActivityForResult(intent, ADDRESSRESULTCODE);
            }
                break;


        }

    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {

            WaitDialog.hideDialog();

            if (strfFrom.trim().equalsIgnoreCase("getShippingPrice")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    JSONObject dataObj = result.getJSONObject("data");
                    strDeliveryPrice = dataObj.getString("amount");
                    tv_deliveryPrice.setText(strDeliveryPrice+" "+SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));


                    mShippingAdapter.setList(itemList);


                } else {

                    Methods.showToast(ShippingAddressActivity.this, result.getString("msg"));

                    Log.e("HomeFragment", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            }


    } catch (Exception ee) {

        Log.e("Login", "Login User Exception: " + ee.getMessage());
    }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("dfdsfdsf", "requestCode: " + requestCode + "  resultCode: " + resultCode);

        if (requestCode == ADDRESSRESULTCODE) {
            if (data != null) {
                strAddresName = data.getStringExtra("Name");
                strAddress = data.getStringExtra("Address");
                strAddressId = data.getStringExtra("Id");
                tv_shippingAddress.setText(strAddresName + "\n" + strAddress);
            }


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

    @Override
    public void onValueChange(int listSize) {

        double totailFair = 0;
        int itemCount = 0;
        for (int i = 0; i < listSize; i++) {

            totailFair = totailFair + (Double.parseDouble(itemList.get(i).product_price) * Double.parseDouble("" + itemList.get(i).qty));
            itemCount = itemCount + (Integer.parseInt("" + itemList.get(i).qty));

        }

        tv_itemPrice.setText(Methods.getTwoDecimalVAlue("" + totailFair) + " "+SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));

        double finalcalculatedValue = (totailFair + Float.parseFloat(strDeliveryPrice));
        tv_finalTotalPrice.setText(Methods.getTwoDecimalVAlue("" + finalcalculatedValue));
        tv_totalPrice.setText(Methods.getTwoDecimalVAlue("" + finalcalculatedValue) + " "+SessionStore.getUserDetails(ShippingAddressActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));
        tv_itemPriceLable.setText("Price (" + itemCount + " item)");





    }

    @Override
    public void refreshListSuccess() {
        setValue();
    }
}
