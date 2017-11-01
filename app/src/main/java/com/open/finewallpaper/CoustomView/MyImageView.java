package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.open.finewallpaper.R;

/**
 * Created by SEELE on 2017/11/1.
 */

public class MyImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = "MyImageView";
    private float ration;
    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.imageScale);
        ration = array.getFloat(R.styleable.imageScale_ration, 0f);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null){
            Log.e(TAG, "onMeasure: " );
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) Math.ceil(widthSize * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth());
            setMeasuredDimension(widthSize,height);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

}
