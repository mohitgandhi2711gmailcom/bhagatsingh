package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mohi.in.R;

public class SliderHomeAdapter extends PagerAdapter
{
    private Context mContext;
    private int[] layoutsCount;

    public SliderHomeAdapter(Context context, int[] layouts) {
        mContext=context;
        layoutsCount=layouts;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(layoutsCount[position], container, false);
        LinearLayout slider_screen_ll=view.findViewById(R.id.slider_screen_ll);
        setImage(slider_screen_ll,position);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layoutsCount.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    private void setImage(LinearLayout view,int position)
    {
        if(position==0)
        {
            view.setBackground(mContext.getResources().getDrawable(R.drawable.explore_new));
        }
        if(position==1)
        {
            view.setBackground(mContext.getResources().getDrawable(R.drawable.pay_new));
        }
        if(position==2)
        {
            view.setBackground(mContext.getResources().getDrawable(R.drawable.deliver_new));
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
