package com.frami.utils.slidingpanel;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.sothree.slidinguppanel.ScrollableViewHelper;

public class NestedScrollableViewHelper extends ScrollableViewHelper {
    public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
        if (scrollableView instanceof RecyclerView) {
            if (isSlidingUp) {
                return scrollableView.getScrollY();
            } else {
                RecyclerView nsv = ((RecyclerView) scrollableView);
                View child = nsv.getChildAt(0);
                return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
            }
        } else {
            return 0;
        }
    }
}