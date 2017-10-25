package com.open.finewallpaper.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.open.finewallpaper.Adapter.CurrentAdapter;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Window.VerticalDrawerLayout;

import java.util.ArrayList;
import java.util.List;

public class CurrentActivity extends AppCompatActivity {
    private final static String TAG = "CurrentActivity";

    private VerticalDrawerLayout  mVerticalDrawerLayout;
    private RecyclerView recyclerView;

    private List<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_activtiy);

        mVerticalDrawerLayout = (VerticalDrawerLayout) findViewById(R.id.layout_id);
        init();

        recyclerView = (RecyclerView) findViewById(R.id.current_rv);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

       recyclerView.setAdapter(new CurrentAdapter(CurrentActivity.this,data));
    }

    private void init(){
        data = new ArrayList<>();
        for (int i = 0; i < 20;i ++){
            data.add("! GO GO !!  " + i );
        }

    }





}
