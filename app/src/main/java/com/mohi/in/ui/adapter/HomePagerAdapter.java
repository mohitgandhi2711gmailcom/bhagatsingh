package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;

import com.koushikdutta.ion.builder.AnimateGifMode;
import com.mohi.in.R;
import com.mohi.in.model.HomePagerModel;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class HomePagerAdapter  extends PagerAdapter {

    private Context mContext;
    private ArrayList<HomePagerModel> mList = new ArrayList<>();

    public HomePagerAdapter(Context context) {
        mContext = context;
    }

    public void setPagerList(ArrayList<HomePagerModel> list) {

        this.mList = list;
        notifyDataSetChanged();
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        HomePagerModel modelObject = mList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.row_home_pager, collection, false);
        ImageView imgView = layout.findViewById(R.id.img_view);

        Glide.with(mContext)
                .load(modelObject.name)
                .into(imgView);

        //For gif image
      /*  Glide.with(mContext).load("")
                .thumbnail(Glide.with(mContext).load(R.drawable.giphy))
                .fitCenter()
                .crossFade()
                .into(imgView);*/



        collection.addView(layout);



        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            ModelObject customPagerEnum = ModelObject.values()[position];
//            return mContext.getString(customPagerEnum.getTitleResId());
//        }

}