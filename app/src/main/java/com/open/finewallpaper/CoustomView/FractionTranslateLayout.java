package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by yaojian on 2017/10/17.
 */

public class FractionTranslateLayout extends RelativeLayout {

    private int screenWidth;
    private float fraction;
    private OnLayoutTranslateListener onLayoutTranslateListener;


    public FractionTranslateLayout(Context context) {
        super(context);
    }

    public FractionTranslateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FractionTranslateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        screenWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    public float getFraction() {
        return fraction;
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
        setX((screenWidth > 0)?(fraction * screenWidth) : 0);
        if (fraction == 1 || fraction == -1){
            setAlpha(1);
        }else if (fraction < 1 || fraction > -1){
            if (getAlpha() != 1){
                setAlpha(1);
            }
        }if (onLayoutTranslateListener != null){
            onLayoutTranslateListener.onLayoutTranslate(this,fraction);
        }

    }

    public void setOnLayoutTranslateListener(OnLayoutTranslateListener onLayoutTranslateListener) {
        this.onLayoutTranslateListener = onLayoutTranslateListener;
    }

    public static interface OnLayoutTranslateListener{
        void onLayoutTranslate(FractionTranslateLayout view,float fraction);
    }

}
