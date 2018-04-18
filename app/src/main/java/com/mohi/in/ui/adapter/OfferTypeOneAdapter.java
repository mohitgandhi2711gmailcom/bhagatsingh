package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.model.HomeScreenSliderModel;

import java.util.ArrayList;

public class OfferTypeOneAdapter extends RecyclerView.Adapter<OfferTypeOneAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<HomeScreenSliderModel> mList = new ArrayList<>();

    public OfferTypeOneAdapter(Context context,ArrayList<HomeScreenSliderModel> mList) {
        this.mContext = context;
        this.mList=mList;
    }

    public void updateList(ArrayList<HomeScreenSliderModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.offer_type_one_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HomeScreenSliderModel model = mList.get(position);
        Glide.with(mContext).load(model.getImageUrl()).into(holder.offer_type_iv);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView offer_type_iv;

        public ViewHolder(View itemView) {
            super(itemView);
            offer_type_iv = itemView.findViewById(R.id.offer_type_iv);
        }
    }
}
