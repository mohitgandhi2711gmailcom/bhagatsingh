package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mohi.in.R;
import com.mohi.in.model.OrderModelNew;

import java.util.ArrayList;

public class OrderAdapterNew extends RecyclerView.Adapter<OrderAdapterNew.MyViewHolder> {

    private ArrayList<OrderModelNew> list;
    private Context context;
    private final OrderItemListener listener;


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView order_image;
        private TextView brand_tv, brand_details_tv, extra_items_tv, delivery_status, amount_tv;
        private LinearLayout parent_item_ll;

        MyViewHolder(View view) {
            super(view);
            order_image = view.findViewById(R.id.order_image);
            brand_tv = view.findViewById(R.id.brand_tv);
            brand_details_tv = view.findViewById(R.id.brand_details_tv);
            extra_items_tv = view.findViewById(R.id.extra_items_tv);
            delivery_status = view.findViewById(R.id.delivery_status);
            amount_tv = view.findViewById(R.id.amount_tv);
            parent_item_ll=view.findViewById(R.id.parent_item_ll);
        }
    }


    public OrderAdapterNew(Context context,ArrayList<OrderModelNew> list,OrderItemListener listener) {
        this.list = list;
        this.context=context;
        this.listener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OrderModelNew model = list.get(position);
        holder.order_image.setImageResource(R.drawable.google);
        holder.brand_tv.setText(model.getBrandName());
        holder.brand_details_tv.setText(model.getBrandDetails());
        holder.extra_items_tv.setText("+" + (model.getOrderNumber() - 1) + " Other Items.");
        holder.delivery_status.setText(model.getDeliveryStatus());
        holder.amount_tv.setText("RP " + String.valueOf(model.getAmount()));
        setDeliveryImage(holder.delivery_status, model.getDeliveryStatus());
        holder.parent_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClicked(model);
            }
        });
    }

    private void setDeliveryImage(TextView delivery_status, String deliveryStatus) {
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OrderItemListener
    {
        void itemClicked(OrderModelNew obj);
    }

}