package com.open.finewallpaper.Util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.open.finewallpaper.R;

/**
 * Created by yaojian on 2017/11/2.
 */

public class AlphaBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "AlphaBehavior";
    private int offset = 0;

    private int endOffset = 0;
    private Context context;

    private float mLastMotionX;
    private float mLastMotionY;

    public AlphaBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }





}



























