package com.mohi.in.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mohi.in.R;
import com.mohi.in.model.SelectListModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.widgets.TextAwesome;
import com.mohi.in.widgets.UbuntuRegularTextView;

import java.util.ArrayList;
import java.util.List;

public class SelectListAdapter extends RecyclerView.Adapter<SelectListAdapter.MyViewHolder> {


    private Context mContext;
    private List<SelectListModel> mList = new ArrayList<>();
    private String title;

    public SelectListAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<SelectListModel> list, String title) {
        this.mList = list;
        this.title = title;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SelectListModel model = mList.get(position);
        holder.mName.setText(Methods.capitalizeWord(model.getName()));
        if (model.isChecked()) {
            holder.isChecked.setTextColor(mContext.getResources().getColor(R.color.color_F47A23));
        } else {
            holder.isChecked.setTextColor(mContext.getResources().getColor(R.color.please_select_color));
        }
        if (title.trim().equalsIgnoreCase("Color")) {
            holder.colorCode.setVisibility(View.VISIBLE);
            String color = model.getColorCode();
            if (color != null && !TextUtils.isEmpty(color)) {
                holder.colorCode.setTextColor(Color.parseColor(model.getColorCode()));
            }
        } else {
            holder.colorCode.setVisibility(View.GONE);
        }
        holder.parentRowLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.isChecked()) {
                    model.setChecked(false);
                } else {
                    model.setChecked(true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private UbuntuRegularTextView mName;
        private TextAwesome isChecked;
        private TextAwesome colorCode;
        private LinearLayout parentRowLinearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.SelectList_Row_Name);
            isChecked = itemView.findViewById(R.id.SelectList_Row_Check);
            colorCode = itemView.findViewById(R.id.SelectList_Row_ColorCode);
            parentRowLinearLayout = itemView.findViewById(R.id.SelectList_Row);
        }
    }
}
