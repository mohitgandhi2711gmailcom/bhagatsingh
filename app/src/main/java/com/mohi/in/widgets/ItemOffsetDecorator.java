package com.mohi.in.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecorator extends RecyclerView.ItemDecoration {
    private int offset;

    public ItemOffsetDecorator(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
//        outRect.left = offset;

        outRect.bottom = offset;
//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.top = offset;
//        }
//        if (parent.getChildAdapterPosition(view)%2 != 0){
//            outRect.right = offset;
//        }

    }
}