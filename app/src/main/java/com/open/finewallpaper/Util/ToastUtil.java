package com.open.finewallpaper.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by SEELE on 2017/10/15.
 */

public class ToastUtil {

    public static void show(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();

    }
}
