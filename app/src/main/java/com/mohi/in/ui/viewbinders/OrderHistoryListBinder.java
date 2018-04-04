package com.mohi.in.ui.viewbinders;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.mohi.in.R;
import com.mohi.in.model.orderHistoryModel;
import com.mohi.in.ui.viewholders.BaseViewHolder;
import com.mohi.in.ui.viewholders.OrderHistoryListHolder;

/**
 * Created by Great Summit on 1/15/2016.
 */
public class OrderHistoryListBinder extends ViewBinder<orderHistoryModel> {
    private Context mContext;

    public OrderHistoryListBinder(Context ctx){
        super(R.layout.order_history_row);
        mContext = ctx;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new OrderHistoryListHolder(view);
    }

    @Override
    public void bindView(final orderHistoryModel orderHistoryItem, int position, int grpPosition, View view, Activity activity) {
        final OrderHistoryListHolder holder;


        Log.e("dsfdsfsd", "dfdsfsdfdsfdsf 555555");
        if (view.getTag() != null)
            holder = (OrderHistoryListHolder)view.getTag();
        else
            holder = new OrderHistoryListHolder(view);


       /* holder.tvTitle.setText(orderHistoryItem.name);
        holder.tvDelievery.setText(orderHistoryItem.deliveredStatus);
        holder.rbRating.setRating(orderHistoryItem.rating);
*/


    }

}
