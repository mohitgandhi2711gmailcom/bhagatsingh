package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.ProductDetailActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.fragments.CartFragment;
import com.mohi.in.model.FeaturedProductsModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.OnValueChangeListner;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.ArialUnicodeMSTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> implements ServerCallBack {


    private Context mContext;
    private ArrayList<FeaturedProductsModel> mList = new ArrayList<>();
    private int pos = 0;
    CartFragment fragment;
    private OnValueChangeListner mCallback;
    private boolean flage=true;

    public DealsAdapter(Context context) {
        this.mContext = context;


    }


    public DealsAdapter(Context context, CartFragment fragment, OnValueChangeListner mCallback) {
        this.mContext = context;
        this.fragment = fragment;



        this.mCallback = mCallback;

    }

    public void setList(ArrayList<FeaturedProductsModel> list) {
        this.mList = new ArrayList<>();
        this.mList = list;
        flage=true;

        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.addtocart_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FeaturedProductsModel model = mList.get(position);
        holder.mCategoryNameTv.setText(model.product_name);
        holder.tv_type.setVisibility(View.GONE);

        holder.tv_newprice.setText(Methods.getTwoDecimalVAlue(model.new_price));
        holder.tv_oldprice.setText(Methods.getTwoDecimalVAlue(model.product_price));

        holder.tv_newpriceCyrrencyType.setText(" "+SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE));
        holder.tv_oldpriceCyrrencyType.setText(" "+SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE));



        holder.spn_qty.setVisibility(View.GONE);
        holder.fl_oldPrice.setVisibility(View.VISIBLE);
        holder.iv_addToCardIcon.setImageResource(R.drawable.add_to_cart_gray);
        holder.tv_addTocard.setText("Add to cart");


        Log.e("dddd", "fffffff: " + model.is_wishlist);

        if (model.is_wishlist.equalsIgnoreCase("0")) {

            holder.iv_faveritIcon.setImageResource(R.drawable.like_inactive_large);

        } else {
            holder.iv_faveritIcon.setImageResource(R.drawable.like_active_large);

        }




        Glide.with(mContext)
                .load(model.image)
                .into(holder.mCategoryIv);

        holder.ll_moveToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {



                        WaitDialog.showDialog(mContext);
                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                        json.addProperty("product_id", model.product_id);

                        pos = position;


                        if (model.is_wishlist.equalsIgnoreCase("0")) {

                            ServerCalling.ServerCallingProductsApiPost(mContext, "addToWishlist", json, DealsAdapter.this);

                        } else {

                            ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemWishlist", json, DealsAdapter.this);

                        }



                } catch (Exception e) {


                    Log.e("AllProductListAdapter", "Exception AllProductListAdapter: " + e.getMessage());
                }

            }
        });


        holder.ll_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {


                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                        json.addProperty("product_id", model.product_id);
                        json.addProperty("qty", 1);
                        json.addProperty("quote_id", "");


                        pos = position;


                        WaitDialog.showDialog(mContext);
                        ServerCalling.ServerCallingProductsApiPost(mContext, "addToCart", json, DealsAdapter.this);




                } catch (Exception e) {


                    Log.e("AllProductListAdapter", "Exception AllProductListAdapter: " + e.getMessage());
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

    private void onCallActivity(FeaturedProductsModel model) {
        WaitDialog.showDialog(mContext);
        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra("ProductId", model.product_id);

        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCategoryIv, iv_faveritIcon, iv_addToCardIcon;
        private UbuntuMediumTextView mCategoryNameTv, tv_type, tv_oldprice, tv_newprice, tv_oldpriceCyrrencyType, tv_newpriceCyrrencyType;
        private ArialUnicodeMSTextView tv_deliveryTime, tv_qty, tv_addTocard;
        private LinearLayout ll_qyt, ll_moveToWishList, ll_remove, ll_row;
        FrameLayout fl_oldPrice;
        private Spinner spn_qty;


        public ViewHolder(View itemView) {
            super(itemView);
            /*mCategoryIv = (ImageView) itemView.findViewById(R.id.AddToCart_Row_Image);

            mCategoryNameTv = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_Name);
            tv_type = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_Type);
            tv_oldprice = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_OldPrice);
            tv_newprice = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_NewPrice);
            tv_oldpriceCyrrencyType = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_OldPriceCurrencyType);
            tv_newpriceCyrrencyType = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_NewPriceCurrencyType);

            tv_deliveryTime = (ArialUnicodeMSTextView) itemView.findViewById(R.id.AddToCart_Row_DeliveryTime);
            tv_qty = (ArialUnicodeMSTextView) itemView.findViewById(R.id.AddToCart_Row_qty_text);

            ll_moveToWishList = (LinearLayout) itemView.findViewById(R.id.AddToCart_Row_MoveToWishlist);
            ll_remove = (LinearLayout) itemView.findViewById(R.id.AddToCart_Row_Remove);

            ll_qyt = (LinearLayout) itemView.findViewById(R.id.AddToCart_Row_qty);
            ll_row = (LinearLayout) itemView.findViewById(R.id.AddToCart_Row);

            fl_oldPrice = (FrameLayout) itemView.findViewById(R.id.AddToCart_Row_OldPriceLayout);
            spn_qty = (Spinner) itemView.findViewById(R.id.AddToCart_Row_QtySpinner);

            iv_addToCardIcon = (ImageView) itemView.findViewById(R.id.AddToCart_Row_RemoveIcon);
            iv_faveritIcon = (ImageView) itemView.findViewById(R.id.AddToCart_Row_MoveToWishlistIcon);
            tv_addTocard = (ArialUnicodeMSTextView) itemView.findViewById(R.id.AddToCart_Row_RemoveText);*/

        }
    }

    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {

            WaitDialog.hideDialog();
            if (strfFrom.trim().equalsIgnoreCase("addToCart")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    FeaturedProductsModel model = mList.get(pos);
                    model.is_add_to_cart = "1";
                    //mList.set(pos, new FeaturedProductsModel(model.product_id, model.product_name, model.image, model.product_price, model.is_wishlist, model.rating, 1 ));
                    notifyDataSetChanged();

                    //HomeActivity.HomeActivity.CartCountCallBackSuccess();


                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("FeaturedProductsAdapter", "error FeaturedProductsAdapter log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    FeaturedProductsModel model = mList.get(pos);
                    model.is_wishlist = "1";

                    // mList.set(pos, new CartModel(model.product_id, model.product_name, model.image, model.qty, model.category,1, model.rating, model.price ));
                    notifyDataSetChanged();


                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("removeItemWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    FeaturedProductsModel model = mList.get(pos);
                    model.is_wishlist = "0";

                    // mList.set(pos, new FeaturedProductsModel(model.product_id, model.product_name, model.image, model.product_price, "0", model.rating, model.is_add_to_cart ));
                    notifyDataSetChanged();


                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("FeaturedProductsAdapter", "error FeaturedProductsAdapter log: " + result.getString("msg"));
                }
            }


        } catch (Exception e) {


            Log.e("AllProductListAdapter", "Exception AllProductListAdapter ServerCallBackSuccess: " + e.getMessage());
        }

    }

    private void showQtyDialog(final ArialUnicodeMSTextView tv_qty) {

        AlertDialog.Builder b = new AlertDialog.Builder(mContext);

        String[] types = {"1", "2", "3", "4", "5"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    case 0:
                        tv_qty.setText("Qty: 1");
                        break;
                    case 1:
                        tv_qty.setText("Qty: 2");
                        break;

                    case 2:
                        tv_qty.setText("Qty: 3");
                        break;

                    case 3:
                        tv_qty.setText("Qty: 4");
                        break;

                    case 4:
                        tv_qty.setText("Qty: 5");
                        break;


                }
            }

        });


      /*  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(b.getWindow().getAttributes());
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height =  LayoutParams.WRAP_CONTENT;
        lp.gravity =Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);*/

        b.show();

    }


}
