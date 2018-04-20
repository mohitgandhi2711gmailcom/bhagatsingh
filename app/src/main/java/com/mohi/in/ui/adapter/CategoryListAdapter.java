package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.activities.AllProductsListActivity;
import com.mohi.in.model.FeaturedCategoryModel;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<FeaturedCategoryModel> mList = new ArrayList<>();
    private boolean flage = true;

    public CategoryListAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(ArrayList<FeaturedCategoryModel> list) {
        this.mList = list;
        flage = true;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.categories_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FeaturedCategoryModel model = mList.get(position);
        int totalWidth=mContext.getResources().getDisplayMetrics().widthPixels;
        int height=totalWidth/3;
        holder.mCategoryIv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        holder.mCategoryNameTv.setText(model.name);
        Glide.with(mContext).load(model.image).into(holder.mCategoryIv);
        holder.mCatecories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flage) {
                    onCallActivity(model);
                    flage = false;
                }
            }
        });

        holder.mCategoryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flage) {
                    onCallActivity(model);
                    flage = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCategoryIv;
        private TextView mCategoryNameTv;
        private CardView mCatecories;

        public ViewHolder(View itemView) {
            super(itemView);
            mCategoryIv = itemView.findViewById(R.id.iv_photo);
            mCategoryNameTv = itemView.findViewById(R.id.tv_name);
            mCatecories = itemView.findViewById(R.id.Categories_Row);
        }
    }

    private void onCallActivity(FeaturedCategoryModel model) {
        Intent intent = new Intent(mContext, AllProductsListActivity.class);
        intent.putExtra("name", model.name);
        intent.putExtra("cat_id", model.cat_id);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
    }
}
