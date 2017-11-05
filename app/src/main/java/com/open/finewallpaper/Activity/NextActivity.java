package com.open.finewallpaper.Activity;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.open.finewallpaper.Fragment.Fragment1;
import com.open.finewallpaper.Fragment.MainFragment;
import com.open.finewallpaper.Fragment.NextFragment;
import com.open.finewallpaper.R;

import java.util.ArrayList;

public class NextActivity extends AppCompatActivity implements NextFragment.OnFragmentInteractionListener {
    private final static String TAG = "NextActivity";
    private PopupWindow popUpWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_next);
        //BackUtil.attach(this);
        Log.e(TAG, "onCreate: " );

        init();
        NextFragment nextFragment;
        Bundle bundle = getIntent().getBundleExtra("urls");

        if (bundle != null){

            nextFragment = NextFragment.newInstance(bundle.getParcelableArrayList("url"),bundle.getInt("position"));
        }else {

            nextFragment = NextFragment.newInstance(null,-1);
        }
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_layout,null,true);
        popUpWindow = new PopupWindow(view);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.next_activity,nextFragment)
                .show(nextFragment)
                .commit();
    }

    public void init(){

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }



    private void backGroudAlpha(){
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
