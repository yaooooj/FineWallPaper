package com.open.finewallpaper.Window;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SEELE on 2017/10/25.
 */

public class Daa extends ViewGroup {
    private View mContentView;
    private View mDrawerView;

    private int mCurTop = 0;

    public Daa(Context context) {
        super(context);
        init();
    }

    public Daa(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Daa(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void  init(){

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

        mContentView = getChildAt(0);
        mDrawerView = getChildAt(1);

        MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                measureWidth- (params.leftMargin + params.rightMargin), MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                measureHeight - (params.topMargin + params.bottomMargin), MeasureSpec.EXACTLY);
        mContentView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

        mDrawerView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
            mContentView.layout(params.leftMargin, params.topMargin,
                    mContentView.getMeasuredWidth() + params.leftMargin,
                    mContentView.getMeasuredHeight() + params.topMargin);

            params = (MarginLayoutParams) mDrawerView.getLayoutParams();
            //mCurTop + params.topMargin
            //mCurTop + mDrawerView.getMeasuredHeight() + params.topMargin)
            mDrawerView.layout(params.leftMargin, -(mCurTop + mDrawerView.getMeasuredHeight() + params.topMargin),
                    mDrawerView.getMeasuredWidth() + params.leftMargin,
                    0);
        }
    }
}
