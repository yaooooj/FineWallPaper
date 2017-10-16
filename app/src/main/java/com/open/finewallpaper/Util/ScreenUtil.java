package com.open.finewallpaper.Util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by yaojian on 2017/10/16.
 */

public class ScreenUtil {
    public int color;
    private View statusBarView;
    private ViewGroup decorViewGroup;
    public static int getStatusHeight(Context context){
        int statusHeight = -1;
        try{
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    public static int getStatusBarHeight(Context context){
        int statusBarHeight = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0 ){
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        Log.e("ScreenUtil","StatusBarHeight = "+statusBarHeight);
        return statusBarHeight;
    }

    private void createStausBarView(Window window){
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        decorViewGroup = (ViewGroup) window.getDecorView();
        statusBarView = new View(window.getContext());
    }
    public void setStatusView(Window window){
        createStausBarView(window);
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.
                LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        decorViewGroup.addView(statusBarView);
    }

    public void removeStatusView(){
        decorViewGroup.removeView(statusBarView);
    }

    public void setColor(int color){
        this.color = color;
    }
    public int getColor(){
        return color;
    }
}
