package com.mohi.in.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mohi.in.R;
import com.mohi.in.activities.ActivityFullScreenImage;
import com.mohi.in.activities.FullImageScreen_Activity;
import com.mohi.in.model.MediaModel;

import java.util.ArrayList;

public class ItemDetailsAdapter extends RecyclerView.Adapter<ItemDetailsAdapter.MyViewHolder> {

    private ArrayList<MediaModel> list;
    private Context context;
    int itemHeight = 0;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image_iv;

        MyViewHolder(View view) {
            super(view);
            image_iv = view.findViewById(R.id.image_iv);
        }
    }

    public ItemDetailsAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void addData(ArrayList<MediaModel> list, int itemHeight) {
        this.itemHeight = itemHeight;
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_rv_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MediaModel model = list.get(position);
        int totalHeight=context.getResources().getDisplayMetrics().heightPixels;
//                int width=((totalWidth*280)/600);

        int height = totalHeight - itemHeight;
        int width = (280*height)/374;
//        int height=(374*width)/280;
        holder.image_iv.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        Glide.with(context).load(model.getImageUrl()).into(holder.image_iv);

        holder.image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFullScreenImage.mediaList = list;
                Intent intent=new Intent(context, ActivityFullScreenImage.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}