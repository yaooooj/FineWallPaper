package com.open.finewallpaper.View;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.open.finewallpaper.HTTP.NetWorkUtils;
import com.open.finewallpaper.R;

public class ErrorActivity extends AppCompatActivity {
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_error);
        mButton = (Button) findViewById(R.id.network_error_bt);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetWorkUtils.isNetworkConnected(ErrorActivity.this)){
                    setResult(1);
                    finish();
                }
            }
        });
    }


}
