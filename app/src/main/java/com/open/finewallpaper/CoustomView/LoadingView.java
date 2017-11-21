package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.DisplayUtil;

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

    private Path path;

    private Path textPath;
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




        int screenWidth = DisplayUtil.getsWidthPixles(context);
        int screenHeight = DisplayUtil.getsHightPixles(context);
        float ratioWidth = (float)screenWidth / 720;
        float ratioHeight = (float)screenHeight / 1080;
        float RATIO = Math.min(ratioWidth, ratioHeight);
        int textSize = Math.round(20 * RATIO);


        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(2f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
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

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        path = new Path();
        path.moveTo(3 * width / 8,height / 2 - width / 8 );
        path.quadTo(width / 2, height / 6, 5 * width / 8,height / 2 - width / 8 );

        textPath = new Path();
        textPath.moveTo(3 * width / 8,height * 17 / 32);
        textPath.lineTo(5 * width / 8,height * 17 / 32);

    }

    @Override
    protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);

        canvas.drawARGB(1,2,3,4);

        Log.e(TAG, "init: next  " + height +  "   " + width  );
        //new RectF(3 * width / 8,height / 3,5 * width / 8,height / 2)
        canvas.drawRect(width / 4,height / 2 - width / 8 ,3 * width / 4, height / 2 + width / 6,mPaint);
        //canvas.drawText("Loading",3 * width / 8,7 * height / 12,5 * width / 8,7 * height / 12,textPaint);
       // canvas.drawArc(new RectF(3 * width / 8,height / 3,5 * width / 8,height / 2),180,190,false,mPaint);
        canvas.drawPath(path, mPaint);
        canvas.drawTextOnPath("Loading",textPath,0,-8,textPaint);

    }
}














