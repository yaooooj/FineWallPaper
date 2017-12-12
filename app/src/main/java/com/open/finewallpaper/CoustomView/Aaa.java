package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by SEELE on 2017-12-11.
 */

public class Aaa extends ViewPager {

    private static final String  TAG = "Aaa";
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mActivePointerId = INVALID_POINTER;
    /**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;

    private int mTouchSlop;
    public Aaa(Context context) {
        super(context);
        init(context);
    }

    public Aaa(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept  = false;
        final int action = ev.getAction();
        Log.e(TAG, "onInterceptTouchEvent: " + getCurrentItem() );
        Log.e(TAG, "onInterceptTouchEvent: getScrollX = " + getScrollX() );
        if (getCurrentItem() == 0 && getScrollX() == 0 ){

            switch (action){
                case MotionEvent.ACTION_MOVE:
                    Log.e(TAG, "onInterceptTouchEvent: "+ "Move" );
                    final int activePointerId = mActivePointerId;

                    if (activePointerId == INVALID_POINTER) {
                        Log.e(TAG, "onInterceptTouchEvent: " + "break" );
                        // If we don't have a valid id, the touch down wasn't on content.
                        break;
                    }

                    final int pointerIndex = ev.findPointerIndex(activePointerId);
                    final float x = ev.getX(pointerIndex);
                    final float dx = x - mLastMotionX;
                    final float xDiff = Math.abs(dx);
                    final float y = ev.getY(pointerIndex);
                    final float yDiff = Math.abs(y - mInitialMotionY);
                    Log.e(TAG, "onInterceptTouchEvent: " );
                    if (xDiff > mTouchSlop && xDiff * 0.5f > yDiff){
                        Log.e(TAG, "onInterceptTouchEvent: " + mTouchSlop );
                        intercept = true;
                        overScroll(dx);
                    } else if (yDiff > mTouchSlop) {
                        intercept = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    Log.e(TAG, "onInterceptTouchEvent: " + "Down" );
                    mLastMotionX = mInitialMotionX = ev.getX();
                    mLastMotionY = mInitialMotionY = ev.getY();
                    break;
            }
        }else {
            Log.e(TAG, "onInterceptTouchEvent: "  + "execute original intercept touch event" );
            intercept = super.onInterceptTouchEvent(ev);
        }

        Log.e(TAG, "onInterceptTouchEvent: " + intercept );
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean intercept  = false;
        final int action = ev.getAction();
        if (getCurrentItem() == 0){
            intercept = super.onTouchEvent(ev);
        }else {
            intercept = super.onTouchEvent(ev);
        }
        return intercept;
    }

    private void overScroll(float dx){
        Log.e(TAG, "overScroll: " +  getScrollX());
        scrollBy((int)dx,getScrollY());
    }
}
