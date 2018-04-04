package com.mohi.in.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mohi.in.R;
import com.mohi.in.ui.adapter.SliderHomeAdapter;

public class SliderHomeActivity extends AppCompatActivity {
    private LinearLayout layoutDots;
    private TextView btn_skip;
    private int[] layouts;
    private ViewPager view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_slider);
        init();
    }

    private void init() {
        view_pager = findViewById(R.id.view_pager);
        layoutDots = findViewById(R.id.layoutDots);
        btn_skip = findViewById(R.id.btn_skip);
        layouts = new int[]{R.layout.slider_screen, R.layout.slider_screen, R.layout.slider_screen};
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPagerData();
    }

    private void setPagerData()
    {
        addBottomDots(0);
        SliderHomeAdapter viewPagerAdapter = new SliderHomeAdapter(this, layouts);
        view_pager.setAdapter(viewPagerAdapter);
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener);
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ((TextView) v).getText().toString();
                if (text.equalsIgnoreCase("Done")) {
                    startHomeActivity(HomeActivity.class);
                }
                if (text.equalsIgnoreCase("Skip")) {
                    startHomeActivity(HomeActivity.class);
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];
        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_inactive));
            layoutDots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.dot_active));
    }

    private void startHomeActivity(Class name) {
        Intent intent = new Intent(SliderHomeActivity.this, name);
        startActivity(intent);
        overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if(position==0)
            {
                btn_skip.setText("Skip");
            }
            if(position==1)
            {
                btn_skip.setText("Skip");
            }
            if(position==2)
            {
                btn_skip.setText("Done");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
}