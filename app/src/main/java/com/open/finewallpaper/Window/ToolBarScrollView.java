package com.open.finewallpaper.Window;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;


/**
 * Created by SEELE on 2017/10/27.
 */

public class ToolBarScrollView extends NestedScrollView {
    private static final String TAG = "ToolBarScrollView";
    private onScrollChangeListener mScrollChangeListener;

    public ToolBarScrollView(Context context) {
        super(context);
    }

    public ToolBarScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if (mScrollChangeListener != null){
            mScrollChangeListener.onScrollListener(h);
            Log.e(TAG, "onSizeChanged: "+ h);
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setScrollChangeListener(onScrollChangeListener scrollChangeListener) {
        mScrollChangeListener = scrollChangeListener;
    }

    public interface onScrollChangeListener{
        void onScrollListener(int y);
    }
}
