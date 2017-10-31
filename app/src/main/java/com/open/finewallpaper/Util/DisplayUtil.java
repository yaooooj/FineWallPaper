package com.open.finewallpaper.Util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by yaojian on 2017/10/31.
 */

public class DisplayUtil {
    public static int sWidthPixles;
    public static int sHightPixles;

    public static void init(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);

        sWidthPixles = point.x;
        sHightPixles = point.y;
    }

    public static int getsWidthPixles(Context context) {
        init(context);
        return sWidthPixles;
    }

    public static void setsWidthPixles(int sWidthPixles) {
        DisplayUtil.sWidthPixles = sWidthPixles;
    }

    public static int getsHightPixles(Context context) {
        init(context);
        return sHightPixles;
    }

    public static void setsHightPixles(int sHightPixles) {
        DisplayUtil.sHightPixles = sHightPixles;
    }
}
