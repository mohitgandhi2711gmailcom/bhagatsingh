package com.mohi.in.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mohi.in.R;
import com.mohi.in.fragments.CardFragment;
import com.mohi.in.fragments.CodFragment;
import com.mohi.in.fragments.WalletFragment;
import com.mohi.in.ui.adapter.PaymentAdapterNew;

public class ActivityPaymentNew extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_new);
        ViewPager viewPager = findViewById(R.id.payment_pager);
        PaymentAdapterNew adapter = new PaymentAdapterNew(getSupportFragmentManager());
        adapter.addFragment(new CodFragment(), "Cash on Delivery");
        adapter.addFragment(new CardFragment(), "CREDIT/DEBIT CARD");
        adapter.addFragment(new WalletFragment(), "Wallet");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.payment_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
