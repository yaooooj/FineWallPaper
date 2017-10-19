package com.open.finewallpaper.SwishBackActivity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by SEELE on 2017/10/19.
 */

public class ActivityLifecycleHelper implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityLifecycleHelper";
    private static ActivityLifecycleHelper singleton;
    private static final Object lockObj = new Object();
    private static List<Activity> activities;
    //private static ArrayMap<String,Activity> activities;
    private ActivityLifecycleHelper() {
        activities = new LinkedList<>();
        //activities = new ArrayMap<>();
    }

    public static ActivityLifecycleHelper build() {
        synchronized (lockObj) {
            if (singleton == null) {
                singleton = new ActivityLifecycleHelper();
            }
            return singleton;
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if(activities.contains(activity)) {
            activities.remove(activity);
        }

        if(activities.size() == 0) {
            activities = null;
        }
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activities == null) {
            activities = new LinkedList<>();
        }

        if (activities.contains(activity)){
            return;
        }
        if (BuildConfig.DEBUG){
            Log.e(TAG, "addActivity: " + "current activity size  " + activities.size() );
        }
        activities.add(activity);
    }

    /**
     * 获取集合中当前Activity
     * @return
     */
    public static Activity getLatestActivity() {
        ActivityLifecycleHelper adapter = build();
        int count = activities.size();
        if (count == 0) {
            return null;
        }
        return activities.get(count - 1);
    }

    /**
     * 获取集合中上一个Activity
     * @return
     */
    public static Activity getPreviousActivity(){
        ActivityLifecycleHelper adapter = build();
        int count = activities.size();
        if (count < 2) {
            return null;
        }
        return activities.get(count - 2);
    }

    public void addNewActivity(Activity activity){
        if (activities == null) {
            activities = new LinkedList<>();
        }

        if (BuildConfig.DEBUG){
            Log.e(TAG, "addNewActivity: " + "current activity size  " + activities.size() );
        }

        if (activities.contains(activity)){
            return;
        }
        activities.add(activity);
    }
}
