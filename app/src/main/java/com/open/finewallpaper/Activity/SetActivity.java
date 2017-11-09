package com.open.finewallpaper.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.open.finewallpaper.Fragment.SettingFragment;
import com.open.finewallpaper.R;

public class SetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        setShowToolbar(true);

    }


}
