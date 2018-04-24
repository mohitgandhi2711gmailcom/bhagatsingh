package com.mohi.in.ui.adapter;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mohi.in.model.MediaModel;
import com.mohi.in.widgets.TouchImageView;

import java.util.ArrayList;


public class FullScreenImagePagerAdapterNew extends PagerAdapter {
    Context context;
    private ArrayList<MediaModel> imageList = new ArrayList<>();

    public FullScreenImagePagerAdapterNew(Context act, ArrayList<MediaModel> imageList) {
        this.imageList = imageList;
        context = act;
    }

    public int getCount() {
        return imageList.size();
    }

    public Object instantiateItem(ViewGroup collection, final int position) {
        MediaModel modelObject = imageList.get(position);
        TouchImageView img = new TouchImageView(collection.getContext());
        Glide.with(context).load(modelObject.getImageUrl()).into(img);
        collection.addView(img, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return img;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
