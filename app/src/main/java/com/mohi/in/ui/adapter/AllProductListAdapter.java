package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.activities.ActivityItemDetails;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.SubCategoriesModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuMediumTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ServerCallBack {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private ArrayList<SubCategoriesModel> mList = new ArrayList<>();
    private boolean isLoadingAdded = false;
    private Context mContext;
    private int pos = 0;
    private boolean flage = true;

    public AllProductListAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(ArrayList<SubCategoriesModel> list) {
        this.mList.addAll(list);
        flage = true;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View v1 = inflater.inflate(R.layout.subcategories_row, parent, false);
                viewHolder = new ViewHolder(v1);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        final SubCategoriesModel model = mList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                ViewHolder holder = (ViewHolder) holder1;
                holder.mName.setText(model.product_name);
                if (model.product_price != null) {
                    holder.mPrice.setText(Methods.getTwoDecimalVAlue(model.product_price));
                }
                holder.mCurrencyType.setText(" " + SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE));
                Glide.with(mContext).load(model.image).into(holder.mImage);
                holder.mItemLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (flage) {
                            onCallActivity(model);
                            flage = false;
                        }
                    }
                });
                holder.mImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (flage) {
                            onCallActivity(model);
                            flage = false;
                        }
                    }
                });
                if (model.is_wishlist == 0) {
                    holder.mFavorite.setImageResource(R.drawable.ic_love_like);
                } else {
                    holder.mFavorite.setImageResource(R.drawable.like_active_large);
                }
                holder.favorite_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            WaitDialog.showDialog(mContext);
                            JsonObject json = new JsonObject();
                            json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                            json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                            json.addProperty("product_id", model.product_id);
                            pos = position;
                            if (model.is_wishlist == 0) {
                                ServerCalling.ServerCallingProductsApiPost(mContext, "addToWishlist", json, AllProductListAdapter.this);
                            } else {
                                ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemWishlist", json, AllProductListAdapter.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.llAddToCart.setOnClickListener(new View.OnClickListener() {
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
                            ServerCalling.ServerCallingProductsApiPost(mContext, "addToCart", json, AllProductListAdapter.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    private void onCallActivity(SubCategoriesModel model) {
        WaitDialog.showDialog(mContext);
        Intent intent = new Intent(mContext, ActivityItemDetails.class);
        intent.putExtra("ProductId", model.product_id);
        intent.putExtra("isWishlist",String.valueOf(model.is_wishlist));
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
    }

    public void addAll(List<SubCategoriesModel> mcList) {
        flage = true;
        for (SubCategoriesModel mc : mcList) {
            mList.add(mc);
            notifyItemInserted(mList.size() - 1);
        }
    }

    public void remove(SubCategoriesModel city) {
        int position = mList.indexOf(city);
        if (position > -1) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        mList.add(new SubCategoriesModel());
        notifyItemInserted(mList.size() - 1);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = mList.size() - 1;
        SubCategoriesModel item = getItem(position);

        if (item != null) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private SubCategoriesModel getItem(int position) {
        return mList.get(position);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private UbuntuMediumTextView mName, mCurrencyType;
        private UbuntuMediumTextView mPrice;
        private ImageView mImage;
        private ImageView mFavorite;
        private LinearLayout mItemLl, llAddToCart;
        private MaterialRippleLayout favorite_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.Subcategories_Row_Title);
            mPrice = itemView.findViewById(R.id.Subcategories_Row_Price);
            mCurrencyType = itemView.findViewById(R.id.Subcategories_Row_CurrencyType);
            mFavorite = itemView.findViewById(R.id.Subcategories_Row_Favorite);
            mImage = itemView.findViewById(R.id.Subcategories_Row_Image);
            mItemLl = itemView.findViewById(R.id.ll_item);
            llAddToCart = itemView.findViewById(R.id.Subcategories_Row_AddToCart);
            favorite_layout=itemView.findViewById(R.id.favorite_layout);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {
        LoadingVH(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            WaitDialog.hideDialog();
            if (strfFrom.trim().equalsIgnoreCase("addToWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    SubCategoriesModel model = mList.get(pos);
                    mList.set(pos, new SubCategoriesModel(model.product_id, model.product_name, model.image, model.product_price, 1, model.rating, model.is_add_to_cart));
                    notifyDataSetChanged();
                } else {
                    Methods.showToast(mContext, result.getString("Unable to Add to Wishlist"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("removeItemWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    SubCategoriesModel model = mList.get(pos);
                    mList.set(pos, new SubCategoriesModel(model.product_id, model.product_name, model.image, model.product_price, 0, model.rating, model.is_add_to_cart));
                    notifyDataSetChanged();
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToCart")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    SubCategoriesModel model = mList.get(pos);
                    mList.set(pos, new SubCategoriesModel(model.product_id, model.product_name, model.image, model.product_price, model.is_wishlist, model.rating, 1));
                    notifyDataSetChanged();
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}