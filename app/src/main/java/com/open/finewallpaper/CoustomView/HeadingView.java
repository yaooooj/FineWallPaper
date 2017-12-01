package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.Scroller;

/**
 * Created by yaojian on 2017/11/20.
 */


public class HeadingView extends ViewGroup {
    private final static String TAG = "HeadingView";
    private boolean DEBUG = true;
    private Context context;
    private int start;
    private ListAdapter mAdapter;
    private OnItemClickListener mOnItemClickListener;

    private static final int CLOSE_ENOUGH = 2;



    private int totalHeight;

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


    private int mCloseEnough;

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
        mScroller = new Scroller(context, sInterpolator);
        mCloseEnough = (int) (CLOSE_ENOUGH * density);
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

        int childWidthSize = getMeasuredWidth()  * 2 / 3;
        int childWidthMode = MeasureSpec.EXACTLY;
        int childHeightSize = getMeasuredHeight() * 4 / 5;
        int childHeightMode = MeasureSpec.EXACTLY ;

        for (int i = 0;i < childCount;i ++){
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE){
                continue;
            }

            int makeWidthMeasureSpec = -1;
            int makeHeightMeasureSpec = -1;
            makeWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,childWidthMode);
            makeHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,childHeightMode);
            child.measure(makeWidthMeasureSpec,makeHeightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        totalHeight = getChildCount() * w;
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

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();
                mIsUnableToDrag = false;

                mIsScrollStarted = true;

                mScroller.computeScrollOffset();
                if (mScrollState == SCROLL_STATE_SETTLING &&
                        Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) > mCloseEnough){
                    mScroller.abortAnimation();
                    mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(SCROLL_STATE_DRAGGING);
                }else {
                    //completeScroll(false);
                    mIsBeingDragged = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
        }

        return true;
    }


    public void resetTouch(){

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }


    @Override
    public void computeScroll() {

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
