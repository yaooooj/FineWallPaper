package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by SEELE on 2017/11/3.
 */

public class ToolbarRecycler extends RecyclerView {
    private Toolbar toolbar;
    private static final String TAG = "ToolbarRecycler";

    public ToolbarRecycler(Context context) {
        super(context);
    }

    public ToolbarRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolbarRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTitle(Toolbar toolbar){
        this.toolbar = toolbar;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float headHeight = 300
                - toolbar.getMeasuredHeight();
        int alpha = (int) (((float) h / headHeight) * 255);
        Log.e(TAG, "onSizeChanged: " +  alpha);
        if (alpha >= 255)
            alpha = 255;
        if (alpha <= 10)
            alpha = 0;
        toolbar.getBackground().setAlpha(alpha);
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
