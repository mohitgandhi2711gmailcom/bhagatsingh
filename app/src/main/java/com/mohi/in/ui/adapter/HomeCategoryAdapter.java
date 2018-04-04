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
import com.mohi.in.model.FeaturedProductsModel;

import java.util.ArrayList;


/**
 * Created by admin on 11/10/17.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{


    private Context mContext;
    private ArrayList<FeaturedProductsModel> mList = new ArrayList<>();
    private boolean flage=true;


    public HomeCategoryAdapter(Context context)
    {
        this.mContext =  context;
    }

    public void setList(ArrayList<FeaturedProductsModel> list)
    {

        this.mList = list;
        flage=true;
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.featured_categories_row , parent ,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FeaturedProductsModel model = mList.get(position);
        holder.mCategoryNameTv.setText(model.product_name);
        Log.v("CateAdapter" , "imgUrlllll ********* "+model.image);




        Glide.with(mContext)
                .load(model.image)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .dontAnimate()
                .override(R.dimen.home_categouries_row_height, R.dimen.home_categouries_row_height)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        holder.mCategoryIv.setImageBitmap(resource);
                    }
                });



        /*Glide.with(mContext)
                .load(model.image)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(holder.mCategoryIv);
*/
        holder.mCategoryRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flage){
                    onCallActivity(model);
                    flage =false;
                }
            }
        });


        holder.mCategoryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flage){
                    onCallActivity(model);
                    flage =false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private CircularImageView mCategoryIv;
        private ImageView mCategoryIv1;
        private TextView mCategoryNameTv;
        private FrameLayout mCategoryRow;

        public ViewHolder(View itemView) {
            super(itemView);
            mCategoryIv = (CircularImageView) itemView.findViewById(R.id.iv_photo);
            mCategoryIv1 = (ImageView) itemView.findViewById(R.id.iv_photoUp);
            mCategoryNameTv = (TextView) itemView.findViewById(R.id.tv_name);

            mCategoryRow = (FrameLayout)itemView.findViewById(R.id.FeaturedCategory_row);
        }
    }

    private void onCallActivity(FeaturedProductsModel model ){
        Intent intent = new Intent(mContext, AllProductsListActivity.class);
        intent.putExtra("name", model.product_name);
        intent.putExtra("cat_id", model.product_id);
        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);



    }
}
