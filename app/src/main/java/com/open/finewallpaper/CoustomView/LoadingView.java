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

    private Paint mPaint;

    private Paint textPaint;


    //view大小
    private int result;

    //padding大小
    private int padding;

    // View中心
    private int center;

    private int textColor;

    private int color;

    private int height ;

    private int width ;

    private Path path;

    private Path textPath;

    private int textTopx;

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

        result = dp2px(100);


        int screenWidth = DisplayUtil.getsWidthPixles(context);
        int screenHeight = DisplayUtil.getsHightPixles(context);
        float ratioWidth = (float)screenWidth / 720;
        float ratioHeight = (float)screenHeight / 1080;
        float RATIO = Math.min(ratioWidth, ratioHeight);
        textTopx = Math.round(20 * RATIO);
    }






    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高的mode和size
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //测量宽度
        if (widthMode == MeasureSpec.AT_MOST) {
            width = result;
        } else {
            width = widthSize;
        }

        //测量高度
        if (heightMode == MeasureSpec.AT_MOST) {
            height = result;
        } else {
            height = heightSize;
        }

        //设置测量的宽高值
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        padding = dp2px(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);

        center = getWidth() / 2;

        drawRect(canvas);

        drawArc(canvas);

        drawText(canvas);
    }



    private void drawRect(Canvas canvas){

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

        //canvas.drawRect(width / 4,height / 2 - width / 8 ,3 * width / 4, height / 2 + width / 6,mPaint);
        canvas.drawRect(padding, height / 3 - padding,width - padding,height - padding - padding ,mPaint);
    }

    private void drawArc(Canvas canvas){

        path = new Path();
        //path.moveTo(3 * width / 8,height / 2 - width / 8 );
        // path.quadTo(width / 2, height / 6, 5 * width / 8,height / 2 - width / 8 );
        path.moveTo(width / 5 , height / 3 - padding);

        path.quadTo(width / 2 , -10 , 4 * width / 5, height / 3 - padding);

        canvas.drawPath(path, mPaint);
    }

    private void drawText(Canvas canvas){



        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(0);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(textTopx);

        int textCenter = ((height - padding ) - ( height / 3 - padding)) / 2;
        Log.e(TAG, "textCenter: " +textCenter + "Center:" + center );
        //获取文字的宽度 用于绘制文本内容
        float textWidth = textPaint.measureText("Loading");
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top + height / 3 - padding ) / 2 - fontMetrics.top ;
        canvas.drawText("Loading", center - textWidth / 2, baseline ,textPaint);
    }

    /**
     * dp转px
     */
    public int dp2px(int dp) {

        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}














