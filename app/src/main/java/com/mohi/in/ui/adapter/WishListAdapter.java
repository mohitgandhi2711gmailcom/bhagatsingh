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
import com.koushikdutta.ion.Ion;
import com.mohi.in.R;
import com.mohi.in.activities.ProductDetailActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.WishListModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuMediumTextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> implements ServerCallBack {


    private Context mContext;
    private ArrayList<WishListModel> mList = new ArrayList<>();
    private int pos=0;
    private boolean flage=true;


    public WishListAdapter(Context context)
    {
        this.mContext =  context;
    }

    public void setList(ArrayList<WishListModel> list)
    {

        this.mList = list;
        flage=true;
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wishlist_row , parent ,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final WishListModel model = mList.get(position);
        holder.mCategoryNameTv.setText(model.product_name);


        holder.rb_rating.setRating((float) model.rating);
        holder.tv_type.setText(model.category);
        holder.tv_price.setText(Methods.getTwoDecimalVAlue(model.price));
        holder.tv_priceCyrrencyType.setText(" "+SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));




        Glide.with(mContext)
                .load(model.image)
                .into(holder.mCategoryIv);


        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                        WaitDialog.showDialog(mContext);
                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                        json.addProperty("product_id", model.product_id);

                        pos = position;



                            ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemWishlist", json, WishListAdapter.this);





                }catch (Exception e){


                    Log.e("AllProductListAdapter", "Exception AllProductListAdapter: "+e.getMessage());
                }

            }
        });


        holder.iv_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {


                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                        json.addProperty("product_id", model.product_id);
                        json.addProperty("qty", 1);
                        json.addProperty("quote_id","");


                        pos = position;


                            WaitDialog.showDialog(mContext);
                            ServerCalling.ServerCallingProductsApiPost(mContext, "addToCart", json, WishListAdapter.this);




                }catch (Exception e){


                    Log.e("AllProductListAdapter", "Exception AllProductListAdapter: "+e.getMessage());
                }

            }
        });


        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flage){
                    onCallActivity(model);
                    flage =false;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    private void onCallActivity(WishListModel model ){
        WaitDialog.showDialog(mContext);
        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra("ProductId", model.product_id);

        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mCategoryIv, iv_addToCart, iv_remove;
        private UbuntuMediumTextView mCategoryNameTv, tv_type, tv_price, tv_priceCyrrencyType;
        private RatingBar rb_rating;
        private LinearLayout ll_row;




        public ViewHolder(View itemView) {
            super(itemView);

            ll_row = (LinearLayout)itemView.findViewById(R.id.WishList_Row);

            mCategoryIv = (ImageView) itemView.findViewById(R.id.WishList_Row_Image);
            iv_remove = (ImageView) itemView.findViewById(R.id.WishList_Row_cross);
            iv_addToCart = (ImageView) itemView.findViewById(R.id.WishList_Row_CheckOut);

            mCategoryNameTv = (UbuntuMediumTextView) itemView.findViewById(R.id.WishList_Row_Name);
            tv_type = (UbuntuMediumTextView) itemView.findViewById(R.id.WishList_Row_Type);
            tv_price = (UbuntuMediumTextView) itemView.findViewById(R.id.WishList_Row_Price);
            tv_priceCyrrencyType = (UbuntuMediumTextView) itemView.findViewById(R.id.WishList_Row_PriceCurrencyType);

            rb_rating = (RatingBar) itemView.findViewById(R.id.WishList_Row_Rating);


        }
    }

    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            WaitDialog.hideDialog();

            if(strfFrom.trim().equalsIgnoreCase("removeItemWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    mList.remove(pos);
                    notifyDataSetChanged();


                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            }else if(strfFrom.trim().equalsIgnoreCase("addToCart")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    WishListModel model = mList.get(pos);

                    mList.set(pos, new WishListModel(model.product_id, model.product_name, model.image, model.qty, model.category,1, model.rating, model.price ));
                    notifyDataSetChanged();



                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            }


        }catch (Exception e){


            Log.e("AllProductListAdapter", "Exception AllProductListAdapter ServerCallBackSuccess: "+e.getMessage());
        }

    }
}
