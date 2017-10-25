package com.open.finewallpaper.Window;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

/**
 * Created by SEELE on 2017/10/24.
 */

public class VerticalDrawerLayout extends ViewGroup {
    private final static String TAG = "VerticalDrawerLayout";
    private ViewDragHelper mTopViewDragHelper;

    private View mContentView;
    private View mDrawerView;

    private int mCurTop = 0;

    private boolean mIsOpen = true;

    private float lastMotionY;
    private float lastMotionX;

    public VerticalDrawerLayout(Context context) {
        super(context);
        init();
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //Step1：使用静态方法构造ViewDragHelper,其中需要传入一个ViewDragHelper.Callback回调对象.
        mTopViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallBack());
        //mTopViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
        Log.e(TAG, "init: " + " init first" );

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mDrawerView = getChildAt(1);

    }

    //Step2：定义一个ViewDragHelper.Callback回调实现类
    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //返回true则表示可以捕获该view,手指摸上一瞬间调运

            return child == mDrawerView || child == mContentView;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //setEdgeTrackingEnabled设置的边界滑动时触发
            //captureChildView是为了让tryCaptureView返回false依旧生效
            mTopViewDragHelper.captureChildView(mDrawerView, pointerId);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //手指触摸移动时实时回调, left表示要到的x位置
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //手指触摸移动时实时回调, top表示要到的y位置
            //保证手指挪动时只能向上，向下最大到0
            if (child == mContentView){

                //return -2 * mContentView.getHeight();
                return  top;
            }

            if (child == mDrawerView){
                if (top > 0){
                    top = getMeasuredHeight() - mDrawerView.getMeasuredHeight();
                }
            }
            return top;
            //return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //手指释放时回调
           // Log.e(TAG, "onViewReleased: " + "xvel = " + xvel + "  yvel = "  + yvel);
            if (releasedChild == mContentView){
               // Log.e(TAG, "onViewReleased: " + releasedChild.getTop() );
                float movePercent = (releasedChild.getHeight() + releasedChild.getTop()) / (float) releasedChild.getHeight();
                if (yvel >= 500 && movePercent > 0.5f){

                    mTopViewDragHelper.smoothSlideViewTo(mDrawerView, mDrawerView.getLeft(), 0);
                }else {
                    mTopViewDragHelper.smoothSlideViewTo(mDrawerView, mDrawerView.getLeft(), -mDrawerView.getHeight());
                }
            }
            if (releasedChild == mDrawerView){
                float movePercent = (releasedChild.getHeight() + releasedChild.getTop()) / (float) releasedChild.getHeight();

                if (yvel < 0 && movePercent > 0.3f){
                    mTopViewDragHelper.smoothSlideViewTo(mDrawerView, mDrawerView.getLeft(), -mDrawerView.getHeight());
                }else {
                    mTopViewDragHelper.smoothSlideViewTo(mDrawerView, mDrawerView.getLeft(), 0);
                    //int finalBottom = (yvel < 0 && movePercent > 0.3f) ? 0 : -releasedChild.getHeight();
                    //mTopViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalBottom);
                }
            }
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            //mDrawerView完全挪出屏幕则防止过度绘制
            if (changedView == mContentView){
                //Log.e(TAG, "onViewPositionChanged: " + dy  );
                changedView.layout(mContentView.getLeft(),mContentView.getTop() - dy,mContentView.getRight(),mContentView.getBottom() - dy);
                mDrawerView.layout(mDrawerView.getLeft() ,mDrawerView.getTop() + dy,mDrawerView.getRight(),mDrawerView.getBottom() + dy);
            }
            mDrawerView.setVisibility((changedView.getHeight()+top == 0)? View.GONE : View.VISIBLE);
            mCurTop = top;
            requestLayout();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            if (mDrawerView == null) return 0;
            return (mDrawerView == child) ? mDrawerView.getHeight() : 0;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                mIsOpen = (mDrawerView.getTop() == 0);
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mTopViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    //Step3：重写onInterceptTouchEvent回调ViewDragHelper中对应的方法.
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        final int actionIndex = MotionEventCompat.getActionIndex(ev);

        //intercept = mTopViewDragHelper.shouldInterceptTouchEvent(ev);
        if (mTopViewDragHelper.shouldInterceptTouchEvent(ev)){
            return true;
        }
            float y = ev.getY();
            float x = ev.getX();
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    lastMotionY = ev.getY();
                    lastMotionX = ev.getX();
                    Log.e(TAG, "onInterceptTouchEvent: "+ "Down" );
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float dy = y - lastMotionY;
                    final float dx = Math.abs(x - lastMotionX);
                    Log.e(TAG, "onInterceptTouchEvent: " + " dy " + dy );
                    View view = findTopChildUnder((int) x,(int) y);
                    if (view == mContentView && isTop(view)){
                        Log.e(TAG, "onInterceptTouchEvent: " + " is mContentView" );
                        if (dy > 20 && dy * 0.5f > dx){
                            mTopViewDragHelper.captureChildView(mContentView,actionIndex);
                            intercept = true;
                        }
                    }else if (view == mDrawerView){
                        Log.e(TAG, "onInterceptTouchEvent: " + " is mDrawerView" );
                        intercept = true;
                    }else {
                        intercept = false;
                    }
                    break;
                default:
                    break;
            }
        return intercept;
    }

    public View findTopChildUnder(int x, int y) {
        final int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight()
                    && y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    //Step3：重写onTouchEvent回调ViewDragHelper中对应的方法.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTopViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);



        MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                measureWidth- (params.leftMargin + params.rightMargin), MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                measureHeight - (params.topMargin + params.bottomMargin), MeasureSpec.EXACTLY);
        mContentView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

        mDrawerView.measure(widthMeasureSpec, heightMeasureSpec);
        //Log.e(TAG, "onMeasure: " );

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


            //Log.e(TAG, "onLayout: " );
            MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
            mContentView.layout(params.leftMargin, params.topMargin,
                    mContentView.getMeasuredWidth() + params.leftMargin,
                    mContentView.getMeasuredHeight() + params.topMargin);
        if (changed){
            params = (MarginLayoutParams) mDrawerView.getLayoutParams();
            //mCurTop + params.topMargin
            //mCurTop + mDrawerView.getMeasuredHeight() + params.topMargin)
            mDrawerView.layout(params.leftMargin, -(mCurTop + mDrawerView.getMeasuredHeight() + params.topMargin),
                    mDrawerView.getMeasuredWidth() + params.leftMargin,
                    0);
        }






    }


    public void invilide(){
        invalidate();
    }

    private View getFirstVisibleChild() {

        for (int i =0; i < getChildCount();i++){
            View child = getChildAt(i);
            if (child.getVisibility() == GONE){
                continue;
            }else {
                return child;
            }
        }
        return null;
    }
    private boolean isTop(View view){
        boolean intercept = false;
        if (view instanceof ViewGroup) {
            if (view instanceof NestedScrollView){

                if (view.getScrollY() <= 0){
                    intercept = true;
                }
            }else if (view instanceof ScrollView){
                if (view.getScrollY() <= 0) {
                    intercept = true;
                }
            }else {
                intercept = isChildTop((ViewGroup) view);
            }

        }else {
            intercept = false;
        }
        return intercept;
    }
    private boolean isChildTop(ViewGroup viewGroup){
        int minY = 0;
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            int topMargin = 0;
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                topMargin = ((MarginLayoutParams) lp).topMargin;
            }
            int top = view.getTop() - topMargin;
            minY = Math.min(minY, top);
        }
        return minY >= 0;
    }
}
