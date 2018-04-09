package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.model.CartModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.SessionStore;

import java.util.ArrayList;

public class CartAdapterNew extends RecyclerView.Adapter<CartAdapterNew.ViewHolder> {

    private Context mContext;
    private ArrayList<CartModel> mList = new ArrayList<>();

    public CartAdapterNew(Context context) {
        this.mContext = context;
    }

    public void updateList(ArrayList<CartModel> cartList) {
        this.mList = cartList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.addtocart_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CartModel model = mList.get(position);
        Glide.with(mContext).load(model.getImage()).into(holder.product_iv);
        holder.brand_name_tv.setText(model.getProduct_name());
        holder.product_price_tv.setText(SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE)+" "+model.getProduct_price().substring(0,(model.getProduct_price().length()-2)));
        holder.counter_tv.setText(model.getQty());
        holder.cross_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.showToast(mContext,"Working on Cross Button");
            }
        });
        holder.plus_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.showToast(mContext,"Working on Plus Button");
            }
        });
        holder.minus_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.showToast(mContext,"Working on Minus Button");
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_iv;
        private TextView brand_name_tv;
        private ImageView cross_iv;
        private TextView product_price_tv;
        private TextView color_tv;
        private TextView size_tv;
        private ImageView plus_btn_iv;
        private TextView counter_tv;
        private ImageView minus_btn_iv;

        public ViewHolder(View itemView) {
            super(itemView);
            product_iv = itemView.findViewById(R.id.product_iv);
            brand_name_tv = itemView.findViewById(R.id.brand_name_tv);
            cross_iv = itemView.findViewById(R.id.cross_iv);
            product_price_tv = itemView.findViewById(R.id.product_price_tv);
            color_tv = itemView.findViewById(R.id.color_tv);
            size_tv = itemView.findViewById(R.id.size_tv);
            plus_btn_iv = itemView.findViewById(R.id.plus_btn_iv);
            counter_tv = itemView.findViewById(R.id.counter_tv);
            minus_btn_iv = itemView.findViewById(R.id.minus_btn_iv);
        }
    }
}
