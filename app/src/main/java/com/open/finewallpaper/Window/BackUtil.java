package com.open.finewallpaper.Window;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.open.finewallpaper.R;

/**
 * Created by SEELE on 2017/10/22.
 */

public class BackUtil{

    private final static String TAG = "BackUtil";

    public static boolean attach(Activity activity){

        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        View oldScreen = decorView.getChildAt(0);
        decorView.removeViewAt(0);


        BackFrame panel = new BackFrame(activity, oldScreen);
        panel.setId(R.id.slidable_panel);
        oldScreen.setId(R.id.slidable_content);
        panel.addView(oldScreen);
        decorView.addView(panel, 0);
        return false;
    }


    public static void attach(Activity activity,Class clazz){
        Intent in  = new Intent(activity.getApplication(),clazz);
        activity.getApplication().startActivity(in);
    }



}
