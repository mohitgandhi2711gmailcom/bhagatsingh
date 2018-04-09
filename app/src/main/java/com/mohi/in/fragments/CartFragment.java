package com.mohi.in.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.LoginActivityNew;
import com.mohi.in.activities.ShippingAddressActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.CartModel;
import com.mohi.in.model.CartModelNew;
import com.mohi.in.ui.adapter.CartAdapterNew;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.OnValueChangeListner;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuMediumTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartFragment extends Fragment implements ServerCallBack, OnValueChangeListner, View.OnClickListener, RefreshList {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;
    private View root;
    private RecyclerView rv_listView;
    private UbuntuMediumTextView tv_priceCurrencyType;
    private TextView tv_price;
    private Button but_checkOut;
    private LinearLayout ll_layout;
    private ArrayList<CartModel> cartList = new ArrayList<>();
    CartFragment fragment;

    public CartFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_cart, container, false);
        getControls();


        return root;
    }


    private void getControls() {

        rv_listView = (RecyclerView) root.findViewById(R.id.Cart_Listview);
        ll_layout = (LinearLayout) root.findViewById(R.id.Cart_CheckoutLayout);

        tv_price = root.findViewById(R.id.Cart_Price);
        tv_priceCurrencyType = (UbuntuMediumTextView) root.findViewById(R.id.Cart_PriceCurrencyType);
        but_checkOut = root.findViewById(R.id.Cart_Checkout);


    }

    private void setValue() {
        tv_priceCurrencyType.setText(" " + SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));
//        mCartAdapter = new CartAdapter(getActivity(), fragment, this, HomeActivityNew.homeActivityNew, CartFragment.this);
        CartAdapterNew mCartAdapter = new CartAdapterNew(getActivity(), getList());
        LinearLayoutManager mCartLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_listView.setLayoutManager(mCartLayoutManager);
        rv_listView.setAdapter(mCartAdapter);


        but_checkOut.setOnClickListener(this);

    }

    private ArrayList<CartModelNew> getList() {
        ArrayList<CartModelNew> list = new ArrayList<>();
        CartModelNew obj1 = new CartModelNew();
        obj1.setTitle("Title1");
        obj1.setAmount("$ 200");
        obj1.setColor("Red");
        obj1.setSize("L");
        obj1.setCounter(0);
        CartModelNew obj2 = new CartModelNew();
        obj2.setTitle("Title2");
        obj2.setAmount("$ 400");
        obj2.setColor("Black");
        obj2.setSize("M");
        obj2.setCounter(0);
        /*CartModelNew obj3=new CartModelNew();
        obj3.setTitle("Title3");
        obj3.setAmount("$ 600");
        obj3.setColor("Green");
        obj3.setSize("XL");
        obj3.setCounter(0);
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);*/
        return list;
    }


    @Override
    public void onResume() {
        super.onResume();
        setValue();
        attemptGetFeaturedCategories();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(getActivity());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // Get user information
    private void attemptGetFeaturedCategories() {
        try {
            WaitDialog.showDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.addProperty("width", getResources().getDimension(R.dimen.addtocart_row_image_width));
            json.addProperty("height", getResources().getDimension(R.dimen.addtocart_row_image_height));

            ServerCalling.ServerCallingProductsApiPost(getActivity(), "getCart", json, this);


        } catch (Exception e) {


            Log.e("CartFragment", "Exception attemptTOGetUserInfo: " + e.getMessage());
        }

    }


    private void setCartItem(JSONArray dataArray) {

        try {
            cartList.clear();


            int dataArraylength = dataArray.length();

            double totailFair = 0;


            for (int i = 0; i < dataArraylength; i++) {

                JSONObject dataJson = dataArray.getJSONObject(i);


                cartList.add(new CartModel(dataJson.getString("product_id"), dataJson.getString("item_id"), dataJson.getString("product_name"), dataJson.getString("product_price"),
                        dataJson.getString("image"), dataJson.getString("category"), dataJson.getString("qty"), dataJson.getString("quote_id"),
                        dataJson.getString("stock"), dataJson.getString("is_wishlist")));

                totailFair = totailFair + (Double.parseDouble(dataJson.getString("product_price")) * Double.parseDouble("" + dataJson.getInt("qty")));

            }


            Log.e("dsfdsfdsf", "SISISISISIS: " + cartList.size());

            if (cartList.size() == 0) {
                ll_layout.setVisibility(View.GONE);
            } else {

                ll_layout.setVisibility(View.VISIBLE);

            }

            // mCartAdapter.setList(cartList);


            tv_price.setText(Methods.getTwoDecimalVAlue("" + totailFair));
            WaitDialog.hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {


            if (strfFrom.trim().equalsIgnoreCase("getCart")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    JSONArray dataArray = result.getJSONArray("data");

                    setCartItem(dataArray);


                } else {

                    Methods.showToast(getActivity(), result.getString("msg"));

                    Log.e("CartFragment", "ServerCallBackSuccess log: " + result.getString("msg"));
                }
            }

        } catch (Exception e) {


            Log.e("CartFragment", "Exception ServerCallBackSuccess: " + e.getMessage());
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @Override
    public void onValueChange(int listSize) {

        double totailFair = 0;
        for (int i = 0; i < listSize; i++) {

            totailFair = totailFair + (Double.parseDouble(cartList.get(i).product_price) * Double.parseDouble("" + cartList.get(i).qty));

        }
        tv_price.setText(Methods.getTwoDecimalVAlue("" + totailFair));

        if (listSize == 0) {

            ll_layout.setVisibility(View.GONE);

        }


    }

    @Override
    public void onClick(View view) {

        Intent intent = null;

        switch (view.getId()) {

            case R.id.Cart_Checkout:

                String strQt = "";

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


                   /* if (SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESSID) == null || SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESSID).equalsIgnoreCase("")) {

                        intent = new Intent(getActivity(), AddAddressActivity.class);


                    } else */
                    {
                        intent = new Intent(getActivity(), ShippingAddressActivity.class);
                        String address=SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_STREET_ONE)+" "+SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_STREET_TWO)+" "+SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_CITY)+" "+SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_REGION)+" "+SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_POSTCODE);
                        intent.putExtra("Address", address);
                        intent.putExtra("AddressId", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ADDRESS_ID));
                        intent.putExtra("AddressName", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_STREET_ONE)+" "+SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_STREET_TWO));

                    }
                    intent.putExtra("ProductId", model.product_id);
                    intent.putExtra("From", "Cart");
                    intent.putExtra("From", strQt);
                    ShippingAddressActivity.itemList.clear();
                    ShippingAddressActivity.itemList.addAll(cartList);


                    startActivity(intent);

                }

                break;

        }

    }

    @Override
    public void refreshListSuccess() {
        attemptGetFeaturedCategories();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
