package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mohi.in.R;
import com.mohi.in.activities.ProductDetailActivity;
import com.mohi.in.model.SelectListModel;
import com.mohi.in.model.SubCategoriesModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.widgets.TextAwesome;
import com.mohi.in.widgets.UbuntuRegularTextView;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class SelectListAdapter extends RecyclerView.Adapter<SelectListAdapter.ViewHolder>  {


    private Context mContext;
    private ArrayList<SelectListModel> mList = new ArrayList<>();
    private int pos=0;
    private String strStatus;


    public SelectListAdapter(Context context)
    {
        this.mContext =  context;
    }

    public void setList(ArrayList<SelectListModel> list, String strStatus)
    {

        this.mList = list;
        this.strStatus =strStatus;
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_list_row , parent ,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SelectListModel model = mList.get(position);


        holder.mName.setText(Methods.capitalizeWord(model.name));

        if(model.isChecked){

            holder.isChecked.setTextColor(mContext.getResources().getColor(R.color.color_F47A23));

        }else {
            holder.isChecked.setTextColor(mContext.getResources().getColor(R.color.please_select_color));

        }

        if(strStatus.trim().equalsIgnoreCase("Color")){

            holder.colorCode.setVisibility(View.VISIBLE);
            holder.colorCode.setTextColor(Color.parseColor(model.colorCode));




        }else {

            holder.colorCode.setVisibility(View.GONE);

        }


        holder.ll_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(model.isChecked){

                    model.isChecked = false;


                }else {

                    model.isChecked = true;

                }

                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    private void onCallActivity(SubCategoriesModel model ){

        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra("ProductId", model.product_id);

        mContext.startActivity(intent);
        ((Activity)mContext).overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private UbuntuRegularTextView mName;
        private TextAwesome isChecked, colorCode;

        private LinearLayout ll_row;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (UbuntuRegularTextView) itemView.findViewById(R.id.SelectList_Row_Name);
            isChecked = (TextAwesome) itemView.findViewById(R.id.SelectList_Row_Check);
            colorCode = (TextAwesome) itemView.findViewById(R.id.SelectList_Row_ColorCode);

            ll_row = (LinearLayout) itemView.findViewById(R.id.SelectList_Row);


        }
    }


}
