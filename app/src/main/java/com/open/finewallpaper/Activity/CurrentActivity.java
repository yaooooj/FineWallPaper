package com.open.finewallpaper.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.open.finewallpaper.R;
import com.open.finewallpaper.SwishBackActivity.SwipeBackActivity;

public class CurrentActivity extends SwipeBackActivity {
    private final static String TAG = "CurrentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_activtiy);

        Log.e(TAG, "onCreate: " );
    }



    @Override
    public Activity getNextActivity() {
        Log.e(TAG, "getNextActivity: " );
        return new Activity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart:" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }
}
