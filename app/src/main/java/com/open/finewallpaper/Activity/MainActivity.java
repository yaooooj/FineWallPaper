package com.open.finewallpaper.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


import com.open.finewallpaper.Adapter.MainFragmentAdapter;

import com.open.finewallpaper.Adapter.ViewPagerAdapter;
import com.open.finewallpaper.Bean.FinePic;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.CoustomView.FreshViewPager;
import com.open.finewallpaper.CoustomView.HeaderView;
import com.open.finewallpaper.CoustomView.OnPullListener;
import com.open.finewallpaper.CoustomView.WrapContentLinearLayoutManager;
import com.open.finewallpaper.Fragment.MainFragment;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.FileUtil;
import com.open.finewallpaper.Util.GlideApp;
import com.open.finewallpaper.Util.RvScrollListener;
import com.open.finewallpaper.Util.ScreenUtil;
import com.open.finewallpaper.Util.SpaceDecoration;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.graphics.Color.TRANSPARENT;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{
    private final static String TAG = "MainActivity";

    private List<String> pictureBeen;
    private ArrayList<SetBean> mPicBeanList;

    private MainFragmentAdapter adapter;
    private int lastMotionY;
    private int lastMotionX;

    private Toolbar mToolbar;
    private AppBarLayout appBarLayout;

    private TranslateAnimation animation;
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(
         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initBmob();
        initFile();
        initData();
        initToolbar();
        initViewPager();
        initView();
        //initFreshView();

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
        mPicBeanList = new ArrayList<>();

        BmobQuery<FinePic> query = new BmobQuery<>();
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.addQueryKeys("pic_name,pic_url");
        query.order("-createdAt");
        query.findObjects(new FindListener<FinePic>() {
            @Override
            public void done(List<FinePic> list, BmobException e) {
                if (e == null){
                    SetBean finePic;
                    for (int i =0 ;i < list.size();i++){
                        finePic = new SetBean();
                        finePic.setName(list.get(i).getPic_name());
                        finePic.setUrl(list.get(i).getPic_url());
                        mPicBeanList.add(finePic);
                    }
                    ViewPagerAdapter adapter = new ViewPagerAdapter(mPicBeanList,0,MainActivity.this);
                    viewPager.setAdapter(adapter);
                    adapter.setListener(new ViewPagerAdapter.OnViewPagerItemListener() {
                        @Override
                        public void click(int position) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("url",mPicBeanList);
                            bundle.putInt("position",position);
                            Intent in = new Intent(MainActivity.this,NextActivity.class);
                            in.putExtra("urls",bundle);
                            startActivity(in);
                        }
                    });
                }else {
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void initViewPager(){

        viewPager = (ViewPager) findViewById(R.id.main_vp);

    }

    public void initView(){


        recyclerView = (RecyclerView) findViewById(R.id.current_rv);

        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
       // recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.addOnScrollListener(new RvScrollListener(appBarLayout) {
            @Override
            public void onLoadMore() {
                GlideApp.with(MainActivity.this).resumeRequests();
            }

            @Override
            public void onDragLoadMore() {
                Log.e(TAG, "onDragLoadMore: " + "dragMore" );
               GlideApp.with(MainActivity.this).pauseRequests();
            }
        });
        adapter = new MainFragmentAdapter(MainActivity.this,R.layout.adapter_2,pictureBeen);
       // recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new SpaceDecoration(SpaceDecoration.VERTICAL_LIST));
        adapter.setOnItemClickListener(new MainFragmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(List url, String type) {

                Bundle bundle = new Bundle();
                bundle.putString("type",type);
                Intent intent = new Intent(MainActivity.this,ShowPictureActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

    }

    public void initToolbar(){
        ScreenUtil screenUtil = new ScreenUtil();
        screenUtil.setColor(TRANSPARENT);
        screenUtil.setStatusView(getWindow());


        appBarLayout = (AppBarLayout) findViewById(R.id.main_abl);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mToolbar.setBackgroundColor(changeAlpha(Color.GRAY,
                        Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.getBackground().setAlpha(5);
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.ic_menu_white_36dp));
        setSupportActionBar(mToolbar);

    }

    public void initFreshView(){
        FreshViewPager refreshLayout = (FreshViewPager) findViewById(R.id.main_fresh);
        refreshLayout.addHeader(new HeaderView(this));
        refreshLayout.setOnPullListener(new OnPullListener() {
            @Override
            public boolean onRefresh(int diff) {
                BmobQuery<PictureBean> query = new BmobQuery<>();
                boolean isCache = query.hasCachedResult(PictureBean.class);
                if (isCache){
                    query.clearCachedResult(PictureBean.class);
                }else {
                    query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
                    query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    query.addQueryKeys("type");
                    query.order("type");
                }
                query.findObjects(new FindListener<PictureBean>() {
                    @Override
                    public void done(List<PictureBean> list, BmobException e) {
                        if (e == null){

                            adapter.updataData(true);
                        }else {
                            Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                return true;
            }

            @Override
            public boolean onLoadMore() {
                return false;
            }

            @Override
            public void onMoveLoad(int dx) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nvg:
                Intent intent = new Intent(MainActivity.this,SetActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
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


    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction *0.7);
        return Color.argb(alpha, red, green, blue);
    }

}
