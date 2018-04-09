package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.mohi.in.R;
import com.mohi.in.utils.listeners.imgSelectListener;
import com.mohi.in.model.ProductImgModel;

import java.util.ArrayList;

/**
 * Created by admin on 18/10/17.
 */

public class ProductsLeftImgAdapter extends RecyclerView.Adapter<ProductsLeftImgAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ProductImgModel> mList = new ArrayList<>();
    private imgSelectListener mListener;
    public ProductsLeftImgAdapter(Context context , imgSelectListener listener)
    {
        mContext = context;
        this.mListener = listener;
    }

    public void setList(ArrayList<ProductImgModel> list)
    {

        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_product_left_img , parent , false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ProductImgModel model = mList.get(position);
        //Show image

        if(model.isSelected) {
            holder.mImgView.setBackgroundColor(mContext.getResources().getColor(R.color.termsandcondition_color));
        }else
        {
            holder.mImgView.setBackgroundColor(mContext.getResources().getColor(R.color.white_color));
        }

        Glide.with(mContext)
                .load(model.thumbnail)
                .into(holder.mImgView);


        holder.mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length = mList.size();
                for(int i=0;i<length;i++) {
                    mList.get(i).isSelected = false;
                    if(i==position) {
                        mList.get(i).isSelected = true;
                        holder.mImgView.setBackgroundColor(mContext.getResources().getColor(R.color.termsandcondition_color));
                    }else
                    {
                        holder.mImgView.setBackgroundColor(mContext.getResources().getColor(R.color.white_color));
                    }
                }
                notifyDataSetChanged();
                mListener.selectImgPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mImgView;
        public ViewHolder(View itemView) {
            super(itemView);
            mImgView = (ImageView)  itemView.findViewById(R.id.product_iv);
        }
    }
}
