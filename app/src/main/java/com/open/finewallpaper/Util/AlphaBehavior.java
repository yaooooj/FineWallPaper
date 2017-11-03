package com.open.finewallpaper.Util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.open.finewallpaper.R;

/**
 * Created by yaojian on 2017/11/2.
 */

public class AlphaBehavior extends CoordinatorLayout.Behavior<Toolbar> {
    private static final String TAG = "AlphaBehavior";
    private int offset = 0;

    private int endOffset = 0;
    private Context context;

    private float mLastMotionX;
    private float mLastMotionY;

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
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        Log.e(TAG, "onNestedPreScroll: " + "dy" + dy );

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               Toolbar child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int startOffset = 0;
        if (dyConsumed > 0){

        }
        offset += dyConsumed;
        Log.e(TAG, "onNestedScroll: " + "dyConsumed  "+ dyConsumed  );
        Log.e(TAG, "onNestedScroll: " + "offSet  " + child.getTranslationY() );

        endOffset = (int) context.getResources().getDimension(R.dimen.header) - child.getHeight();
        Log.e(TAG, "onNestedPreScroll: " +" endOffSet  " + endOffset );

        if (dyConsumed < endOffset && dyConsumed > startOffset){
            float percent = (dyConsumed - startOffset) / endOffset;
            child.getBackground().setAlpha((int) (percent * 255));
        }else if (dyConsumed > endOffset){
            child.getBackground().setAlpha(255);
        }else {
            child.getBackground().setAlpha(0);
        }


    }
}



























