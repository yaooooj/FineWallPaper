package com.open.finewallpaper.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.open.finewallpaper.Adapter.MainFragmentAdapter;

import com.open.finewallpaper.CoustomView.ToolbarRecycler;
import com.open.finewallpaper.Fragment.MainFragment;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.FileUtil;
import com.open.finewallpaper.Util.RvDecoration;
import com.open.finewallpaper.Util.RvScrollListener;
import com.open.finewallpaper.Util.ScreenUtil;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

import static android.graphics.Color.TRANSPARENT;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{
    private final static String TAG = "MainActivity";

    private List<String> pictureBeen;
    private MainFragmentAdapter adapter;
    private int lastMotionY;
    private int lastMotionX;

    private Toolbar mToolbar;

    private TranslateAnimation animation;
    private RecyclerView recyclerView;

    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(
         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_current_activtiy);
        initBmob();
        initFile();
        initData();
        initToolbar();
        initView();


    }

    public void initBmob(){
        //第一：默认初始化
        Bmob.initialize(this, "565a11c9e57a1f1a61b20d5fb2d08134");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");
    }

    public void initFile(){
        FileUtil.init();
    }

    public void initData(){
        pictureBeen = new ArrayList<>();

    }


    public void initView(){
        ScreenUtil screenUtil = new ScreenUtil();
        screenUtil.setColor(TRANSPARENT);
        screenUtil.setStatusView(getWindow());


        ListPreloader.PreloadSizeProvider size = new FixedPreloadSizeProvider(imageWidthPixels,imageHeightPixels);


        recyclerView = (RecyclerView) findViewById(R.id.current_rv);

        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addOnScrollListener(new RvScrollListener() {
            @Override
            public void onLoadMore() {
                Glide.with(MainActivity.this).resumeRequests();
            }

            @Override
            public void onDragLoadMore() {
                Glide.with(MainActivity.this).pauseRequests();
            }
        });
        adapter = new MainFragmentAdapter(MainActivity.this,R.layout.adapter_2,pictureBeen);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RvDecoration(this));
        adapter.setOnItemClickListener(new MainFragmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(List<String> url, int position) {
                Intent intent = new Intent(MainActivity.this,NextActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.getBackground().setAlpha(5);
        setSupportActionBar(mToolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    public void startAnimation(int diff){
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,diff,Animation.RELATIVE_TO_PARENT,0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        animation.start();
    }


}
