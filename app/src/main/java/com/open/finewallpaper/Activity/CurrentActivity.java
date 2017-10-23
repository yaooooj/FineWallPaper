package com.open.finewallpaper.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.open.finewallpaper.R;
import com.open.finewallpaper.SwishBackActivity.SwipeBackActivity;
import com.open.finewallpaper.Window.BackUtil;
import com.open.finewallpaper.Window.VerticalDrawerLayout;

public class CurrentActivity extends AppCompatActivity {
    private final static String TAG = "CurrentActivity";

    private VerticalDrawerLayout    mVerticalDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_activtiy);
        mVerticalDrawerLayout = (VerticalDrawerLayout) findViewById(R.id.layout_id);
        Log.e(TAG, "onCreate: " );
    }






}
