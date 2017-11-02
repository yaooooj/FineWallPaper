package com.open.finewallpaper.Util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yaojian on 2017/11/2.
 */

public class AlphaBehavior extends CoordinatorLayout.Behavior<Toolbar> {
    private static final String TAG = "AlphaBehavior";
    private int offset = 0;
    private int startOffset = 0;
    private int endOffset = 0;
    private Context context;

    public AlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, Toolbar child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, Toolbar child, View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll: " + dy );
        offset += dy;
        endOffset =  child.getMeasuredHeight();
        Log.e(TAG, "onNestedPreScroll: " +" offset" + offset );

        if (offset < endOffset && offset > 0){
            float percent = (offset - startOffset) / endOffset;
            child.getBackground().setAlpha((int) (percent * 255));
        }else if (offset > endOffset){
            child.getBackground().setAlpha(255);
        }else if (offset <= 0 ){
            child.getBackground().setAlpha(0);
        }
    }
}
