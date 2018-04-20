package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.model.HomeScreenSliderModel;

import java.util.ArrayList;

public class OfferTypeTwoAdapter extends RecyclerView.Adapter<OfferTypeTwoAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<HomeScreenSliderModel> mList = new ArrayList<>();

    public OfferTypeTwoAdapter(Context context,ArrayList<HomeScreenSliderModel> mList) {
        this.mContext = context;
        this.mList=mList;
    }

    public void updateList(ArrayList<HomeScreenSliderModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.offer_type_two_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HomeScreenSliderModel model = mList.get(position);
        int totalWidth=mContext.getResources().getDisplayMetrics().widthPixels;
        int width=((totalWidth*405)/650);
        int height=(200*width)/405;
        holder.offerTypeImageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        Glide.with(mContext).load(model.getImageUrl()).into(holder.offerTypeImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView offerTypeImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            offerTypeImageView = itemView.findViewById(R.id.offer_type_iv);
        }
    }
}
