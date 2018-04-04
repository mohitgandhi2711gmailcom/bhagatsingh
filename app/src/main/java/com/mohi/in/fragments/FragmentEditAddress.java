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
import android.widget.Button;
import android.widget.ImageView;

import com.mohi.in.R;
import com.mohi.in.activities.ActivityShippingAddress;
import com.mohi.in.model.AddressModel;
import com.mohi.in.ui.adapter.AddressAdapter;

import java.util.ArrayList;

public class FragmentEditAddress extends Fragment implements View.OnClickListener{

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_shipping_address,container,false);
        init(view);
        return view;
    }

    private void init(View view)
    {
        mContext=getActivity();
        ImageView plus_btn = view.findViewById(R.id.plus_btn);
        plus_btn.setOnClickListener(this);
        RecyclerView address_rv = view.findViewById(R.id.address_rv);
        ArrayList <AddressModel> list=getList();
        AddressAdapter mAdapter = new AddressAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        address_rv.setLayoutManager(mLayoutManager);
        address_rv.setItemAnimator(new DefaultItemAnimator());
        address_rv.setAdapter(mAdapter);
    }

    private ArrayList<AddressModel> getList() {
        ArrayList<AddressModel> list=new ArrayList<>();
        for(int i=0;i<3;i++)
        {
            AddressModel obj=new AddressModel();
            obj.setAddressTitle("Title"+i);
            obj.setAddressTitleDesc("Description"+i);
            list.add(obj);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.plus_btn:
                startActivity(new Intent(mContext, ActivityShippingAddress.class));
                break;
        }
    }
}
