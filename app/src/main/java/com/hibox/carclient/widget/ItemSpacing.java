package com.hibox.carclient.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Everyday is another day, keep going.
 * author:  Ramo
 * email:   327300401@qq.com
 * date:    2017/6/13 15:09
 * desc:
 */

public class ItemSpacing extends RecyclerView.ItemDecoration {
    private int left;
    private int right;
    private int top;
    private int bottom;
    private boolean includeEdge;

    public ItemSpacing(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    /**
     * @param includeEdge 主要是用于Bottom为0的时候，是否要显示最后一个item的底部间距
     */
    public ItemSpacing(int left, int right, int top, int bottom, boolean includeEdge) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (left != 0)
            outRect.left = left;
        if (right != 0)
            outRect.right = right;
        if (top != 0)
            outRect.top = top;
        if (position == parent.getAdapter().getItemCount() - 1) {
            if (bottom != 0) {
                outRect.bottom = bottom;
            } else {
                outRect.bottom = includeEdge ? top : bottom;
            }
        } else {
            if (bottom != 0) {
                outRect.bottom = bottom;
            }
        }

    }

}
