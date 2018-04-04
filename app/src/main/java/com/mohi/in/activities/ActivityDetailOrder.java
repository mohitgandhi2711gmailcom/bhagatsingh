package com.mohi.in.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.mohi.in.R;
import com.mohi.in.model.DetailOrderModel;
import com.mohi.in.model.OrderModelNew;
import com.mohi.in.ui.adapter.DetailOrderAdapter;
import java.util.ArrayList;

public class ActivityDetailOrder extends AppCompatActivity implements View.OnClickListener {
    private TextView amount_tv, status_tv, payment_tv, address_tv, tracking_code_tv;
    private RecyclerView order_detail_rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        OrderModelNew obj = (OrderModelNew) getIntent().getSerializableExtra("Model");
        init();
        if (obj != null) {
            setData(obj);
        }
    }

    private void setData(OrderModelNew obj) {
        String amount = "RP " + obj.getAmount();
        amount_tv.setText(amount);
        String status = obj.getDeliveryStatus();
        setDeliveryImage(status_tv, status);
        String paymentMode = obj.getPaymentMode();
        payment_tv.setText(paymentMode);
        String address = obj.getAddress();
        address_tv.setText(address);
        String trackingCode = obj.getTrackingCode();
        tracking_code_tv.setText(trackingCode);
        ArrayList<DetailOrderModel> list=obj.getList();
        if(list!=null && list.size()>0)
        {
            DetailOrderAdapter adapter = new DetailOrderAdapter(this,obj);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            order_detail_rv.setLayoutManager(mLayoutManager);
            order_detail_rv.setAdapter(adapter);
            order_detail_rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        }
    }

    private void setDeliveryImage(TextView delivery_status, String deliveryStatus) {
        delivery_status.setText(deliveryStatus);
        switch (deliveryStatus) {
            case "Paid":
                delivery_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.orange_circular_shape, 0, 0, 0);
                break;
            case "Waiting payment":
                delivery_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_circular_shape, 0, 0, 0);
                break;
            case "Delivered":
                delivery_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_circular_shape, 0, 0, 0);
                break;
        }
    }

    private void init() {
        amount_tv = findViewById(R.id.amount_tv);
        status_tv = findViewById(R.id.status_tv);
        payment_tv = findViewById(R.id.payment_tv);
        address_tv = findViewById(R.id.address_tv);
        tracking_code_tv = findViewById(R.id.tracking_code_tv);
        order_detail_rv=findViewById(R.id.order_detail_rv);
        Button copy_btn = findViewById(R.id.copy_btn);
        ImageView bt_back = findViewById(R.id.bt_back);
        order_detail_rv = findViewById(R.id.order_detail_rv);
        bt_back.setOnClickListener(this);
        copy_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.copy_btn:
                break;
        }
    }

    private class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.divider_line);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left + 50, top, right - 30, bottom);
                mDivider.draw(c);
            }
        }
    }
}
