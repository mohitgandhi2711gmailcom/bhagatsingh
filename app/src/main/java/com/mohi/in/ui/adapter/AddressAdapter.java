package com.mohi.in.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mohi.in.R;
import com.mohi.in.activities.ActivityShippingAddress;
import com.mohi.in.utils.listeners.deleteAddressListener;
import com.mohi.in.model.AddressModel;
import com.mohi.in.utils.Methods;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private ArrayList<AddressModel> addressList;
    private Context mContext;
    private deleteAddressListener listener;

    public AddressAdapter(ArrayList<AddressModel> addressList, @NonNull Context mContext, deleteAddressListener listener) {
        this.addressList = addressList;
        this.mContext = mContext;
        this.listener = listener;
    }

    public void updateList(ArrayList<AddressModel> addressList) {
        this.addressList = addressList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.fragment_add_shipping_address_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AddressModel model = addressList.get(position);
        String title = "AREA: " + model.getPostcode();
        holder.addressTitle.setText(title);
        String desc = model.getStreet_1() + " " + model.getStreet_2();
        holder.addressDesc.setText(desc);
        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ActivityShippingAddress.class).putExtra("model",model));
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getDefault_shipping()) {
                    Methods.showToast(mContext, "Unable to Delete Default Address");
                } else {
                    listener.deleteAddress(model.getAddress_id());
                }
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView addressTitle;
        private TextView addressDesc;
        private Button edit_btn;
        private Button delete_btn;

        MyViewHolder(View view) {
            super(view);
            addressTitle = view.findViewById(R.id.address_title);
            addressDesc = view.findViewById(R.id.address_desc_tv);
            edit_btn = view.findViewById(R.id.edit_btn);
            delete_btn = view.findViewById(R.id.delete_btn);
        }
    }
}