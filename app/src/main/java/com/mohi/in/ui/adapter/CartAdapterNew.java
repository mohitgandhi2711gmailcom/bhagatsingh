package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohi.in.R;
import com.mohi.in.model.CartModelNew;
import com.mohi.in.widgets.UbuntuMediumTextView;

import java.util.ArrayList;

public class CartAdapterNew extends RecyclerView.Adapter<CartAdapterNew.ViewHolder> {
    private Context mContext;
    private ArrayList<CartModelNew> mList =new ArrayList<>();


    public CartAdapterNew(Context context, ArrayList<CartModelNew> list) {
        this.mContext = context;
        this.mList=list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.addtocart_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CartModelNew model = mList.get(position);
        holder.item.setText(model.getTitle());
        holder.price.setText(model.getAmount());
        holder.color.setText(model.getColor());
        holder.size.setText(model.getSize());
        holder.counter.setText(model.getCounter()+"");

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView counter,color,size;
        private UbuntuMediumTextView item,price;
        public ViewHolder(View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.AddToCart_Row_Type);
            price = itemView.findViewById(R.id.AddToCart_Row_NewPrice);
            counter=itemView.findViewById(R.id.counter_tv);
            color=itemView.findViewById(R.id.tv_color);
            size=itemView.findViewById(R.id.tv_size);
        }
    }
}
