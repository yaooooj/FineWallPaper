package com.open.finewallpaper.Activity;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by yaojian on 2017/10/17.
 */

public class BackGestureListener implements GestureDetector.OnGestureListener {

    private Activity activity;



    private swipeRightListener swipRightListener;

    public BackGestureListener(Activity activity) {
        this.activity =  activity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (e2.getX() - e1.getX() > 100 && Math.abs(e2.getY() - e1.getY()) < 60){
            activity.onBackPressed();
        }
        if (e1.getX() - e2.getX() > 100 && Math.abs(e2.getY() - e1.getY()) < 60){
            swipRightListener.swipRight();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void setSwipRigthListener(swipeRightListener swipeRightListener) {
        this.swipRightListener = swipeRightListener;
    }


    public interface swipeRightListener {
        void swipRight();
    }
}
