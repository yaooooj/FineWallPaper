package com.open.finewallpaper.CustomView;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.open.finewallpaper.R;

import java.lang.ref.WeakReference;

/**
 * Created by SEELE on 2017-12-13.
 */

public class ScrollBehavior extends CoordinatorLayout.Behavior {
    private static final String TAG = "ScrollBehavior";

    WeakReference<View> childView ;
    WeakReference<View> dependencyView;
    private int offset = 0;

    private int headerSize = -1;
    private int titleSize = -1;
    private int mLayoutTop;
    private int mChildLayoutTop;

    private Context context;

    private boolean isSkipPreNestScroll = false;
    private boolean isNestedFlung = false;

    private ScrollerCompat mScroller;
    private FlingRunnable flingRunnable;
    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }



    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return super.onTouchEvent(parent, child, ev);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View
            child, View dependency) {
        boolean isDependView = false;
        if (child != null){
            childView = new WeakReference<>(child);

        }
        if (dependency != null && dependency instanceof RelativeLayout){
            dependencyView = new WeakReference<>(dependency);
           isDependView = true;
        }
        return isDependView;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        child.layout(0,0,parent.getWidth(),(parent.getHeight() - dependencyView.get().getHeight()));
        if (headerSize == -1){
            headerSize = dependencyView.get().getMeasuredHeight();
            mLayoutTop = dependencyView.get().getTop();
            mChildLayoutTop = child.getTop();
            child.setTranslationY(headerSize);
            Log.e(TAG, "onLayoutChild: " + " headerSize  "+ headerSize +
                    " mLayoutTop " + mLayoutTop + " mChildLayoutTop " + child.getTop());
            titleSize = 0;

        }
        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
       // View view  = dependencyView.get();
        //float translationY = child.getTranslationY();
       // float min = titleSize *1.0f/headerSize;
       // float pro = translationY / headerSize;
        /*
        View titleView = dependencyView.get().findViewById(R.id.viewa_tb);
        titleView.setPivotX(0);
        titleView.setPivotY(0);
        titleView.setAlpha(1 - pro);
        if (pro  <= min + 1){
            titleView.setAlpha(1);
        }
        */
       // View viewpager = dependencyView.get().findViewById(R.id.heading_vp);
      //  int offset = viewpager.getHeight() - child.getTop();
      //  ViewCompat.offsetTopAndBottom(viewpager,offset);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {


    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy != 0  && !isSkipPreNestScroll){
            int min,max;
            if (dy < 0){
                min = -headerSize;
                max = -headerSize + titleSize + min;
            }else {
                min = -headerSize;
                max = 0;
            }
            consumed[1] = scroll(child,getTopBottomOffset() - dy, min,max);
        }
    }

    private int scroll(View child,int newOffset,int min,int max){
        int consumed = 0;
        final int currOffset = getTopBottomOffset();
        View view = dependencyView.get().findViewById(R.id.ts_rvl);
        if (min != 0 && currOffset >= min && currOffset <= max){
            newOffset = newOffset < min ? min : (newOffset > max ? max : newOffset);
            if (currOffset != newOffset){
                setOffset(newOffset);
                int p = newOffset - (child.getTop() - mLayoutTop);
                child.offsetTopAndBottom(p);
                int l = newOffset - (view.getTop() - mLayoutTop);
                view.offsetTopAndBottom(l);
                consumed = currOffset - newOffset;
            }
        }
        return consumed;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0) {
            scroll(child,getTopBottomOffset() - dyUnconsumed ,-headerSize,0 );
            isSkipPreNestScroll = true;
        }else {
            isSkipPreNestScroll = false;
        }
    }


    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        boolean flung = false;
        Log.e(TAG, "onNestedFling: " +consumed );
        if (!consumed){
            flung = fling(coordinatorLayout,child,-headerSize,0,-velocityY);
        }
        return flung;
    }

    private boolean fling(CoordinatorLayout coordinatorLayout, View child, int minOffset,
                          int maxOffset, float velocityY){

        if (mScroller == null){
            mScroller = ScrollerCompat.create(child.getContext());
        }
        mScroller.fling(
                0, getTopBottomOffset(), // curr
                0, Math.round(velocityY), // velocity.
                0, 0, // x
                minOffset, maxOffset); // y

        if (mScroller.computeScrollOffset()){
            flingRunnable = new FlingRunnable(coordinatorLayout,child);
            ViewCompat.postOnAnimation(child,flingRunnable);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        isSkipPreNestScroll = false;
        isNestedFlung = false;
    }


    private int getTopBottomOffset() {
        return offset;
    }

    private void setOffset(int offset) {
        this.offset = offset;
    }

    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        @Override
        public void run() {
            if (mParent != null && mLayout != null){
                if (mScroller.computeScrollOffset()){
                    if (mScroller.computeScrollOffset()) {
                        //setHeaderTopBottomOffset(mParent, mLayout, mScroller.getCurrY());
                        scroll(mLayout,getTopBottomOffset() - mScroller.getCurrY(),-headerSize,0);
                        // Post ourselves so that we run on the next animation
                        ViewCompat.postOnAnimation(mLayout, this);
                    }
                }
            }
        }
    }
}
