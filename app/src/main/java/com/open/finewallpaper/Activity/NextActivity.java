package com.open.finewallpaper.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.open.finewallpaper.R;

public class NextActivity extends AppCompatActivity {
    private final static String TAG = "NextActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Log.e(TAG, "onCreate: " );
    }
}
