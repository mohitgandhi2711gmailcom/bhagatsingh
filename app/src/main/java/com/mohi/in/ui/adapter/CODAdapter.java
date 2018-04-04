package com.mohi.in.ui.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mohi.in.R;
import com.mohi.in.activities.AllProductsListActivity;
import com.mohi.in.model.CODModel;
import com.mohi.in.model.FeaturedProductsModel;

import java.util.ArrayList;

public class CODAdapter extends RecyclerView.Adapter<CODAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<CODModel> mList = new ArrayList<>();
    private boolean flage = true;


    public CODAdapter(Context context, ArrayList<CODModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cod_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CODModel model= mList.get(position);
        holder.title_tv.setText(model.getTitlename());
        holder.desc_tv.setText(model.getDesc());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title_tv,desc_tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            title_tv= itemView.findViewById(R.id.title_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
        }
    }
}
