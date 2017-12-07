package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.util.ArrayList;

/**
 * Created by yaojian on 2017/11/20.
 */


public class HeadingView extends ViewGroup {
    private final static String TAG = "HeadingView";
    private boolean DEBUG = true;
    private int start;
    private ListAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener;
    private VelocityTracker mVelocityTracker;

    private static final int CLOSE_ENOUGH = 2;
    private int mActivePointerId = INVALID_POINTER;
    private static final int INVALID_POINTER = -1;

    private int pagePosition;
    static class ItemInfo {
        Object object; // 加载的View对象
        int position;  // 在所有View中的位置
        boolean scrolling;  //是否可以滑动
        float widthFactor;  //一个view在屏幕中占的比例
        float offset;       //偏移量
    }
    private final ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();

    private int totalWidth;

    private Scroller mScroller;

    private boolean mIsScrollStarted;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;

    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;


    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;

    private int mScrollState = SCROLL_STATE_IDLE;

    private int mTouchSlop;
    private int mCloseEnough;
    private int mMaximumVelocity;
    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public HeadingView(Context context) {
        super(context);
        init();
    }

    public HeadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init(){
        final Context context = getContext();
        final float density = context.getResources().getDisplayMetrics().density;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mScroller = new Scroller(context, sInterpolator);
        mCloseEnough = (int) (CLOSE_ENOUGH * density);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        if (mAdapter != null){
            buildView();
        }else {
            throw new IllegalArgumentException("adapter is null");
        }
        super.onAttachedToWindow();
    }

    private void buildView(){
        for (int i = 0;i < mAdapter.getCount();i++){
            final View itemView = mAdapter.getView(i,null,this);
            final int position = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onClick(itemView,position);
                    }
                }
            });
            addView(itemView);
            Log.e(TAG, "buildView: " + getChildCount() );
        }
    }

    public void setAdapter(ListAdapter adapter){
        mAdapter = adapter;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);
        //measure self
        setMeasuredDimension(w,h);
        //measure child
        measureChildView();
    }


    private int measureWidth(int widthMeasureSpec){
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (mode){
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = getSuggestedMinimumWidth();
                result = result == 0 ? widthSize : result;
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, widthSize) : result;
        return result + getPaddingLeft() + getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec){
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode){
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = getSuggestedMinimumHeight();
                result = result == 0 ? heightSize : result;
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, heightSize) : result;
        return result + getPaddingTop() + getPaddingBottom();
    }

    private void measureChildView(){

        int childCount = getChildCount();

        int childWidthSize = getMeasuredWidth();
        int childWidthMode = MeasureSpec.EXACTLY;
        int childHeightSize = getMeasuredHeight();
        int childHeightMode = MeasureSpec.EXACTLY ;

        for (int i = 0;i < childCount;i ++){
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE){
                continue;
            }

            int makeWidthMeasureSpec;
            int makeHeightMeasureSpec;
            makeWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,childWidthMode);
            makeHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,childHeightMode);
            child.measure(makeWidthMeasureSpec,makeHeightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        totalWidth = getChildCount() * w;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int offset = getMeasuredHeight() / 12;
        int left;
        int right;
        int bottom =  getMeasuredHeight() - offset;

        //计算每一个child占用的宽度
        start = getMeasuredWidth() / 7;
        int width = getMeasuredWidth() * 2 / 3;

        for(int i = 0; i < count;i++){

            final View child = getChildAt(i);

            if (child.getVisibility() == GONE){
                continue;
            }
            left = start;
            right = width + start;
            child.layout(left,offset,right,bottom);
            start += width + offset;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the drag.
            resetTouch();
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN) {
            if (mIsBeingDragged) {
                if (DEBUG) Log.v(TAG, "Intercept returning true!");
                return true;
            }
            if (mIsUnableToDrag) {
                if (DEBUG) Log.v(TAG, "Intercept returning false!");
                return false;
            }
        }
        Log.e(TAG, "onInterceptTouchEvent: " );
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();
                mIsUnableToDrag = false;
                mIsScrollStarted = true;
                mActivePointerId = ev.getPointerId(0);
                //mScroller.computeScrollOffset();
                if (mScrollState == SCROLL_STATE_SETTLING &&
                        Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) > mCloseEnough){
                    mScroller.abortAnimation();
                    mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(SCROLL_STATE_DRAGGING);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerId = mActivePointerId;
                Log.e(TAG, "onInterceptTouchEvent: " + "Move" + activePointerId);
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    Log.e(TAG, "onInterceptTouchEvent: " + "Move Break" );
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                final float x = ev.getX(pointerIndex);
                final float dx = x - mLastMotionX;
                final float xDiff = Math.abs(dx);
                final float y = ev.getY(pointerIndex);
                final float yDiff = Math.abs(y - mInitialMotionY);
                if (xDiff > mTouchSlop && xDiff * 0.5f > yDiff) {
                    Log.e(TAG, "onInterceptTouchEvent: " + "Starting drag!" );
                    mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(SCROLL_STATE_DRAGGING);
                    mLastMotionX = dx > 0
                            ? mInitialMotionX + mTouchSlop : mInitialMotionX - mTouchSlop;
                    mLastMotionY = y;
                } else if (yDiff > mTouchSlop) {
                    // The finger has moved enough in the vertical
                    // direction to be counted as a drag...  abort
                    // any attempt to drag horizontally, to work correctly
                    // with children that have scrolling containers.
                    if (DEBUG) Log.v(TAG, "Starting unable to drag!");
                    mIsUnableToDrag = true;
                }
                /*
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    if (performDrag(x)) {
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                }
                */
                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
        }

        return mIsBeingDragged;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mAdapter == null || mAdapter.getCount() == 0) {
            // Nothing to present or scroll; nothing to touch.
            return false;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final int action = event.getAction();
        boolean needsInvalidate = false;

        switch (action & MotionEventCompat.ACTION_MASK){
            case MotionEvent.ACTION_DOWN: {
                Log.e(TAG, "onTouchEvent: " + "Down" );
                mScroller.abortAnimation();

                // Remember where the motion event started
                mLastMotionX = mInitialMotionX = event.getX();
                mLastMotionY = mInitialMotionY = event.getY();
                mActivePointerId = event.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: " + " Move" );
                if (!mIsBeingDragged) {
                    final int pointerIndex = event.findPointerIndex(mActivePointerId);
                    if (pointerIndex == -1) {
                        // A child has consumed some touch events and put us into an inconsistent
                        // state.
                        needsInvalidate = resetTouch();
                        break;
                    }
                    final float x = event.getX(pointerIndex);
                    final float xDiff = Math.abs(x - mLastMotionX);
                    final float y = event.getY(pointerIndex);
                    final float yDiff = Math.abs(y - mLastMotionY);
                    if (DEBUG) {
                        Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);
                    }
                    if (xDiff > mTouchSlop && xDiff > yDiff) {
                        if (DEBUG) Log.v(TAG, "Starting drag!");
                        mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        mLastMotionX = x - mInitialMotionX > 0 ? mInitialMotionX + mTouchSlop :
                                mInitialMotionX - mTouchSlop;
                        mLastMotionY = y;
                        setScrollState(SCROLL_STATE_DRAGGING);

                        // Disallow Parent Intercept, just in case
                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                // Not else! Note that mIsBeingDragged can be set above.

                if (mIsBeingDragged) {
                    // Scroll to follow the motion event

                    final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                    final float x = event.getX(activePointerIndex);
                    performDrag(x);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {

                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
                            velocityTracker, mActivePointerId);
                    final int width = getMeasuredWidth();
                    final int scrollX = getScrollX();

                    final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                    final float x = event.getX(activePointerIndex);
                    final int totalDelta = (int) (x - mInitialMotionX);
                    if (totalDelta > width / 2){
                        Log.e(TAG, "onTouchEvent: " + "UP" );
                        targetNextPage(x);

                    }
                    needsInvalidate = resetTouch();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    //scrollToItem(mCurItem, true, 0, false);
                    needsInvalidate = resetTouch();
                }
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(event);
                final float x = event.getX(index);
                mLastMotionX = x;
                mActivePointerId = event.getPointerId(index);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP:
                //onSecondaryPointerUp(event);
                mLastMotionX = event.getX(event.findPointerIndex(mActivePointerId));
                break;
        }
        return true;
    }

    private boolean performDrag(float x){
        final float deltaX = mLastMotionX - x;
        mLastMotionX = x;

        float oldScrollX = getScrollX();
        float scrollX = oldScrollX + deltaX;
        scrollTo((int) scrollX,getScrollY());
        return true;
    }

    private void targetNextPage(float x){
        final float deltaX = mLastMotionX - x;
        mLastMotionX = x;
        float oldScrollX = getScrollX();
        float scrollX = oldScrollX + deltaX;
        final int width = getMeasuredWidth();
        final int w = Math.abs(width);
        int offset = width + getScrollX();
        if (deltaX > w / 4 ){
            Log.e(TAG, "targetNextPage: " + w );
            mScroller.startScroll(0,0,w,0,500);
            pagePosition += 1;
        }
    }


    public boolean resetTouch(){

        return false;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    void setScrollState(int newState) {
        if (mScrollState == newState) {
            return;
        }

        mScrollState = newState;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(View itemView,int position);
    }


}
