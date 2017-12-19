package com.open.finewallpaper.CustomView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;


import com.open.finewallpaper.R;

import java.lang.ref.WeakReference;

/**
 * Created by SEELE on 2017-12-13.
 */

public class ScrollBehavior extends CoordinatorLayout.Behavior {
    private static final String TAG = "ScrollBehavior";
    private static final int INVALID_POINTER = -1;

    private WeakReference<View> childView ;
    private WeakReference<View> dependencyView;

    private ValueAnimator mOffsetAnimator;
    final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final int MAX_OFFSET_ANIMATION_DURATION = 600; // ms

    private boolean mIsBeingDragged;
    private int mTouchSlop = -1;
    private VelocityTracker mVelocityTracker;

    private int mActivePointerId = INVALID_POINTER;
    private int mLastMotionY;
    private int offset = 0;

    private int headerSize = -1;
    private int titleSize = -1;
    private int mLayoutTop;
    private int mChildLayoutTop;

    private Context context;

    private boolean isSkipPreNestScroll = false;
    private boolean isNestedFlung = false;
    private boolean mIsNeedFlung = false;

    private ScrollerCompat mScroller;
    private FlingRunnable flingRunnable;


    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }



    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {

        if (mTouchSlop < 0){
            mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

        final int action= ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                if ( parent.isPointInChildBounds(dependencyView.get(), x, y)) {
                    mLastMotionY = y;
                    mActivePointerId = ev.getPointerId(0);
                    ensureVelocityTracker();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

                final int yy = (int) ev.getY(pointerIndex);
                final int yDiff = Math.abs(yy - mLastMotionY);
                if (yDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionY = yy;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(ev);
        }

        return mIsBeingDragged;
    }


    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }

        switch (MotionEventCompat.getActionMasked(ev)){
            case MotionEvent.ACTION_DOWN:
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();

                if (parent.isPointInChildBounds(dependencyView.get(), x, y)) {
                    mLastMotionY = y;
                    mActivePointerId = ev.getPointerId(0);
                    ensureVelocityTracker();
                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    return false;
                }

                final int yy = (int) ev.getY(activePointerIndex);
                int dy = mLastMotionY - yy;

                if (!mIsBeingDragged && Math.abs(dy) > mTouchSlop ) {
                    mIsBeingDragged = true;
                    if (dy > 0) {
                        dy -= mTouchSlop;
                    } else {
                        dy += mTouchSlop;
                    }
                }

                if (mIsBeingDragged) {
                    mLastMotionY = yy;
                    Log.e(TAG, "onTouchEvent: " + yy );
                    // We're being dragged so scroll the ABL
                    scroll(dependencyView.get(), getTopBottomOffset() - dy, -headerSize , 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float yvel = VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                            mActivePointerId);
                    fling(parent, dependencyView.get(), -headerSize, 0, yvel);
                }
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(ev);
        }

        return true;
    }

    private void ensureVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
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
        if (headerSize == -1){
            headerSize = dependencyView.get().getMeasuredHeight();
            mLayoutTop = dependencyView.get().getTop();
            mChildLayoutTop = child.getTop();
            child.layout(0,0,parent.getWidth(),parent.getHeight());
            child.setTranslationY(headerSize);

            Log.e(TAG, "onLayoutChild: " + " headerSize  "+ headerSize +
                    " mLayoutTop " + mLayoutTop + " mChildLayoutTop " + child.getTop());
            titleSize = 0;
        }
        Log.e(TAG, "onLayoutChild: " );
        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        child.setY(dependency.getHeight() + dependency.getY());
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
                consumed[1] = scroll(child,getTopBottomOffset() - dy, min,max);
            }

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
                //int p = newOffset - (child.getTop() - mLayoutTop);
                //child.offsetTopAndBottom(p);
                view.offsetTopAndBottom(newOffset - (view.getTop() - mLayoutTop));
                consumed = currOffset - newOffset;
            }
        }
        return consumed;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        if (dyUnconsumed < 0 ) {
            scroll(child,getTopBottomOffset() - dyUnconsumed ,-headerSize,0 );
            isSkipPreNestScroll = true;
        }else {
            isSkipPreNestScroll = false;
        }


    }



    private void animateOffsetTo(final CoordinatorLayout coordinatorLayout,
                                 final View child, final int offset, float velocity){

        final int deltaY = Math.abs(getTopBottomOffset() - offset);
        final int duration;

        velocity = Math.abs(velocity);

        if (velocity > 0) {
            duration = 3 * Math.round(1000 * (deltaY / velocity));
        }else {
            final float deltaRatio = (float) deltaY / child.getHeight();
            duration = (int) ((deltaRatio + 1) * 150);
        }

        animateOffsetWithDuration(coordinatorLayout,child,offset,duration);
    }

    private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout,
                                           final View child, final int offset, final int duration){

        final int currOffset = getTopBottomOffset();
        if (currOffset == offset){
            if (mOffsetAnimator != null && mOffsetAnimator.isRunning()){
                mOffsetAnimator.cancel();
            }
            return;
        }

        if (mOffsetAnimator == null){
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setOffset((Integer) animation.getAnimatedValue());
                }
            });
        }else {
            mOffsetAnimator.cancel();
        }

        mOffsetAnimator.setDuration(Math.max(duration,MAX_OFFSET_ANIMATION_DURATION));
        mOffsetAnimator.setIntValues(offset);
        mOffsetAnimator.start();

    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling: " );

        return  false;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target,
                                 float velocityX, float velocityY, boolean consumed) {
        boolean flung = false;
        if (consumed){
            flung = fling(coordinatorLayout,dependencyView.get(),-headerSize,0,-velocityY);
        }
        isNestedFlung = flung;
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
            //Log.e(TAG, "fling: " + " mScroll is not null ,can we fling" );
            flingRunnable = new FlingRunnable(coordinatorLayout,child);
            ViewCompat.postOnAnimation(child,flingRunnable);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        if (!isNestedFlung ){
            Log.e(TAG, "onStopNestedScroll: " +  " we are not on fling ,can we do  next steps"  );
        }
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
                        //Log.e(TAG, "run: "  + mScroller.getCurrY() );
                        scroll(mLayout, mScroller.getCurrY(), Integer.MIN_VALUE, Integer.MAX_VALUE);
                        // Post ourselves so that we run on the next animation
                        ViewCompat.postOnAnimation(mLayout, this);
                    }
                }
            }
        }
    }
}
