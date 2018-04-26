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
import com.mohi.in.model.RelatedProductModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.widgets.UbuntuMediumTextView;

import java.util.ArrayList;

public class ItemDetailsSimilarItemAdapter extends RecyclerView.Adapter<ItemDetailsSimilarItemAdapter.MyViewHolder> {

    private ArrayList<RelatedProductModel> list;
    private Context context;

    public void addData(ArrayList<RelatedProductModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_item;
        private ImageView Subcategories_Row_Image;
        private ImageView Subcategories_Row_Favorite;
        private UbuntuMediumTextView Subcategories_Row_Title;
        private UbuntuMediumTextView Subcategories_Row_Price;
        private UbuntuMediumTextView short_desc_tv;

        MyViewHolder(View view) {
            super(view);
            ll_item = view.findViewById(R.id.ll_item);
            Subcategories_Row_Image = view.findViewById(R.id.Subcategories_Row_Image);
            Subcategories_Row_Favorite = view.findViewById(R.id.Subcategories_Row_Favorite);
            Subcategories_Row_Title = view.findViewById(R.id.Subcategories_Row_Title);
            Subcategories_Row_Price = view.findViewById(R.id.Subcategories_Row_Price);
            short_desc_tv = view.findViewById(R.id.short_desc_tv);
        }
    }

    public ItemDetailsSimilarItemAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewed_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RelatedProductModel model = list.get(position);
        Glide.with(context).load(model.getImage_url()).into(holder.Subcategories_Row_Image);
        String isFavourite = model.getIs_wishlist();
        if (isFavourite.trim().equalsIgnoreCase("0")) {
            holder.Subcategories_Row_Favorite.setBackgroundResource(R.drawable.ic_love_like);
        } else {
            holder.Subcategories_Row_Favorite.setBackgroundResource(R.drawable.ic_love_fill);
        }
        holder.Subcategories_Row_Title.setText(model.getProduct_name());
//        holder.short_desc_tv.setText(model.getType());
        String price=model.getProduct_price();
        //price=price.substring(0,(price.length()-2));
        holder.Subcategories_Row_Price.setText(price);
        final String ProductId = model.getProduct_id();
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityItemDetails.class);
                intent.putExtra("ProductId", ProductId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}