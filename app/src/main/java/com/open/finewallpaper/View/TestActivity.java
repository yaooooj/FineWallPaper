package com.open.finewallpaper.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.open.finewallpaper.BaseActivity;
import com.open.finewallpaper.CoustomView.CharacterView;
import com.open.finewallpaper.CoustomView.MultiCharacterView;
import com.open.finewallpaper.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "SetActivity";
    private CharacterView ca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewa);


        Log.e(TAG, "onCreate: " );
        ca  = (CharacterView) findViewById(R.id.charview);

        MultiCharacterView multiCharacterView = (MultiCharacterView) findViewById(R.id.multi_view);
        multiCharacterView.setText("Loading");

        MultiCharacterView multiCharacterView1 = (MultiCharacterView) findViewById(R.id.load_more);
        multiCharacterView1.setText("Refresh");
        multiCharacterView1.setOnFreshListener(new MultiCharacterView.OnFreshListener() {
            @Override
            public void onFresh() {
                Toast.makeText(TestActivity.this,"refresh complete",Toast.LENGTH_SHORT).show();
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button){
            Log.e(TAG, "onClick: " );
           // ca.setAnimation();
        }
    }
}
