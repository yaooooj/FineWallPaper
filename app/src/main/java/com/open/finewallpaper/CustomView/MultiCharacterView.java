package com.open.finewallpaper.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.open.finewallpaper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/11/26.
 */

public class MultiCharacterView extends LinearLayout {
    private final static String TAG = "MultiCharacterView";
    private Context mContext;
    private List<CharacterView> mViews = new ArrayList<>();
    private OnFreshListener mOnFreshListener;
    private Type type = Type.CH;
    public enum Type{
        EN,CH
    }
    private int mTextSize = sp2px(30);
    private int times = 5;
    private int textColor = 0xFF000000;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private String mFontFileName;
    private int mVelocity = 15;

    public MultiCharacterView(Context context) {
        super(context);
        mContext = context;
    }

    public MultiCharacterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MultiCharacterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiCharacterView);
        mTextSize = typedArray.getInteger(R.styleable.MultiCharacterView_textSize,130);
        //spiteCharacter("Loading");
        typedArray.recycle();

        setOrientation(HORIZONTAL);

        setGravity(Gravity.CENTER);
    }


    public void setText(String strings){
        if (TextUtils.isEmpty(strings)){
            throw new IllegalArgumentException("text can not be null");
        }
        if (strings.length() > 20){
            throw new IllegalArgumentException("length more than 20");
        }
        spiteCharacter(strings);
    }

    public void setText(String strings,Type type){
        this.type = type;
        spiteCharacter(strings);
    }
    public void setText(String strings,Type type,int times){
        this.times = times;
        this.type = type;
        spiteCharacter(strings);
    }

    public void dismiss(){
        for (CharacterView s : mViews) {
            s.setStop(true);
        }
    }

    public void show(){
        for (CharacterView s : mViews) {
            s.setStop(false);
        }
    }


    private void spiteCharacter(String strings){

        String str = String.valueOf(strings);
        Log.e(TAG, "spiteCharacter: " + str );
        char[] charArray = str.toCharArray();
        for (int i = 0;i < charArray.length;i++){
            Log.e(TAG, "spiteCharacter: " + charArray[i] );
            CharacterView characterView = new CharacterView(mContext);
            characterView.setCharacter(str.charAt(i),times,type);
            characterView.setTextSize(mTextSize);
            characterView.setTextColor(textColor);
            characterView.setVelocity(mVelocity);
            mViews.add(characterView);
            addView(characterView);
            times += 1;
        }
        mViews.get(mViews.size()-1).setOnFinish(new CharacterView.OnFinish() {
            @Override
            public void finished() {
                for (CharacterView s : mViews) {
                    s.setStop(false);
                }
                if (mOnFreshListener != null){
                    mOnFreshListener.onFresh();
                }
            }
        });
    }


    public void setOnFreshListener(OnFreshListener onFreshListener) {
        mOnFreshListener = onFreshListener;
    }

    public interface OnFreshListener{

        void onFresh();
    }


    public void setTextSize(int textSize) {
        if (textSize <= 0) throw new IllegalArgumentException("text size must > 0!");
        mTextSize = textSize;
        for (CharacterView s : mViews) {
            s.setTextSize(textSize);
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        for (CharacterView s : mViews) {
            s.setTextColor(textColor);
        }

    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null)
            throw new IllegalArgumentException("interpolator couldn't be null");
        mInterpolator = interpolator;
        for (CharacterView s : mViews) {
            s.setInterpolator(interpolator);
        }
    }

    public void setTextFont(String fileName) {
        if (TextUtils.isEmpty(fileName)) throw new IllegalArgumentException("file name is null");
        mFontFileName = fileName;
        for (CharacterView s : mViews) {
            s.setTextFont(fileName);
        }
    }

    public void setScrollVelocity(int velocity) {
        if (velocity < 0 || velocity > 1000){
            throw new IllegalArgumentException("velocity can't less than 0 or more than 1000");
        }
        mVelocity = velocity;
        for (CharacterView s : mViews) {
            s.setVelocity(velocity);
        }
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }

    public static class CharacterView extends View {
        private final static String TAG = "CharacterView";
        private Context mContext;
        /**
         * 初始化画笔
         */
        private Paint mPaint;
        private Rect mTextBounds = new Rect();
        /**
         * 字体大小
         */
        private int mTextSize = sp2px(16);
        /**
         * 字体颜色
         */
        private int mTextColor = 0xFF000000;
        /**
         * 测量字体类型，显示的是英文字符或中文字符
         */

        private final static String EN = "L";
        private final static String CH = "正";
        private String type = EN;
        /**
         * 画字符时的基准线
         */
        private int baseLine;
        /**
         * 画字符时所在X轴的位置
         */
        private int center;
        /**
         * 重复转动的次数，默认为5
         */
        private int times = 5;
        /**
         * 当前转动的次数
         */
        private int mCurNum = 0;
        /**
         * 当前值和目标值之间的差值
         */
        private int mDeltaNum = 6;
        /**
         * 默认初始化时的速度
         */
        public static final int DEFAULT_VELOCITY = 20;
        private int mVelocity = DEFAULT_VELOCITY;

        /**
         * 全局字符变量
         */
        private char character;
        /**
         * 默认插值器
         */
        private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
        /**
         * 转动字符时的偏移值
         */
        private float mOffset = 0;
        /**
         * 字符的高度
         */
        private int mTextHeight;
        /**
         * 完成转动后回调接口
         */
        private OnFinish mOnFinish;

        private boolean mDismiss = false;

        private Typeface mTypeface;

        private int delay = 50;

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
            mPaint.setStrokeWidth(0);

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


        public void setCharacter(final char character,int times,Type t){
            this.character = character;
            this.times = times;
            this.mDeltaNum =  times + 1;
            type = t == Type.CH ? CH : EN;
            delay = t == Type.CH ? 50 : 0;
            requestLayout();
            invalidate();
        }

        public void setCharacter(final char character){
            this.character = character;
            invalidate();
        }

        public void setCharacter(final char character,int times){
            this.character = character;
            this.times = times;
            this.mDeltaNum =  times + 1;
            invalidate();
        }

        public void setTextSize(int textSize) {
            //this.mTextSize = sp2px(textSize);
            mPaint.setTextSize(textSize);
            measureTextHeight();
            requestLayout();
            invalidate();
        }


        public void setTextColor(int mTextColor) {
            this.mTextColor = mTextColor;
            mPaint.setColor(mTextColor);
            invalidate();
        }


        public void setTextFont(String fileName) {


            if (TextUtils.isEmpty(fileName))
                throw new IllegalArgumentException("please check file name end with '.ttf' or '.otf'");
            mTypeface = Typeface.createFromAsset(mContext.getAssets(), fileName);
            if (mTypeface == null) throw new RuntimeException("please check your font!");
            mPaint.setTypeface(mTypeface);
            requestLayout();
            invalidate();
        }

        public void setInterpolator(Interpolator interpolator) {
            mInterpolator = interpolator;
        }


        public void setVelocity(int velocity) {
            mVelocity = velocity;

        }

        public void setStop(boolean dismiss){
            this.mDismiss = dismiss;
            if (dismiss){
                invalidate();
            }else {
                mCurNum = 0;
                postInvalidateDelayed(delay);
            }
        }


        private void measureTextHeight() {
            mPaint.getTextBounds( type, 0, 1, mTextBounds);
            mTextHeight = mTextBounds.height();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            //获取宽高的大小
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
            if (mDismiss){
               mOffset = 0;
            }else {
                if (mCurNum != times) {
                    postDelayed(mScrollRunnable, 0);
                }else{
                    if (mOnFinish != null){
                        mOnFinish.finished();
                    }
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
                if (mDismiss){
                    mOffset = 0;
                    mCurNum = times;
                }
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
}
