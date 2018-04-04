package com.mohi.in.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohi.in.R;
import com.mohi.in.model.AddressModel;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private ArrayList<AddressModel> list;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView addresstitle, addressDesc;

        MyViewHolder(View view) {
            super(view);
            addresstitle = view.findViewById(R.id.address_title);
            addressDesc = view.findViewById(R.id.address_desc_tv);
        }
    }

    public AddressAdapter(ArrayList<AddressModel> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_add_shipping_address_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AddressModel model = list.get(position);
        String title=model.getAddressTitle();
        holder.addresstitle.setText(title);
        String desc=model.getAddressTitleDesc();
        holder.addressDesc.setText(desc);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}