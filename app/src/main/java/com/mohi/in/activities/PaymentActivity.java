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

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener{

    private String strProductName = "", strProductId = "", strProductPrice = "", strImage = "", strQuoteId = "", strAddressId = "", strAddress = "", strAddresName = "", strQty = "0", strType="";
    private Intent intent;


    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        intent = getIntent();


        if (intent.getStringExtra("ProductName") != null) {

            strProductName = intent.getStringExtra("ProductName");
            strProductId = intent.getStringExtra("ProductId");
            strProductPrice = intent.getStringExtra("ProductPrice");
            strImage = intent.getStringExtra("Image");
            strQuoteId = intent.getStringExtra("QuoteId");
            strAddressId = intent.getStringExtra("AddressId");
            strAddress = intent.getStringExtra("Address");
            strAddresName = intent.getStringExtra("AddressName");
            strQty = intent.getStringExtra("Qty");

            strType = intent.getStringExtra("Type");


        }


    init();
}

    private void init() {




        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView) findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);
    }


    private void setValue() {

        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.payments);


        iv_menu.setOnClickListener(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        setValue();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(PaymentActivity.this, PaymentMethodsActivity.class);
        intent.putExtra("Address", strAddress);
        intent.putExtra("AddressId", strAddressId);
        intent.putExtra("AddressName", strAddresName);
        intent.putExtra("ProductName", strProductName);
        intent.putExtra("ProductId", strProductId);
        intent.putExtra("ProductPrice", strProductPrice);
        intent.putExtra("Image", strImage);
        intent.putExtra("QuoteId", strQuoteId);
        intent.putExtra("Qty", strQty);

        intent.putExtra("Type", "COD");

        startActivity(intent);


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
    // onclick

    @Override
    public void onClick(View view) {
        Intent intent =null;

        switch (view.getId()) {

            case R.id.Header_Menu:

                onBackPressed();

                break;



        }

    }
}
