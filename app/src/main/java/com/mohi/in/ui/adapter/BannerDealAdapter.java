package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.activities.AllCategoryListActivity;
import com.mohi.in.activities.AllProductsListActivity;
import com.mohi.in.model.ProductModel;
import com.mohi.in.utils.listeners.CartCountCallBack;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.widgets.UbuntuRegularTextView;
import java.util.ArrayList;

public class BannerDealAdapter extends RecyclerView.Adapter<BannerDealAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ProductModel> mList = new ArrayList<>();
    private CartCountCallBack cartCountCallBack;
    private RefreshList refreshListSuccess;

    public BannerDealAdapter(Context context) {
        this.mContext = context;
//        this.cartCountCallBack = cartCountCallBack;
//        this.refreshListSuccess = refreshListSuccess;
    }

    public void setList(ArrayList<ProductModel> list)
    {
        this.mList = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_product_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ProductModel model = mList.get(position);
        HomeCategoryAdapter mCategoryAdapter = null;
        FeaturedProductsAdapter mProductAdapter = null;
        if (position == 0) {
            mCategoryAdapter = new HomeCategoryAdapter(mContext);
        } else {
            mProductAdapter = new FeaturedProductsAdapter(mContext, cartCountCallBack, refreshListSuccess);
        }


        LinearLayoutManager mCategoryLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        holder.tv_headerTitle.setText(Methods.capitalizeWord(model.category.trim()));

        holder.rv_listView.setLayoutManager(mCategoryLayoutManager);
        holder.rv_listView.setHasFixedSize(true);
        Log.e("sdfsdfdsf", "ffffffff: Position: " + position);
        //if (model.isProduct.equalsIgnoreCase("0")) {

            mCategoryAdapter.setList(model.productList);
            holder.rv_listView.setAdapter(mCategoryAdapter);

//          }
         /*else {

            mProductAdapter.setList(model.productList);
            holder.rv_listView.setAdapter(mProductAdapter);

        }*/


        holder.rv_listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }

                //   super.onScrollStateChanged(recyclerView, newState);

            }
        });

        holder.tv_headerSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.isProduct.equalsIgnoreCase("0")) {
                    Intent intent = new Intent(mContext, AllCategoryListActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

                } else {
                    Intent intent = new Intent(mContext, AllProductsListActivity.class);
                    intent.putExtra("Type", model.type);
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_header;
        private UbuntuRegularTextView tv_headerTitle, tv_headerSeeAll;
        private RecyclerView rv_listView;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_headerTitle = (UbuntuRegularTextView) itemView.findViewById(R.id.HomeProduct_Row_HeaderTitelName);
            tv_headerSeeAll = (UbuntuRegularTextView) itemView.findViewById(R.id.HomeProduct_Row_HeaderSeeAll);

            ll_header = (LinearLayout) itemView.findViewById(R.id.HomeProduct_Row_Header);

            rv_listView = (RecyclerView) itemView.findViewById(R.id.HomeProduct_Row_ListView);

        }
    }


}
