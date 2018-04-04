package com.mohi.in.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mohi.in.R;
import com.mohi.in.activities.ActivityDetailOrder;
import com.mohi.in.model.DetailOrderModel;
import com.mohi.in.model.OrderModelNew;
import com.mohi.in.ui.adapter.OrderAdapterNew;
import java.io.Serializable;
import java.util.ArrayList;

public class OrderFragmentNew extends Fragment implements Serializable {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        ArrayList<OrderModelNew> list = prepareData();
        OrderAdapterNew adapter = new OrderAdapterNew(getActivity(), list, new OrderAdapterNew.OrderItemListener() {
            @Override
            public void itemClicked(OrderModelNew obj) {
                Intent intent = new Intent(getActivity(), ActivityDetailOrder.class);
                intent.putExtra("Model", obj);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.order_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
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

    private ArrayList<OrderModelNew> prepareData() {
        ArrayList<OrderModelNew> list = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            OrderModelNew obj = new OrderModelNew();
            obj.setImagedata("");
            obj.setOrderNumber(i + 1);
            obj.setBrandDetails("This is Detail " + i);
            obj.setAmount(i * 100);
            obj.setBrandName("Brand" + i);
            setDeliveryStatus(i, obj);
            obj.setAddress("This is Address Demo");
            obj.setTrackingCode("76376778634273");
            if (i == 0 || i == 1) {
                obj.setList(getListData());
            }
            list.add(obj);
        }
        return list;
    }

    private ArrayList<DetailOrderModel> getListData() {
        ArrayList<DetailOrderModel> list=new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            DetailOrderModel obj = new DetailOrderModel();
            if (i % 2 == 0) {
                obj.setColor("Red");
                obj.setSize("M");
            } else {
                obj.setColor("Green");
                obj.setSize("L");
            }
            list.add(obj);
        }
        return list;
    }

    private void setDeliveryStatus(int i, OrderModelNew obj) {
        switch (i % 3) {
            case 0:
                obj.setDeliveryStatus("Paid");
                obj.setPaymentMode("Online");
                break;
            case 1:
                obj.setDeliveryStatus("Waiting payment");
                obj.setPaymentMode("NetBanking");
                break;
            case 2:
                obj.setDeliveryStatus("Delivered");
                obj.setPaymentMode("Cash On Delivery");
                break;
        }
    }
}
