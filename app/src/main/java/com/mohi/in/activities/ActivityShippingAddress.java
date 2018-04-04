package com.mohi.in.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mohi.in.R;
import com.mohi.in.utils.Methods;


public class ActivityShippingAddress extends AppCompatActivity implements View.OnClickListener
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address_new);
        Button update_address_btn = findViewById(R.id.update_address_btn);
        update_address_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.update_address_btn:
                Methods.showToast(this,"Address Added");
                onBackPressed();
                break;
        }
    }
}

