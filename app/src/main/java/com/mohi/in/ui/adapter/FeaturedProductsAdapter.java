package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.ProductDetailActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.FeaturedProductsModel;

import com.mohi.in.utils.listeners.CartCountCallBack;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuMediumTextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class FeaturedProductsAdapter extends RecyclerView.Adapter<FeaturedProductsAdapter.ViewHolder> implements ServerCallBack {


    private Context mContext;
    private ArrayList<FeaturedProductsModel> mList = new ArrayList<>();
    private int pos=0;
    CartCountCallBack cartCountCallBack;
    RefreshList refreshListSuccess;

    private boolean flage=true;
    boolean status = true;


    public FeaturedProductsAdapter(Context context, CartCountCallBack cartCountCallBack, RefreshList refreshListSuccess) {
        this.mContext = context;
        this.cartCountCallBack = cartCountCallBack;
        this.refreshListSuccess = refreshListSuccess;
    }

    public void setList(ArrayList<FeaturedProductsModel> list) {
        this.mList = list;
       flage=true;
        notifyDataSetChanged();
    }
    public void setList(ArrayList<FeaturedProductsModel> list, boolean status) {

        this.mList = list;
       flage=true;
        this.status = status;

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_feature_subproducts_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FeaturedProductsModel model = mList.get(position);

         holder.mFeaturedProductsRowTitleTv.setText(model.product_name);
        holder.mFeaturedProductsRowPriceTv.setText(Methods.getTwoDecimalVAlue(model.product_price));



        holder.mCurrencyType.setText(" "+SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE));




        Glide.with(mContext)
                .load(model.image)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .dontAnimate()
                .override(R.dimen.home_product_row_width, R.dimen.home_product_row_image_height)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        holder.mCategoryIv.setImageBitmap(resource);
                    }
                });
              //  .into(holder.mCategoryIv);

        if(!status){
            holder.ll_FeaturedProducts_Row_FavoriteAddTOcard.setVisibility(View.GONE);
        }


      if(model.is_wishlist.equalsIgnoreCase("0")){

          holder.mFeaturedProductsRowFavoriteIv.setImageResource(R.drawable.like_inactive_large);

      }else{

          holder.mFeaturedProductsRowFavoriteIv.setImageResource(R.drawable.like_active_large);

      }



        holder.mFeaturedProductsRowFavoriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {


                        WaitDialog.showDialog(mContext);
                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                        json.addProperty("product_id", model.product_id);

                        pos = position;

                        if(model.is_wishlist.equalsIgnoreCase("0")){

                            ServerCalling.ServerCallingProductsApiPost(mContext, "addToWishlist", json, FeaturedProductsAdapter.this);

                        }else{

                            ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemWishlist", json, FeaturedProductsAdapter.this);

                        }




                }catch (Exception e){


                    Log.e("FeaturedProductsAdapter", "Exception FeaturedProductsAdapter: "+e.getMessage());
                }

            }
        });


        holder.mFeaturedProductsRowAddToCartLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {




                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                        json.addProperty("product_id", model.product_id);
                        json.addProperty("qty", 1);
                        json.addProperty("quote_id","");


                        pos = position;

                            WaitDialog.showDialog(mContext);
                            ServerCalling.ServerCallingProductsApiPost(mContext, "addToCart", json, FeaturedProductsAdapter.this);





                }catch (Exception e){


                    Log.e("FeaturedProductsAdapter", "Exception FeaturedProductsAdapter: "+e.getMessage());
                }

            }
        });



        holder.mCategoryRowInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("dsfdsfdsf","saasdasdasd: "+model.product_name);
                Log.e("dsfdsfdsf","saasdasdasd: "+model.product_id);



                if (flage){
                    onCallActivity(model);
                    flage =false;
                }

            }
        });

        holder.mCategoryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flage){
                    onCallActivity(model);
                    flage =false;
                }

            }
        });

    }

    private void onCallActivity(FeaturedProductsModel model ){
        WaitDialog.showDialog(mContext);
        Log.e("dsfdsf","ffffffffff 123123 456456 789789");

        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra("ProductId", model.product_id);


        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCategoryIv, mFeaturedProductsRowFavoriteIv;
        private UbuntuMediumTextView mFeaturedProductsRowTitleTv, mFeaturedProductsRowPriceTv, mCurrencyType;

        private LinearLayout mFeaturedProductsRowAddToCartLl, ll_FeaturedProducts_Row_FavoriteAddTOcard;
        private CardView mCategoryRowInner;

        public ViewHolder(View itemView) {
            super(itemView);

            mCategoryIv = (ImageView) itemView.findViewById(R.id.FeaturedProducts_Row_Image);
            mFeaturedProductsRowTitleTv = (UbuntuMediumTextView) itemView.findViewById(R.id.FeaturedProducts_Row_Title);
            mFeaturedProductsRowPriceTv = (UbuntuMediumTextView) itemView.findViewById(R.id.FeaturedProducts_Row_Price);
            mCurrencyType = (UbuntuMediumTextView)itemView.findViewById(R.id.FeaturedProducts_Row_CurrencyType);

            mCategoryRowInner = (CardView) itemView.findViewById(R.id.FeaturedProducts_Row);
            mFeaturedProductsRowAddToCartLl = (LinearLayout) itemView.findViewById(R.id.FeaturedProducts_Row_AddToCart);
            ll_FeaturedProducts_Row_FavoriteAddTOcard = (LinearLayout) itemView.findViewById(R.id.FeaturedProducts_Row_FavoriteAddTOcard);
            mFeaturedProductsRowFavoriteIv = (ImageView) itemView.findViewById(R.id.FeaturedProducts_Row_Favorite);

        }
    }


    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {

            WaitDialog.hideDialog();

            if(strfFrom.trim().equalsIgnoreCase("addToWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                 /*   Methods.showToast(mContext, result.getString("msg"));
                    FeaturedProductsModel model = mList.get(pos);
                    model.is_wishlist = "1";
                   // mList.set(pos, new FeaturedProductsModel(model.product_id, model.product_name, model.image, model.product_price, "1", model.rating, model.is_add_to_cart ));
                    notifyDataSetChanged();*/

                    if (refreshListSuccess!=null)
                        refreshListSuccess.refreshListSuccess();



                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("FeaturedProductsAdapter", "error FeaturedProductsAdapter log: " + result.getString("msg"));
                }
            }else if(strfFrom.trim().equalsIgnoreCase("removeItemWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
/*

                    Methods.showToast(mContext, result.getString("msg"));
                    FeaturedProductsModel model = mList.get(pos);
                    model.is_wishlist = "0";

                   // mList.set(pos, new FeaturedProductsModel(model.product_id, model.product_name, model.image, model.product_price, "0", model.rating, model.is_add_to_cart ));
                    notifyDataSetChanged();
*/

                    if (refreshListSuccess!=null)
                        refreshListSuccess.refreshListSuccess();



                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("FeaturedProductsAdapter", "error FeaturedProductsAdapter log: " + result.getString("msg"));
                }
            }else if(strfFrom.trim().equalsIgnoreCase("addToCart")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    FeaturedProductsModel model = mList.get(pos);
                    model.is_add_to_cart = "1";
                    //mList.set(pos, new FeaturedProductsModel(model.product_id, model.product_name, model.image, model.product_price, model.is_wishlist, model.rating, 1 ));
                    notifyDataSetChanged();

                    if (cartCountCallBack!=null)
                    cartCountCallBack.CartCountCallBackSuccess();

                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("FeaturedProductsAdapter", "error FeaturedProductsAdapter log: " + result.getString("msg"));
                }
            }


        }catch (Exception e){


            Log.e("FeaturedProductsAdapter", "Exception FeaturedProductsAdapter ServerCallBackSuccess: "+e.getMessage());
        }

    }

}
