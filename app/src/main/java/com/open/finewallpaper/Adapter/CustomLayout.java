package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by yaojian on 2017/10/26.
 */

public class CustomLayout extends GridLayoutManager {


    private boolean isScrollEnable = true;

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomLayout(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnable(boolean scrollEnable) {
        isScrollEnable = scrollEnable;
    }
    @Override
    public boolean canScrollVertically() {
        return isScrollEnable && super.canScrollVertically();
    }
}
