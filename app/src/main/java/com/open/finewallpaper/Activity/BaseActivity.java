package com.open.finewallpaper.Activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.open.finewallpaper.R;


public class BaseActivity extends AppCompatActivity {
    private boolean isShowToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (isShowToolbar){
            initView();
        }

    }

    public void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.base_tb);
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
    }

    public boolean isShowToolbar() {
        return isShowToolbar;
    }

    public void setShowToolbar(boolean showToolbar) {
        isShowToolbar = showToolbar;
    }
}




