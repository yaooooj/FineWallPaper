package com.open.finewallpaper.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.open.finewallpaper.Fragment.Fragment1;
import com.open.finewallpaper.Fragment.MainFragment;
import com.open.finewallpaper.Fragment.NextFragment;
import com.open.finewallpaper.R;

public class NextActivity extends AppCompatActivity implements NextFragment.OnFragmentInteractionListener {
    private final static String TAG = "NextActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        //BackUtil.attach(this);
        Log.e(TAG, "onCreate: " );
        init();
        NextFragment nextFragment;
        Bundle bundle = getIntent().getBundleExtra("urls");
        if (bundle != null){
            nextFragment = NextFragment.newInstance(bundle.getStringArrayList("url"),bundle.getInt("position"));
        }else {
            nextFragment = NextFragment.newInstance(null,-1);
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.next_activity,nextFragment)
                .show(nextFragment)
                .commit();
    }

    public void init(){


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
