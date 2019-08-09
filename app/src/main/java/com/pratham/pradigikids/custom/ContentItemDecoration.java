package com.pratham.pradigikids.custom;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pratham.pradigikids.util.PD_Constant;

public class ContentItemDecoration extends RecyclerView.ItemDecoration {
    private int offset;
    private String item;

    public ContentItemDecoration(String item, int offset) {
        this.item = item;
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        if (item.equalsIgnoreCase(PD_Constant.CONTENT)) {
            if (layoutParams.getSpanIndex() % 2 == 0) {
                //even position
                outRect.top = offset;
                outRect.left = offset * 1;
                outRect.right = offset * 1;
                outRect.bottom = offset;
            } else {
                //odd position
                outRect.top = offset;
                outRect.right = offset * 1;
                outRect.left = offset * 1;
                outRect.bottom = offset;
            }
        } else {
            if (layoutParams.getSpanIndex() % 2 == 0) {
                outRect.top = offset;
                outRect.left = offset;
                outRect.right = offset/*/ 2*/;
                outRect.bottom = offset;
            } else {
                outRect.top = offset;
                outRect.right = offset;
                outRect.left = offset /*/ 2*/;
                outRect.bottom = offset;
            }
        }
    }
}
