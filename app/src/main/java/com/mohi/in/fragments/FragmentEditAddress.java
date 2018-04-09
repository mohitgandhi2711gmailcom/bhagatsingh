package com.mohi.in.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.ActivityShippingAddress;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.listeners.deleteAddressListener;
import com.mohi.in.model.AddressModel;
import com.mohi.in.ui.adapter.AddressAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentEditAddress extends Fragment implements View.OnClickListener, ServerCallBack, deleteAddressListener {

    private Context mContext;
    private ArrayList<AddressModel> addressList;
    private RecyclerView address_rv;
    private ImageView plus_btn;
    private AddressAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_shipping_address, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        addressList = new ArrayList<>();
        plus_btn = view.findViewById(R.id.plus_btn);
        address_rv = view.findViewById(R.id.address_rv);
        setValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        attemptListAddress();
    }

    private void attemptListAddress() {
        try {
            WaitDialog.showDialog(mContext);
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
            ServerCalling.ServerCallingUserApiPost(mContext, "addresslist", json, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValue() {
        mAdapter = new AddressAdapter(addressList, mContext,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        address_rv.setLayoutManager(mLayoutManager);
        address_rv.setItemAnimator(new DefaultItemAnimator());
        address_rv.setAdapter(mAdapter);
        plus_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_btn:
                startActivity(new Intent(mContext, ActivityShippingAddress.class));
                break;
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject jobj, String strfFrom) {
        switch (strfFrom.trim()) {
            case "deleteaddress": {
                if (jobj.optString("status").trim().equalsIgnoreCase("success")) {
                    Methods.showToast(mContext,"Address Deleted Successfully");
                    attemptListAddress();
                } else {
                    Methods.showToast(mContext, jobj.optString("msg"));
                }
                break;
            }

            case "addresslist": {
                if (jobj.optString("status").trim().equalsIgnoreCase("success")) {
                    JSONArray dataArray = jobj.optJSONArray("data");
                    setAddress(dataArray);
                } else {
                    Methods.showToast(mContext, jobj.optString("msg"));
                }
                break;
            }

            default:
                break;
        }
    }

    private void setAddress(JSONArray dataArray) {
        WaitDialog.hideDialog();
        addressList.clear();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dataJson = dataArray.optJSONObject(i);
            AddressModel obj = new AddressModel();
            obj.setAddress_id(dataJson.optString("address_id"));
            obj.setFirstname(dataJson.optString("firstname"));
            obj.setLastname(dataJson.optString("lastname"));
            obj.setTelephone(dataJson.optString("telephone"));
            obj.setStreet_1(dataJson.optString("street_1"));
            obj.setStreet_2(dataJson.optString("street_2"));
            obj.setCity(dataJson.optString("city"));
            obj.setRegion(dataJson.optString("region"));
            obj.setPostcode(dataJson.optString("postcode"));
            obj.setCountry_id(dataJson.optString("country_id"));
            obj.setDefault_shipping(dataJson.optBoolean("default_shipping"));
            obj.setDefault_billing(dataJson.optBoolean("default_billing"));
            addressList.add(obj);
        }
        mAdapter.updateList(addressList);
    }

    @Override
    public void deleteAddress(String addressId) {
        attemptDeleteAddress(addressId);
    }

    private void attemptDeleteAddress(String addressId) {
        try {
            WaitDialog.showDialog(mContext);
            JsonObject json = new JsonObject();
            json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("address_id", addressId);
            json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
            ServerCalling.ServerCallingUserApiPost(mContext, "deleteaddress", json, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
