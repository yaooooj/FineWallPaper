package com.open.finewallpaper.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by yaojian on 2017/10/31.
 */

public class DisplayUtil {
    private static int screenWidth;
    private static int screenHeight;

    public static void init(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        //screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        //screenHeight = (int) (height / density);// 屏幕高度(dp)
        screenWidth = width;
        screenHeight = height;
    }

    public static int getsWidthPixles(Context context) {
        init(context);
        return screenWidth;
    }

    public static void setsWidthPixles(int sWidthPixles) {
        DisplayUtil.screenWidth = sWidthPixles;
    }

    public static int getsHightPixles(Context context) {
        init(context);
        return screenHeight;
    }

    public static void setsHightPixles(int sHightPixles) {
        DisplayUtil.screenHeight = sHightPixles;
    }
}
