package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.model.CategoryModel;

import java.util.List;

public class BannerCategoryAdapter extends RecyclerView.Adapter<BannerCategoryAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private ItemClickListener mClickListener;
    private List<CategoryModel> mList;

    public BannerCategoryAdapter(Context context, List<CategoryModel> mList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    public void updateList(List<CategoryModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.category_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryModel obj = mList.get(position);
        int totalWidth=mContext.getResources().getDisplayMetrics().widthPixels;
        int height=totalWidth/3;
        holder.categoryItemIv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        if (checkNullablity(obj.getName())) {
            holder.categoryTitleTv.setText(obj.getName());
        }
        if (checkNullablity(obj.getImage())) {
            Glide.with(mContext).load(obj.getImage()).into(holder.categoryItemIv);
        } else {
            holder.categoryItemIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.gradient_blue_up_to_down));
        }
    }

    private boolean checkNullablity(String string) {
        return string != null && !TextUtils.isEmpty(string);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView categoryItemIv;
        private TextView categoryTitleTv;

        MyViewHolder(View itemView) {
            super(itemView);
            categoryItemIv = itemView.findViewById(R.id.category_item_iv);
            categoryTitleTv = itemView.findViewById(R.id.category_title_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public CategoryModel getItem(int id) {
        return mList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
