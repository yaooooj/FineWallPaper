package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by yaojian on 2017/11/20.
 */

public class HeadingViewGroup extends ViewGroup {

    public HeadingViewGroup(Context context) {
        super(context);
    }

    public HeadingViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
