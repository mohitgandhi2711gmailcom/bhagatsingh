package com.mohi.in.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.LoginActivityNew;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.CartModel;
import com.mohi.in.ui.adapter.CartAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.CartFragmentEventsListener;
import com.mohi.in.utils.listeners.ServerCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//OnValueChangeListener & RefreshList  was Implemented, And Removed Temporary
public class CartFragment extends Fragment implements ServerCallBack, View.OnClickListener, CartFragmentEventsListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView cartRecyclerView;
    private TextView cartPrice;
    private ImageView backCircleButton;
    private Button checkOutButton;
    private Context mContext;
    private CartAdapter mCartAdapter;
    private ArrayList<CartModel> cartList;
    private SwipeRefreshLayout swipe_layout;
    private String cartId;
    private LinearLayout totalParent;
    private int REQUEST_CODE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        cartList = new ArrayList<>();
        cartRecyclerView = view.findViewById(R.id.cart_rv);
        cartPrice = view.findViewById(R.id.cart_price);
        backCircleButton = view.findViewById(R.id.back_circle_btn);
        checkOutButton = view.findViewById(R.id.checkout_btn);
        swipe_layout=view.findViewById(R.id.swipe_layout);
        totalParent = view.findViewById(R.id.total_parent);
        setValue();
    }

    private void setValue() {
        mCartAdapter = new CartAdapter(mContext, this);
        LinearLayoutManager mCartLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(mCartLayoutManager);
        cartRecyclerView.setAdapter(mCartAdapter);
        backCircleButton.setOnClickListener(this);
        checkOutButton.setOnClickListener(this);
        swipe_layout.setOnRefreshListener(this);
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
            json.addProperty(SessionStore.USER_ID, SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
            json.addProperty(SessionStore.USER_TOKEN, SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
            /*json.addProperty("width", getResources().getDimension(R.dimen.addtocart_row_image_width));
            json.addProperty("height", getResources().getDimension(R.dimen.addtocart_row_image_height));*/
            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getCart", json, this);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        switch (strfFrom.trim()) {
            case "getCart":
                if (result.optString(Common.API_STATUS).trim().equalsIgnoreCase("success")) {
                    JSONObject dataObject= result.optJSONObject("data");
                    cartId = dataObject.optString("id");
                    String subTotal = dataObject.optString("subtotal");
                    cartPrice.setText(SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE) + " " + Methods.getTwoDecimalVAlue("" + subTotal));
                    setCartItem(dataObject.optJSONArray("items"));
                } else {
                    Methods.showToast(getActivity(), result.optString("msg"));
                }
                break;

            case "removeItemFromCart":
                if (result.optString("status").trim().equalsIgnoreCase("success")) {
                    attemptGetCart();
                } else {
                    Methods.showToast(getActivity(), result.optString("msg"));
                }
                break;

            case "updateCartQty":
                if (result.optString("status").trim().equalsIgnoreCase("1")) {
                    attemptGetCart();
                } else {
                    Methods.showToast(getActivity(), result.optString("msg"));
                }
                break;

            default:
                Methods.showToast(mContext, "Unknown Error");
        }
    }

    private void setCartItem(JSONArray dataArray) {
        cartList.clear();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dataJson = dataArray.optJSONObject(i);
            CartModel obj = new CartModel();
            obj.setItemId(dataJson.optString("item_id"));
            obj.setProductId(dataJson.optString(Common.USER_PRODUCT_ID));
            obj.setProductName(dataJson.optString("name"));
            obj.setProductPrice(dataJson.optString("price"));
            obj.setQty(dataJson.optString("qty"));
            obj.setImage(dataJson.optString("image"));
            obj.setSku(dataJson.optString("sku"));
            cartList.add(obj);
        }
        if (cartList.size()>0){
            totalParent.setVisibility(View.VISIBLE);
        }else{
            totalParent.setVisibility(View.GONE);
        }
        mCartAdapter.updateList(cartList);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.checkout_btn:
                if (SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID).equals( "") || SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID) == null){
                    Intent intent = new Intent(getContext(), LoginActivityNew.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                //Intent intent = null;
                /*String strQt = "";

                if (cartList.size() > 0) {
                    strQt = cartList.get(0).quote_id;

                }


                if (SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_ID) == null ||
                        SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_ID).isEmpty()) {
                    intent = new Intent(getActivity(), LoginActivityNew.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

                } else {

                    CartModel model = cartList.get(0);


                   *//* if (SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESSID) == null || SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESSID).equalsIgnoreCase("")) {

                        intent = new Intent(getActivity(), AddAddressActivity.class);


                    } else *//*
                    {
                        intent = new Intent(getActivity(), ShippingAddressActivity.class);
                        String address = SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_STREET_ONE) + " " + SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_STREET_TWO) + " " + SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_CITY) + " " + SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_REGION) + " " + SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_POSTCODE);
                        intent.putExtra("Address", address);
                        intent.putExtra("AddressId", SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESS_ID));
                        intent.putExtra("AddressName", SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_STREET_ONE) + " " + SessionStore.getUserDetails(getActivity(), Common.USER_PREFS_NAME).get(SessionStore.USER_STREET_TWO));

                    }
                    intent.putExtra("ProductId", model.product_id);
                    intent.putExtra("From", "Cart");
                    intent.putExtra("From", strQt);
                    ShippingAddressActivity.itemList.clear();
                    ShippingAddressActivity.itemList.addAll(cartList);


                    startActivity(intent);*/

            case R.id.back_circle_btn:
                break;

            default:
                Methods.showToast(mContext, "Unknown Error");
        }
    }

    @Override
    public void cartEventListener(String event, CartModel model) {
        switch (event) {
            case "plus":
                attemptCartQuantityUpdate(model);
                break;

            case "minus":
                attemptCartQuantityUpdate(model);
                break;

            case "remove":
                attemptRemoveItemFromCart(model);
                break;

            default:
                Methods.showToast(mContext, "Error..");
        }
    }

    private void attemptRemoveItemFromCart(CartModel model) {
        WaitDialog.showDialog(mContext);
        JsonObject json = new JsonObject();
        if (SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID).equals("") || SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID) == null){
//            json.addProperty("sku", model.getSku());
        }else {
            json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
        }
        json.addProperty("item_id", model.getItemId());
        json.addProperty("cart_id", cartId);
        ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemFromCart", json, this);
    }

    private void attemptCartQuantityUpdate(CartModel model) {
        WaitDialog.showDialog(mContext);
        JsonObject json = new JsonObject();
        if (SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID).equals("") || SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID) == null){
            json.addProperty("sku", model.getSku());
        }else {
            json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
        }
        json.addProperty("cart_id", cartId);
        json.addProperty("item_id", model.getItemId());
        json.addProperty("qty", model.getQty());
        json.addProperty("product_id", model.getProductId());
        ServerCalling.ServerCallingProductsApiPost(mContext, "updateCartQty", json, this);
    }

    @Override
    public void onRefresh() {
        swipe_layout.setRefreshing(false);
        attemptGetCart();
    }

    /*@Override
    public void onValueChange(int listSize) {
       *//* double totailFair = 0;
        for (int i = 0; i < listSize; i++) {
            totailFair = totailFair + (Double.parseDouble(cartList.get(i).product_price) * Double.parseDouble("" + cartList.get(i).qty));
        }
        tv_price.setText(Methods.getTwoDecimalVAlue("" + totailFair));
        if (listSize == 0) {
            ll_layout.setVisibility(View.GONE);
        }*//*
    }
*/
    /*@Override
    public void refreshListSuccess() {
        attemptGetCart();
    }*/
}
