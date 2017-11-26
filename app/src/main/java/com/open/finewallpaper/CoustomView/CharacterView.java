package com.open.finewallpaper.CoustomView;


import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;


/**
 * Created by SEELE on 2017/11/23.
 */

public class CharacterView extends View {
    private Paint mPaint;
    private Rect mTextBounds = new Rect();
    private int mTextSize = sp2px(30);
    private int mTextColor = 0xFF000000;

    private final static String type = "L";

    private int baseLine;
    private int center;

    private int times;
    private int mCurNum = 0;
    private int mDeltaNum;

    public static final int DEFAULT_VELOCITY = 20;
    private int mVelocity = DEFAULT_VELOCITY;

    private char character;


    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private float mOffset = 0;

    private int mTextHeight;


    private OnFinish mOnFinish;




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


    public void setCharacter(final char character, final int times, long delay) {
        postDelayed(new Runnable() {
            @Override
            public void run() {

                setCharacter(character,times);
                mDeltaNum = 1 + times;
            }
        }, delay);
    }


    public void setCharacter(final char character,int times){

        this.character = character;
        this.times = times;
        this.mDeltaNum =  times + 1;

        invalidate();
    }


    private void measureTextHeight() {
        mPaint.getTextBounds( type, 0, 1, mTextBounds);
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
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                mPaint.getTextBounds(type, 0, 1, mTextBounds);
                result = mTextBounds.width();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, widthSize) : result;
        return result + getPaddingTop() + getPaddingBottom() + dp2px(5);
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
                mPaint.getTextBounds(type, 0, 1, mTextBounds);
                result = mTextBounds.height();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, heightSize) : result;
        return result + getPaddingTop() + getPaddingBottom() + dp2px(15);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        center = (getMeasuredWidth()- getPaddingLeft() - getPaddingRight()) / 2;
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        baseLine = (h - fontMetrics.bottom + fontMetrics.top  - getPaddingBottom() - getPaddingTop())/ 2 - fontMetrics.top;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCurNum != times) {
            postDelayed(mScrollRunnable, 0);
        }else{
            if (mOnFinish != null){
                mOnFinish.finished();
            }
        }

        canvas.translate(0, mOffset * getMeasuredHeight());
        drawText(canvas);
        drawNext(canvas);

    }


    private void drawText(Canvas canvas){
        canvas.drawText(character + "",center,baseLine,mPaint);
    }

    private void drawNext(Canvas canvas) {
        float y = (float) (getMeasuredHeight() * 1.5);
        canvas.drawText(character + "", center, y + mTextHeight / 2, mPaint);
    }


    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            float x = (float) (1 - 1.0 * (times - mCurNum) / mDeltaNum);
            mOffset -= mVelocity * 0.01f * (1 - mInterpolator.getInterpolation(x) + 0.1);
            invalidate();
            if (mOffset <= -1) {
                mOffset = 0;
                calNum(mCurNum + 1);
            }
        }
    };


    private void calNum(int number) {
        if (number > 20){
            throw new IllegalArgumentException("length more than 20");
        }
        number = number == -1 ? 9 : number;
        number = number == 20 ? 0 : number;
        mCurNum = number;
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }


    private int sp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }


    public void setOnFinish(OnFinish onFinish) {
        mOnFinish = onFinish;
    }

    public interface OnFinish{
        void finished();
    }

}
