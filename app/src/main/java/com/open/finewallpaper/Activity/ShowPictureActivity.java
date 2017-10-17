package com.open.finewallpaper.Activity;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.open.finewallpaper.CoustomView.FractionTranslateLayout;
import com.open.finewallpaper.Fragment.Fragment1;
import com.open.finewallpaper.Fragment.Fragment2;
import com.open.finewallpaper.R;

public class ShowPictureActivity extends BaseActivity {
    private final static String TAG = "ShowPictureActivity";
    private Fragment2 fragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        setNeedGesture(true);
        fragment2 = new Fragment2();
        final View view =  fragment2.getView();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.showpicture_activity,new Fragment1())
                .add(R.id.showpicture_activity,fragment2)
                .hide(fragment2)
                .addToBackStack(null)
                .commit();

        setSwipeRightListener(new swipeRightListener() {
            @Override
            public void swipeRight(float dx) {
                Log.e(TAG, "swipeRight: " );
                if (view != null){
                    Log.e(TAG, "swipeRight: " + "view not null" );
                    getSupportFragmentManager().beginTransaction()
                            .show(fragment2)
                            .commit();
                    view.scrollBy((int) dx,0);
                }
            }
        });
    }


    public void addFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in,R.anim.slide_left_out,
                        R.anim.slide_left_in ,R.anim.slide_right_out)
                .replace(R.id.showpicture_activity,new Fragment2())
                .commit();
    }

    public void startFragment(){

    }

}
