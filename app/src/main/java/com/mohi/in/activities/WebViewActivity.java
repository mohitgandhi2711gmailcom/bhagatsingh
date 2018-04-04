package com.mohi.in.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.utils.Urls;
import com.mohi.in.widgets.UbuntuLightTextView;

public class WebViewActivity extends AppCompatActivity {

    private WebView wv_webView;
    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;
    private Intent intent;
    private String strUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        intent = getIntent();

        if(intent.getStringExtra("URL")!=null){

            strUrl = intent.getStringExtra("URL");
        }


        init();

    }


    private void init(){


        wv_webView = (WebView)findViewById(R.id.WebView_webView);



        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView) findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);
        setValue();
    }


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


        if(strUrl.trim().equalsIgnoreCase(Urls.HELPURL)){

            tv_headerTilel.setText("Need Help");

        }else  if(strUrl.trim().equalsIgnoreCase(Urls.TEAMSANDCONDITIONURL)) {

            tv_headerTilel.setText("Terms and conditions");

        }else  if(strUrl.trim().equalsIgnoreCase(Urls.PRIVACYPOLICYURL)) {

            tv_headerTilel.setText("Privacy Policy");

        }else  if(strUrl.trim().equalsIgnoreCase(Urls.RETURNPOLICYURL)) {

            tv_headerTilel.setText("Return Policy");

        }

            wv_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        wv_webView.getSettings().setLoadWithOverviewMode(true);
//        wv_webView.getSettings().setUseWideViewPort(true);
       // wv_webView.getSettings().setMinimumFontSize(40);
        wv_webView.loadUrl(strUrl);



        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }
}


