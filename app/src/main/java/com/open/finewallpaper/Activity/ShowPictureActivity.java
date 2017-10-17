package com.open.finewallpaper.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.open.finewallpaper.Fragment.Fragment1;
import com.open.finewallpaper.Fragment.Fragment2;
import com.open.finewallpaper.R;

public class ShowPictureActivity extends BaseActivity {
    private final static String TAG = "ShowPictureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        setNeedGesture(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.showpicture_activity,new Fragment1())
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in,R.anim.slide_left_out,
                        R.anim.slide_left_in ,R.anim.slide_right_out)
                .replace(R.id.showpicture_activity,new Fragment2())
                .commit();
        setSwipeRightListener(new swipeRightListener() {
            @Override
            public void swipeRight() {

            }
        });
    }
}
