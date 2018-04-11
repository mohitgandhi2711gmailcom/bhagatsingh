package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.model.HomeScreenSliderModel;
import java.util.ArrayList;

public class TopSliderAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<HomeScreenSliderModel> mList = new ArrayList<>();

    public TopSliderAdapter(Context context) {
        super();
        mContext = context;
    }

    public void setPagerList(ArrayList<HomeScreenSliderModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        HomeScreenSliderModel modelObject = mList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.row_home_pager, collection, false);
        ImageView imgView = layout.findViewById(R.id.img_view);
        Glide.with(mContext).load(modelObject.getImageUrl()).into(imgView);
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

}
