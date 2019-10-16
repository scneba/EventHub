package com.alpha.team.eventhub.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by clasence on 29,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.

 *Gives equal margin around grid item
 */
public class GridDecorator extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridDecorator(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childPosition = parent.getChildAdapterPosition(view);
        int childColumn = childPosition % spanCount;

        if (includeEdge) {
            outRect.left = spacing - childColumn * spacing / spanCount;
            outRect.right = (childColumn + 1) * spacing / spanCount;

            if (childColumn < spanCount) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
        } else {
            outRect.left = childColumn * spacing / spanCount;
            outRect.right = spacing - (childColumn + 1) * spacing / spanCount;
            if (childPosition >= spanCount) {
                outRect.top = spacing;
            }
        }
    }
}