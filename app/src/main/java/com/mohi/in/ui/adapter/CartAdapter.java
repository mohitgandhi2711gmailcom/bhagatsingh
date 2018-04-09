package com.mohi.in.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.fragments.CartFragment;
import com.mohi.in.model.CartModel;
import com.mohi.in.utils.listeners.CartCountCallBack;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.OnValueChangeListner;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.ArialUnicodeMSTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> implements ServerCallBack {
    private Context mContext;
    private ArrayList<CartModel> mList =new ArrayList<>();
    private int pos = 0;
    ArrayAdapter<String> adapter;
    ArrayList<String> item = new ArrayList<>();
    CartFragment fragment;
    private OnValueChangeListner mCallback;
    CartCountCallBack cartCountCallBack;
    RefreshList mRefreshList;

    public CartAdapter(Context context, CartFragment fragment, OnValueChangeListner mCallback, CartCountCallBack cartCountCallBack, RefreshList mRefreshList) {
        this.mContext = context;
        this.fragment = fragment;
        this.mRefreshList = mRefreshList;
        this.cartCountCallBack = cartCountCallBack;
        item.add("Qty: 1");
        item.add("Qty: 2");
        item.add("Qty: 3");
        item.add("Qty: 4");
        item.add("Qty: 5");
        adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        this.mCallback = mCallback;
    }

    public void setList(ArrayList<CartModel> list) {
        this.mList = new ArrayList<>();
        this.mList = list;
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
        final CartModel model = mList.get(position);
        holder.mCategoryNameTv.setText(model.product_name);
        holder.tv_type.setText(model.category);
        holder.tv_newprice.setText(Methods.getTwoDecimalVAlue(model.product_price));
        holder.tv_oldprice.setText(Methods.getTwoDecimalVAlue(model.product_price));
        holder.tv_qty.setText("Qty: " + model.qty);
        holder.spn_qty.setAdapter(adapter);
        holder.spn_qty.setSelection((Integer.parseInt(model.qty) - 1));
        holder.tv_oldpriceCurrencyType.setText(" "+SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));
        holder.tv_newpriceCurrencyType.setText(" "+SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));
        Glide.with(mContext).load(model.image).into(holder.mCategoryIv);

        if(model.is_wishlist.equalsIgnoreCase("0")){

            holder.iv_wishList.setImageResource(R.drawable.like_inactive_large);

        }else{

            holder.iv_wishList.setImageResource(R.drawable.like_active_large);
        }


        holder.ll_qyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showQtyDialog(holder.tv_qty);

            }
        });


        holder.ll_moveToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                        WaitDialog.showDialog(mContext);
                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                        json.addProperty("product_id", model.product_id);
                        pos = position;
                        if(model.is_wishlist.equalsIgnoreCase("0")){

                            ServerCalling.ServerCallingProductsApiPost(mContext, "addToWishlist", json, CartAdapter.this);

                        }else{
                            ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemWishlist", json, CartAdapter.this);
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

                       WaitDialog.showDialog(mContext);
                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                        json.addProperty("product_id", model.product_id);
                        json.addProperty("quote_id", model.quote_id);
                        pos = position;
                        ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemFromCart", json, CartAdapter.this);
                } catch (Exception e) {


                    Log.e("AllProductListAdapter", "Exception AllProductListAdapter: " + e.getMessage());
                }

            }
        });

        holder.spn_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e("dfsdf", "dfdfdf: " + model.qty + " :::: " + Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim()));


                if (Integer.parseInt(model.qty) != Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim())) {

                    CartModel model = mList.get(position);
                    model.qty = ""+Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim());
                    mCallback.onValueChange(mList.size());
                    WaitDialog.showDialog(mContext);
                    JsonObject json = new JsonObject();
                    json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                    json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                    json.addProperty("product_id", model.product_id);
                    json.addProperty("qty", model.qty);
                    json.addProperty("quote_id", model.quote_id);

                    pos = position;

                    ServerCalling.ServerCallingProductsApiPost(mContext, "updateCartQty", json, CartAdapter.this);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCategoryIv, iv_wishList;
        private UbuntuMediumTextView mCategoryNameTv, tv_type, tv_oldprice, tv_newprice, tv_oldpriceCurrencyType, tv_newpriceCurrencyType;
        private ArialUnicodeMSTextView tv_deliveryTime, tv_qty;
        private LinearLayout ll_qyt, ll_moveToWishList, ll_remove;
        private Spinner spn_qty;

        public ViewHolder(View itemView) {
            super(itemView);
            mCategoryIv = (ImageView) itemView.findViewById(R.id.AddToCart_Row_Image);
            iv_wishList = (ImageView) itemView.findViewById(R.id.AddToCart_Row_MoveToWishlistIcon);

            mCategoryNameTv = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_Name);
            tv_type = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_Type);
            tv_oldprice = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_OldPrice);
            tv_newprice = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_NewPrice);


            tv_oldpriceCurrencyType = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_OldPriceCurrencyType);
            tv_newpriceCurrencyType = (UbuntuMediumTextView) itemView.findViewById(R.id.AddToCart_Row_NewPriceCurrencyType);

            tv_deliveryTime = (ArialUnicodeMSTextView) itemView.findViewById(R.id.AddToCart_Row_DeliveryTime);
            tv_qty = (ArialUnicodeMSTextView) itemView.findViewById(R.id.AddToCart_Row_qty_text);

            ll_moveToWishList = (LinearLayout) itemView.findViewById(R.id.AddToCart_Row_MoveToWishlist);
            ll_remove = (LinearLayout) itemView.findViewById(R.id.AddToCart_Row_Remove);

            ll_qyt = (LinearLayout) itemView.findViewById(R.id.AddToCart_Row_qty);

            spn_qty = (Spinner) itemView.findViewById(R.id.AddToCart_Row_QtySpinner);

        }
    }

    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {

            WaitDialog.hideDialog();
            if (strfFrom.trim().equalsIgnoreCase("removeItemFromCart")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    mList.remove(pos);
                    mCallback.onValueChange(mList.size());
                    notifyDataSetChanged();
                    cartCountCallBack.CartCountCallBackSuccess();

                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            }else if(strfFrom.trim().equalsIgnoreCase("removeItemWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    CartModel model = mList.get(pos);
                    model.is_wishlist = "0";
                    notifyDataSetChanged();

                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("FeaturedProductsAdapter", "error FeaturedProductsAdapter log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    CartModel model = mList.get(pos);
                    model.is_wishlist = "1";
                    notifyDataSetChanged();

                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("updateCartQty")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {



                    cartCountCallBack.CartCountCallBackSuccess();
                    Methods.showToast(mContext, result.getString("msg"));


                } else {

                    Methods.showToast(mContext, result.getString("msg"));
                   // cartCountCallBack.CartCountCallBackSuccess();
                    mRefreshList.refreshListSuccess();
                    notifyDataSetChanged();
                    Log.e("AllProductListAdapter", "error AllProductListAdapter log 123: " + result.getString("msg"));
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
