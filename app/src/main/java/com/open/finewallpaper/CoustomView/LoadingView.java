package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.open.finewallpaper.R;

/**
 * Created by yaojian on 2017/11/20.
 */

public class LoadingView  extends View{
    private String LOADTEXT = "loading";
    private Paint mPaint;
    private Paint textPaint;
    private TextView mTextView;

    private int textColor;
    private int color;
    private RectF rect;

    private int height ;
    private int width ;
    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        color = ta.getColor(R.styleable.LoadingView_load_color,Color.BLACK);
        textColor = ta.getColor(R.styleable.LoadingView_load_textColor,Color.RED);
        ta.recycle();
        init(context);
    }

    private void  init(Context context){
        mTextView = new TextView(context);
        mTextView.setTextColor(textColor);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(color);

        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16);
        rect = new RectF(3 * width / 8,height / 3 - width / 8 ,8 * width / 5,height / 3);
    }

    public void setText(String text){
        mTextView.setText(text);
    }

    public void setTextColor(int color){
        this.textColor = color;
    }

    public void  setColor(int color){
        this.color = color;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMeasureSpec == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(200,200);
        }else if (widthMeasureSpec == MeasureSpec.AT_MOST ){
            setMeasuredDimension(200,heightSpecSize);
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,200);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
       // super.onDraw(canvas);
        height = getHeight();
        width = getWidth();
        canvas.drawRect(width / 4,height / 3,3 * width / 4,height * 2 / 3,mPaint);
        canvas.drawText((CharSequence) mTextView,3 * width / 8,7 * height / 12,5 * width / 8,7 * height / 12,textPaint);
        canvas.drawArc(rect,180,180,false,mPaint);
    }
}














