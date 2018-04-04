package com.mohi.in.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mohi.in.R;

public class ActivityHelp extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ImageView back_iv=findViewById(R.id.back_iv);
        ImageView back_iv_lower=findViewById(R.id.back_iv_lower);
        back_iv.setOnClickListener(this);
        back_iv_lower.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_iv:
                onBackPressed();
                break;
            case R.id.back_iv_lower:
                Intent intent = new Intent(ActivityHelp.this, ActivityChat.class);
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                break;
        }
    }
}
