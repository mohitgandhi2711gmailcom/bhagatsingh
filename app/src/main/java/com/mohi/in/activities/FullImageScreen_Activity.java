package com.mohi.in.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;


import com.mohi.in.R;
import com.mohi.in.model.ProductImgModel;
import com.mohi.in.ui.adapter.FullImageScreenviewPager_Adapter;


public class FullImageScreen_Activity extends Activity {

    private ViewPager viewpag_viewPager;
    public static  ArrayList<ProductImgModel> mediaList = new ArrayList<ProductImgModel>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_full_image_screen_);



        maping();

    }



    //*********************** Maping **********************************************************************************************************************************************************

    private void maping(){

        viewpag_viewPager = (ViewPager)findViewById(R.id.FullIMageScreen_viewPager);
        setValue();
    }


    //************** Set Value ****************************************************************************************************************************************************************


    private void setValue(){

        viewpag_viewPager.setAdapter(new FullImageScreenviewPager_Adapter(FullImageScreen_Activity.this, mediaList));

//        viewpag_viewPager.setCurrentItem(pos);
    }



    //********** OnBack Press Method *******************************************************************************************************************************************************************
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        // overridePendingTransition(R.anim.left_side_in, R.anim.right_slide_out);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //     CrearCashMemmory.trimCache(FullImageScreen_Activity.this);
    }

}
