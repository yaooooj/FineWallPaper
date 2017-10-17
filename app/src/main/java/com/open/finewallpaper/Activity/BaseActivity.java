package com.open.finewallpaper.Activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;


public class BaseActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private boolean isNeedGesture;
    private swipeRightListener swipeRightListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGestureDetector();
    }

    public void initGestureDetector(){
        if (gestureDetector == null){
            gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.OnGestureListener() {
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
                        onBackPressed();
                    }
                    if (e1.getX() - e2.getX() > 100 && Math.abs(e2.getY() - e1.getY()) < 60){
                        swipeRightListener.swipeRight(e1.getX() - e2.getX());
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
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isNeedGesture){
            return gestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setNeedGesture(boolean needGesture) {
        isNeedGesture = needGesture;
    }

    public void setSwipeRightListener(swipeRightListener swipeRightListener) {
        this.swipeRightListener = swipeRightListener;
    }


    public interface swipeRightListener {
        void swipeRight(float dx);
    }
}
