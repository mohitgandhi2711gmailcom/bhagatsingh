package com.mohi.in.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.mohi.in.R;
import com.mohi.in.model.MediaModel;
import com.mohi.in.ui.adapter.FullScreenImagePagerAdapterNew;

import java.util.ArrayList;

public class ActivityFullScreenImage extends Activity {

    private ViewPager viewpag_viewPager;
    private Context mContext;
    public static ArrayList<MediaModel> mediaList=new ArrayList<>();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_full_image_screen_);
        maping();
    }

    private void maping() {
        mContext = this;
        if(getIntent()!=null&&getIntent().hasExtra("position"))
        {
            position=getIntent().getIntExtra("position",0);
        }
        viewpag_viewPager = findViewById(R.id.FullIMageScreen_viewPager);
        viewpag_viewPager.setAdapter(new FullScreenImagePagerAdapterNew(mContext, mediaList));
        viewpag_viewPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
