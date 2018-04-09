package com.mohi.in.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.CartModel;
import com.mohi.in.ui.adapter.CartAdapterNew;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.OnValueChangeListner;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.utils.listeners.ServerCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartFragment extends Fragment implements ServerCallBack, OnValueChangeListner, View.OnClickListener, RefreshList {

    private RecyclerView cart_rv;
    private TextView cart_price;
    private ImageView back_circle_btn;
    private Button checkout_btn;
    private Context mContext;
    private CartAdapterNew mCartAdapter;
    private ArrayList<CartModel> cartList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);
        return view;
    }


    private void init(View view) {
        mContext = getActivity();
        cartList = new ArrayList<>();
        cart_rv = view.findViewById(R.id.cart_rv);
        cart_price = view.findViewById(R.id.cart_price);
        back_circle_btn = view.findViewById(R.id.back_circle_btn);
        checkout_btn = view.findViewById(R.id.checkout_btn);
        setValue();
    }

    private void setValue() {
        mCartAdapter = new CartAdapterNew(mContext);
        LinearLayoutManager mCartLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        cart_rv.setLayoutManager(mCartLayoutManager);
        cart_rv.setAdapter(mCartAdapter);
        back_circle_btn.setOnClickListener(this);
        checkout_btn.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        attemptGetCart();
    }

    private void attemptGetCart() {
        try {
            WaitDialog.showDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.addProperty("width", getResources().getDimension(R.dimen.addtocart_row_image_width));
            json.addProperty("height", getResources().getDimension(R.dimen.addtocart_row_image_height));
            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getCart", json, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        switch (strfFrom.trim()) {
            case "getCart": {
                if (result.optString("status").trim().equalsIgnoreCase("1")) {
                    JSONArray dataArray = result.optJSONArray("data");
                    setCartItem(dataArray);
                } else {
                    Methods.showToast(getActivity(), result.optString("msg"));
                }
                break;
            }
        }
    }

    private void setCartItem(JSONArray dataArray) {
        cartList.clear();
        double total = 0;
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dataJson = dataArray.optJSONObject(i);
            CartModel obj = new CartModel();
            obj.setProduct_id(dataJson.optString("product_id"));
            obj.setProduct_id(dataJson.optString("item_id"));
            obj.setProduct_name(dataJson.optString("product_name"));
            obj.setProduct_price(dataJson.optString("product_price"));
            obj.setImage(dataJson.optString("image"));
            obj.setCategory(dataJson.optString("category"));
            obj.setQty(dataJson.optString("qty"));
            obj.setQuote_id(dataJson.optString("quote_id"));
            obj.setStock(dataJson.optString("stock"));
            obj.setIs_wishlist(dataJson.optString("is_wishlist"));
            cartList.add(obj);
            total = total + Double.parseDouble(dataJson.optString("product_price")) * Double.parseDouble(dataJson.optString("qty"));
        }
        mCartAdapter.updateList(cartList);
        cart_price.setText(SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE)+" "+ Methods.getTwoDecimalVAlue(String.valueOf(total)));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.checkout_btn:

                /*String strQt = "";

                if (cartList.size() > 0) {
                    strQt = cartList.get(0).quote_id;

                }


                if (SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID) == null ||
                        SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID).isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivityNew.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

                } else {

                    CartModel model = cartList.get(0);


                   *//* if (SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESSID) == null || SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESSID).equalsIgnoreCase("")) {

                        intent = new Intent(getActivity(), AddAddressActivity.class);


                    } else *//*
                    {
                        intent = new Intent(getActivity(), ShippingAddressActivity.class);
                        String address = SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_STREET_ONE) + " " + SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_STREET_TWO) + " " + SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_CITY) + " " + SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_REGION) + " " + SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_POSTCODE);
                        intent.putExtra("Address", address);
                        intent.putExtra("AddressId", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESS_ID));
                        intent.putExtra("AddressName", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_STREET_ONE) + " " + SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_STREET_TWO));

                    }
                    intent.putExtra("ProductId", model.product_id);
                    intent.putExtra("From", "Cart");
                    intent.putExtra("From", strQt);
                    ShippingAddressActivity.itemList.clear();
                    ShippingAddressActivity.itemList.addAll(cartList);


                    startActivity(intent);*/

//                }
//
//                break;

            case R.id.back_circle_btn:
                break;

        }

    }

    @Override
    public void onValueChange(int listSize) {

       /* double totailFair = 0;
        for (int i = 0; i < listSize; i++) {

            totailFair = totailFair + (Double.parseDouble(cartList.get(i).product_price) * Double.parseDouble("" + cartList.get(i).qty));

        }
        tv_price.setText(Methods.getTwoDecimalVAlue("" + totailFair));

        if (listSize == 0) {

            ll_layout.setVisibility(View.GONE);

        }*/


    }



    @Override
    public void refreshListSuccess() {
        attemptGetCart();
    }
}
