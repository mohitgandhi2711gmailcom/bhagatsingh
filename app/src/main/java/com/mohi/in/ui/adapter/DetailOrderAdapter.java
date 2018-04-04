package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mohi.in.R;
import com.mohi.in.model.DetailOrderModel;
import com.mohi.in.model.OrderModelNew;

import java.util.ArrayList;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.MyViewHolder> {

    private ArrayList<DetailOrderModel> list;
    private OrderModelNew obj;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView order_image;
        private TextView brand_tv, brand_details_tv, amount_tv,size_tv,color_tv;

        MyViewHolder(View view) {
            super(view);
            order_image = view.findViewById(R.id.order_image);
            brand_tv = view.findViewById(R.id.brand_tv);
            brand_details_tv = view.findViewById(R.id.brand_details_tv);
            amount_tv = view.findViewById(R.id.amount_tv);
            size_tv=view.findViewById(R.id.size_tv);
            color_tv=view.findViewById(R.id.color_tv);
        }
    }


    public DetailOrderAdapter(Context context,OrderModelNew obj) {
        this.list = obj.getList();
        this.context=context;
        this.obj=obj;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_order_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.order_image.setImageResource(R.drawable.google);
        holder.brand_tv.setText(obj.getBrandName());
        holder.brand_details_tv.setText(obj.getBrandDetails());
        holder.amount_tv.setText("RP " + String.valueOf(obj.getAmount()));
        holder.color_tv.setText(list.get(position).getColor());
        holder.size_tv.setText(list.get(position).getSize());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}