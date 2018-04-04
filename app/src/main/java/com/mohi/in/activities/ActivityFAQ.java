package com.mohi.in.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.mohi.in.R;
import com.mohi.in.model.FaqModel;
import com.mohi.in.ui.adapter.ExpandableListAdapterFAQ;
import java.util.ArrayList;

public class ActivityFAQ extends AppCompatActivity implements View.OnClickListener{

    ArrayList<FaqModel> faq_list;
    private ExpandableListView expandable_lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        init();
        setData();
        ExpandableListAdapterFAQ adapter = new ExpandableListAdapterFAQ(this,faq_list);
        expandable_lv.setAdapter(adapter);
    }

    private void setData() {
        for(int i=0;i<=10;i++)
        {
            FaqModel obj=new FaqModel();
            obj.setQuestion("Question"+(i+1)+" ?");
            obj.setDesc("Description of "+(i+1)+"Question");
            faq_list.add(obj);
        }
    }

    private void init() {
        faq_list=new ArrayList<>();
        expandable_lv = findViewById(R.id.expandable_lv);
        ImageView back_iv=findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_iv:
                onBackPressed();
                break;
        }
    }
}
