package com.mohi.in.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.activities.ActivityItemDetails;
import com.mohi.in.common.Common;
import com.mohi.in.model.BannerModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuMediumTextView;

import java.util.ArrayList;
import java.util.List;

public class ItemViewedAdapter extends RecyclerView.Adapter<ItemViewedAdapter.MyViewHolder> {
    private Context mContext;
    private List<BannerModel> mList;

    public ItemViewedAdapter(Context context, List<BannerModel> list) {
        mList = new ArrayList<>();
        this.mContext = context;
        this.mList = list;
    }

    public void updateList(List<BannerModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_viewed_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final BannerModel model = mList.get(position);
        int totalWidth=mContext.getResources().getDisplayMetrics().widthPixels;
        int width=((totalWidth*240)/720);
        int height=(316*width)/240;
        holder.subcategoriesRowImage.setLayoutParams(new RelativeLayout.LayoutParams(width, height));

        Glide.with(mContext).load(model.getImage()).into(holder.subcategoriesRowImage);
        String isFavourite = model.getIs_wishlist();
        if (isFavourite.trim().equalsIgnoreCase("0")) {
            holder.subcategoriesRowFavorite.setBackgroundResource(R.drawable.ic_love_like);
        } else {
            holder.subcategoriesRowFavorite.setBackgroundResource(R.drawable.ic_love_fill);
        }
        holder.subcategoriesRowTitle.setText(model.getProduct_name());
        holder.shortDescriptionTextView.setText(model.getProduct_name());
        String price=SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE) + " " + Methods.twoDigitFormat(model.getProduct_price());
        holder.subcategoriesRowPrice.setText(price);
        final String ProductId = model.getProduct_id();
        holder.itemLinearLayout.setOnClickListener(new View.OnClickListener() {
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


    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemLinearLayout;
        private ImageView subcategoriesRowImage;
        private ImageView subcategoriesRowFavorite;
        private UbuntuMediumTextView subcategoriesRowTitle;
        private UbuntuMediumTextView shortDescriptionTextView;
        private UbuntuMediumTextView subcategoriesRowPrice;

        MyViewHolder(View view) {
            super(view);
            itemLinearLayout = view.findViewById(R.id.ll_item);
            subcategoriesRowImage = view.findViewById(R.id.Subcategories_Row_Image);
            subcategoriesRowFavorite = view.findViewById(R.id.Subcategories_Row_Favorite);
            subcategoriesRowTitle = view.findViewById(R.id.Subcategories_Row_Title);
            subcategoriesRowPrice = view.findViewById(R.id.Subcategories_Row_Price);
            shortDescriptionTextView = view.findViewById(R.id.short_desc_tv);
        }
    }
}
