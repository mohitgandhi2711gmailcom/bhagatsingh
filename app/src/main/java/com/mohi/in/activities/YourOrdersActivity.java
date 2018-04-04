package com.mohi.in.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mohi.in.R;
import com.mohi.in.utils.Methods;
import com.mohi.in.widgets.UbuntuLightTextView;

public class YourOrdersActivity extends AppCompatActivity implements View.OnClickListener{


    private LinearLayout ll_vyoh, ll_downloads, ll_yrps, ll_vyrrs, ll_yts, ll_rps;



    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;


    boolean VYOHFlag =true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);


        init();
    }


    //init
    private void init(){
        ll_vyoh = (LinearLayout)findViewById(R.id.YourOrders_VYOH);
        ll_downloads = (LinearLayout)findViewById(R.id.YourOrders_Downloads);
        ll_rps = (LinearLayout)findViewById(R.id.YourOrders_RPS);
        ll_vyrrs = (LinearLayout)findViewById(R.id.YourOrders_VYRR);
        ll_yrps = (LinearLayout)findViewById(R.id.YourOrders_YRP);
        ll_yts = (LinearLayout)findViewById(R.id.YourOrders_YTS);



        //Header
        mrl_menu = (MaterialRippleLayout)findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout)findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout)findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView)findViewById(R.id.Header_Menu);
        iv_search = (ImageView)findViewById(R.id.Header_Search);
        iv_filter = (ImageView)findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView)findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);

        setValue();

    }


    @Override
    protected void onResume() {
        super.onResume();

        VYOHFlag =true;
    }

    //setValue
    private void setValue(){


        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.nav_your_orders);

        ll_vyoh.setOnClickListener(this);
        ll_downloads.setOnClickListener(this);
        ll_rps.setOnClickListener(this);
        ll_vyrrs.setOnClickListener(this);
        ll_yrps.setOnClickListener(this);
        ll_yts.setOnClickListener(this);
        iv_menu.setOnClickListener(this);

    }


    //OnClickListener
    @Override
    public void onClick(View view) {

        Intent intent=null;

        switch (view.getId()){

            case R.id.YourOrders_VYOH:
                if(VYOHFlag) {

                    VYOHFlag = false;
                    intent = new Intent(YourOrdersActivity.this, OrderHistoryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                }
                break;

            case R.id.Header_Menu:

                onBackPressed();
                break;

        }//end of switch

    }//end of onClick

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
