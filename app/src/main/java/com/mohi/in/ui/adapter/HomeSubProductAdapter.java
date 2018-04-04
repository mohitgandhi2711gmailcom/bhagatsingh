package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohi.in.R;
import com.mohi.in.model.HomeProductModel;

import java.util.ArrayList;

/**
 * Created by admin on 12/10/17.
 */

public class HomeSubProductAdapter extends RecyclerView.Adapter<HomeSubProductAdapter.ViewHolder>{


    private Context mContext;
    private ArrayList<HomeProductModel> mProductList = new ArrayList<>();
    public HomeSubProductAdapter(Context context)
    {
        this.mContext = context;
    }

    public void setProductList(ArrayList<HomeProductModel> list)
    {

        this.mProductList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_feature_subproducts_row , parent , false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeProductModel model = mProductList.get(position);
        holder.mFeaturedProductsRowTitleTv.setText(model.name);

    }

    @Override
    public int getItemCount() {
        return mProductList!=null ? mProductList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mFeaturedProductsRowTitleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mFeaturedProductsRowTitleTv = (TextView) itemView.findViewById(R.id.FeaturedProducts_Row_Title);
        }
    }


}
