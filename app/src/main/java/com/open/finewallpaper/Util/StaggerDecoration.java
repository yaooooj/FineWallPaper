package com.open.finewallpaper.Util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by SEELE on 2017/11/5.
 */

public class StaggerDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childPosition = parent.getChildAdapterPosition(view);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager){
            int spantCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            if (childPosition % spantCount == 0){

            }
        }
    }
}
