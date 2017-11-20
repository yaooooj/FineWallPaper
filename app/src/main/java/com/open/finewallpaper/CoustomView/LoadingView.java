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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.open.finewallpaper.R;

/**
 * Created by yaojian on 2017/11/20.
 */

public class LoadingView  extends View{
    private final static String TAG = "LoadingView";
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
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        Log.e(TAG, "init:   " + height +  "   " + width  );
        mTextView = new TextView(context);
        mTextView.setTextColor(textColor);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(16);
        //RectF rect = new RectF(3 * width / 8,(height / 3) - (width / 8) ,8 * width / 5,height / 3);
        rect = new RectF(0,0,200,200);
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

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(100,100);
        }else if (widthMeasureSpec == MeasureSpec.AT_MOST ){
            setMeasuredDimension(100,heightSpecSize);
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,100);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);
        height = getHeight();
        width = getWidth();
        canvas.drawARGB(1,2,3,4);

        Log.e(TAG, "init: next  " + height +  "   " + width  );
        canvas.drawRect(width / 4,height / 3 ,3 * width / 4,height/2,mPaint);
        //canvas.drawText("Loading",3 * width / 8,7 * height / 12,5 * width / 8,7 * height / 12,textPaint);
        canvas.drawArc(new RectF(3 * width / 8,height / 3,5 * width / 8,height / 2),-180,180,false,mPaint);
    }
}














