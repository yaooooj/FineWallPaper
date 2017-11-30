package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by SEELE on 2017-11-30.
 */

public class CuVp extends ViewPager {
    private int start = 0;
    public CuVp(Context context) {
        super(context);
    }

    public CuVp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);
        //measure self
        setMeasuredDimension(w,h);
        //measure child
        measureChildView();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b);
        layoutChild();
    }

    private void layoutChild(){
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
}
