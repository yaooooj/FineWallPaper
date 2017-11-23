package com.open.finewallpaper.CoustomView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by SEELE on 2017/11/23.
 */

public class CharacterView extends View {
    private Paint mPaint;
    private Rect mTextBounds = new Rect();
    private int mTextSize = sp2px(30);
    private int mTextColor = 0xFF000000;

    private int width;
    private int height;

    private int times = 5;

    private int mOffset = 0;

    private int mTextHeight;



    private final static String TAG = "CharacterView";

    public CharacterView(Context context) {
        super(context);
        init();
    }

    public CharacterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);

        measureTextHeight();
    }

    private void measureTextHeight() {
        mPaint.getTextBounds( "L", 0, 1, mTextBounds);
        mTextHeight = mTextBounds.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽高的size
        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);

        setMeasuredDimension(w,h);

    }

    private int measureWidth(int widthMeasureSpec){
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (mode){
            case MeasureSpec.AT_MOST:
                result = widthSize;
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                mPaint.getTextBounds("L", 0, 1, mTextBounds);
                result = mTextBounds.width();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, widthSize) : result;
        return result + getPaddingTop() + getPaddingBottom() + dp2px(40);
    }

    private int measureHeight(int heightMeasureSpec){
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode){
            case MeasureSpec.AT_MOST:
                result = heightSize;
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                mPaint.getTextBounds("L", 0, 1, mTextBounds);
                result = mTextBounds.height();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, heightSize) : result;
        return result + getPaddingTop() + getPaddingBottom() + dp2px(40);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        drawText(canvas);canvas.translate(0, (float) (mOffset * getMeasuredHeight() * 0.01));
        canvas.restore();
    }


    private void drawText(Canvas canvas){
        int height = getMeasuredHeight();

        float textWidth = mPaint.measureText("L");

        int center = (getMeasuredWidth()- getPaddingLeft() - getPaddingRight()) / 2;
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseLine = (height - fontMetrics.bottom + fontMetrics.top  - getPaddingBottom() - getPaddingTop())/ 2 - fontMetrics.top;
        canvas.drawText("L",center,baseLine,mPaint);

    }



    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }


    private int sp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }



    public void setAnimation(Context context) {
        Log.e(TAG, "onAnimationUpdate: ");
        ValueAnimator transAnimator = ValueAnimator.ofInt(getMeasuredHeight() / 2, getMeasuredHeight(),0,getMeasuredHeight() / 2);
        //ObjectAnimator transAnimator = ObjectAnimator.ofFloat(new CharacterView(context),"fraction",getMeasuredHeight() / 2, getMeasuredHeight(),0,getMeasuredHeight() / 2);
        transAnimator.setDuration(2000);
        transAnimator.setRepeatCount(times);
        transAnimator.setInterpolator( new LinearInterpolator());
        transAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mOffset = (int) animation.getAnimatedValue();

                Log.e(TAG, "onAnimationUpdate: " + mOffset );
                invalidate();
            }
        });

        transAnimator.start();
    }

}
