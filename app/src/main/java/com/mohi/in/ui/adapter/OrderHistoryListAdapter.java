package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import com.mohi.in.R;
import com.mohi.in.activities.WebViewActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.orderHistoryModel;
import com.mohi.in.utils.listeners.CartCountCallBack;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.Urls;
import com.mohi.in.widgets.TextAwesome;
import com.mohi.in.widgets.UbuntuBoldTextView;
import com.mohi.in.widgets.UbuntuRegularTextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class OrderHistoryListAdapter extends RecyclerView.Adapter<OrderHistoryListAdapter.ViewHolder> implements ServerCallBack {


    private Context mContext;
    private ArrayList<orderHistoryModel> mList = new ArrayList<>();
    CartCountCallBack cartCountCallBack;


    public OrderHistoryListAdapter(Context context, CartCountCallBack cartCountCallBack) {
        this.mContext = context;
        this.cartCountCallBack = cartCountCallBack;

    }

    public void setList(ArrayList<orderHistoryModel> list) {

        this.mList = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_history_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final orderHistoryModel orderHistoryItem = mList.get(position);

        holder.tvTitle.setText(orderHistoryItem.product_name);
        holder.tvDelievery.setText(orderHistoryItem.status_text);




        Glide.with(mContext)
                .load(orderHistoryItem.image)
                .into(holder.ivPhoto);
        Log.e("dsfdsfds", "sdsdsdsd: "+orderHistoryItem.status);

        if (orderHistoryItem.status.equalsIgnoreCase("Pending")) {

            holder.iv_devieveryStatus.setTextColor(mContext.getResources().getColor(R.color.termsandcondition_color));

            holder.ll_writeReview.setVisibility(View.GONE);
            holder.ll_cancel.setVisibility(View.VISIBLE);

        } else if (orderHistoryItem.status.equalsIgnoreCase("Complete")) {

            holder.iv_devieveryStatus.setTextColor(mContext.getResources().getColor(R.color.color_8ED560));

            holder.ll_writeReview.setVisibility(View.GONE);
            holder.ll_cancel.setVisibility(View.GONE);


        } else if (orderHistoryItem.status.equalsIgnoreCase("Canceled")) {

            holder.iv_devieveryStatus.setTextColor(mContext.getResources().getColor(R.color.price_orange_color));

            holder.ll_writeReview.setVisibility(View.GONE);
            holder.ll_cancel.setVisibility(View.GONE);

        } else if (orderHistoryItem.status.equalsIgnoreCase("Processing")) {
            holder.iv_devieveryStatus.setTextColor(mContext.getResources().getColor(R.color.termsandcondition_color));
            holder.ll_writeReview.setVisibility(View.GONE);
            holder.ll_cancel.setVisibility(View.GONE);
        }



        holder.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                        WaitDialog.showDialog(mContext);
                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                        json.addProperty("order_id", orderHistoryItem.order_id);


                       /* {"user_id":"161","token":"2kxrGkqoo3bfla0","order_id":"36"}*/



                            ServerCalling.ServerCallingProductsApiPost(mContext, "cancelOrder", json, OrderHistoryListAdapter.this);





                } catch (Exception e) {


                    Log.e("AllProductListAdapter", "Exception AllProductListAdapter: " + e.getMessage());
                }


            }
        });



        holder.tv_needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("URL", Urls.HELPURL);
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {

        try {

            WaitDialog.hideDialog();
            if (strfFrom.trim().equalsIgnoreCase("cancelOrder")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));


                    cartCountCallBack.CartCountCallBackSuccess();

                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            }


        } catch (Exception e) {


            Log.e("AllProductListAdapter", "Exception AllProductListAdapter ServerCallBackSuccess: " + e.getMessage());
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPhoto;
        TextAwesome iv_devieveryStatus;
        public UbuntuRegularTextView tvTitle;
        public UbuntuRegularTextView tvDelievery;
        public UbuntuRegularTextView tvWriteView;
        public RatingBar rbRating;
        UbuntuBoldTextView tv_cancel, tv_needHelp;
        private LinearLayout ll_writeReview, ll_cancel;


        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.OrderHistory_Row_Image);
            iv_devieveryStatus = (TextAwesome) itemView.findViewById(R.id.OrderHistory_Row_DelieveryStatus);

            tvTitle = (UbuntuRegularTextView) itemView.findViewById(R.id.OrderHistory_Row_Title);
            tvDelievery = (UbuntuRegularTextView) itemView.findViewById(R.id.OrderHistory_Row_Delievery);
            tvWriteView = (UbuntuRegularTextView) itemView.findViewById(R.id.OrderHistory_Row_WriteReview);
            rbRating = (RatingBar) itemView.findViewById(R.id.OrderHistory_Row_Rating);

            tv_cancel = (UbuntuBoldTextView) itemView.findViewById(R.id.OrderHistory_Row_Cancel);
            tv_needHelp = (UbuntuBoldTextView) itemView.findViewById(R.id.OrderHistory_Row_NeedHelp);

            ll_writeReview = (LinearLayout) itemView.findViewById(R.id.OrderHistory_Row_WriteReviewLayout);
            ll_cancel = (LinearLayout) itemView.findViewById(R.id.OrderHistory_Row_CancelLayout);


        }
    }


}
