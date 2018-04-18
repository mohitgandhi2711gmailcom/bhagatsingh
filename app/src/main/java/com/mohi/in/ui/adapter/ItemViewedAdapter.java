package com.mohi.in.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.activities.ActivityItemDetails;
import com.mohi.in.common.Common;
import com.mohi.in.model.BannerModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuMediumTextView;

import java.util.ArrayList;

public class ItemViewedAdapter extends RecyclerView.Adapter<ItemViewedAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BannerModel> mList;

    public ItemViewedAdapter(Context context, ArrayList<BannerModel> list) {
        mList = new ArrayList<>();
        this.mContext = context;
        this.mList = mList;
    }

    public void updateList(ArrayList<BannerModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_viewed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BannerModel model = mList.get(position);
        Glide.with(mContext).load(model.getImage()).into(holder.Subcategories_Row_Image);
        String isFavourite = model.getIs_wishlist();
        if (isFavourite.trim().equalsIgnoreCase("0")) {
            holder.Subcategories_Row_Favorite.setBackgroundResource(R.drawable.ic_love_like);
        } else {
            holder.Subcategories_Row_Favorite.setBackgroundResource(R.drawable.ic_love_fill);
        }
        holder.Subcategories_Row_Title.setText(model.getProduct_name());
        holder.short_desc_tv.setText(model.getProduct_name());
        holder.Subcategories_Row_Price.setText(SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE) + " " + Methods.twoDigitFormat(model.getProduct_price()));
        final String ProductId = model.getProduct_id();
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityItemDetails.class);
                intent.putExtra("ProductId", ProductId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_item;
        private ImageView Subcategories_Row_Image;
        private ImageView Subcategories_Row_Favorite;
        private UbuntuMediumTextView Subcategories_Row_Title;
        private UbuntuMediumTextView short_desc_tv;
        private UbuntuMediumTextView Subcategories_Row_Price;

        ViewHolder(View view) {
            super(view);
            ll_item = view.findViewById(R.id.ll_item);
            Subcategories_Row_Image = view.findViewById(R.id.Subcategories_Row_Image);
            Subcategories_Row_Favorite = view.findViewById(R.id.Subcategories_Row_Favorite);
            Subcategories_Row_Title = view.findViewById(R.id.Subcategories_Row_Title);
            Subcategories_Row_Price = view.findViewById(R.id.Subcategories_Row_Price);
            short_desc_tv = view.findViewById(R.id.short_desc_tv);
        }
    }
}
