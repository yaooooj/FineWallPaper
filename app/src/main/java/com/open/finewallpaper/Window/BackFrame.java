package com.open.finewallpaper.Window;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by SEELE on 2017/10/22.
 */

public class BackFrame extends FrameLayout {

    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    private int mScreenWidth;
    private int mScreenHeight;

    private View mDimView;
    private View mDecorView;
    private ViewDragHelper mDragHelper;
    private OnPanelSlideListener mListener;

    private boolean mIsLocked = false;
    private boolean mIsEdgeTouched = false;
    private int mEdgePosition;
    private static BackFrameConfig mConfig = new BackFrameConfig();

    public BackFrame(@NonNull Context context) {
        super(context);
    }

    public BackFrame(Context context, View decorView) {
        super(context);
        init();
    }


    public void init(){

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;

        ViewDragHelper.Callback callback;

        callback = mTopCallback;

        mDragHelper = ViewDragHelper.create(this, 1f, callback);
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setEdgeTrackingEnabled(mEdgePosition);

        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);

        mDimView = new View(getContext());
        mDimView.setBackgroundColor(Color.BLACK);
        mDimView.setAlpha(0.8f);

        // Add the dimmer view to the layout
        addView(mDimView);


        post(new Runnable() {
            @Override
            public void run() {
                mScreenHeight = getHeight();
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    /**
     * The drag helper callbacks for dragging the slidr attachment from the top of the screen
     */
    private ViewDragHelper.Callback mTopCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.getId() == mDecorView.getId() && (mIsEdgeTouched);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return clamp(top, 0, mScreenHeight);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mScreenHeight;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            int top = releasedChild.getTop();
            int settleTop = 0;
            int topThreshold = (int) (getHeight() * 0.25f);
            boolean isSideSwiping = Math.abs(xvel) > 5f;

            if(yvel > 0){
                if(Math.abs(yvel) > 5f && !isSideSwiping){
                    settleTop = mScreenHeight;
                }else if(top > topThreshold){
                    settleTop = mScreenHeight;
                }
            }else if(yvel == 0){
                if(top > topThreshold){
                    settleTop = mScreenHeight;
                }
            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), settleTop);
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1f - ((float)Math.abs(top) / (float)mScreenHeight);

            if(mListener != null) mListener.onSlideChange(percent);

            // Update the dimmer alpha
            applyScrim(percent);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if(mListener != null) mListener.onStateChanged(state);
            switch (state){
                case ViewDragHelper.STATE_IDLE:
                    if(mDecorView.getTop() == 0){
                        // State Open
                        if(mListener != null) mListener.onOpened();
                    }else{
                        // State Closed
                        if(mListener != null) mListener.onClosed();
                    }
                    break;
                case ViewDragHelper.STATE_DRAGGING:

                    break;
                case ViewDragHelper.STATE_SETTLING:

                    break;
            }
        }
    };


    /**
     * Apply the scrim to the dim view
     *
     * @param percent dimming percentage
     */
    public void applyScrim(float percent){
        float alpha = (percent * (0.8f - 0f)) + 0f;
        mDimView.setAlpha(alpha);
    }


    public static int clamp(int value, int min, int max){
        return Math.max(min, Math.min(max, value));
    }


    public interface OnPanelSlideListener{
        void onStateChanged(int state);
        void onClosed();
        void onOpened();
        void onSlideChange(float percent);
    }
}
