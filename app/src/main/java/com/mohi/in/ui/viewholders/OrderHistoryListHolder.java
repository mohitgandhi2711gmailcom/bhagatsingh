package com.mohi.in.ui.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.mohi.in.R;
import com.mohi.in.widgets.UbuntuRegularTextView;


/**
 * Created by Great Summit on 1/15/2016.
 */
public class OrderHistoryListHolder extends BaseViewHolder {

    public ImageView ivPhoto;
    public UbuntuRegularTextView tvTitle;
    public UbuntuRegularTextView tvDelievery;
    public UbuntuRegularTextView tvWriteView;
    public RatingBar rbRating;





    public OrderHistoryListHolder(View rootView) {
        ivPhoto = (ImageView) rootView.findViewById(R.id.OrderHistory_Row_Image);
        tvTitle = (UbuntuRegularTextView) rootView.findViewById(R.id.OrderHistory_Row_Title);
        tvDelievery = (UbuntuRegularTextView) rootView.findViewById(R.id.OrderHistory_Row_Delievery);
        tvWriteView = (UbuntuRegularTextView) rootView.findViewById(R.id.OrderHistory_Row_WriteReview);
        rbRating = (RatingBar) rootView.findViewById(R.id.OrderHistory_Row_Rating);


    }
}
