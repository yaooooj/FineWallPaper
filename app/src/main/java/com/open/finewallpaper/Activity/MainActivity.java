package com.open.finewallpaper.Activity;


import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;


import com.open.finewallpaper.Adapter.MainFragmentAdapter;
import com.open.finewallpaper.Bean.PictureBean;

import com.open.finewallpaper.CoustomView.FreshViewPager;
import com.open.finewallpaper.CoustomView.HeaderView;
import com.open.finewallpaper.CoustomView.OnPullListener;
import com.open.finewallpaper.Fragment.MainFragment;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.FileUtil;
import com.open.finewallpaper.Util.ScreenUtil;
import com.open.finewallpaper.Util.ToastUtil;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.graphics.Color.TRANSPARENT;
import static java.lang.System.in;


public class MainActivity extends AppCompatActivity  implements MainFragment.OnFragmentInteractionListener{
    private final static String TAG = "MainActivity";

    private List<PictureBean> pictureBeen;
    private MainFragmentAdapter adapter;
    private int lastMotionY;
    private int lastMotionX;
    private PopupWindow popUpWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        initBmob();
        initFile();
        initData();
        initView();
        initMenu();

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

        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addQueryKeys("url,picturename");
        bmobQuery.setLimit(12);
        bmobQuery.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(List<PictureBean> list, BmobException e) {
                if (e == null){
                    ToastUtil.show(MainActivity.this,"success");
                    for (PictureBean pictureBean : list){
                        Log.e(TAG, "done: " + pictureBean.getUrl() );
                    }
                    adapter.updataData(list);
                }else {
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }


    public void initView(){
        ScreenUtil screenUtil = new ScreenUtil();
        screenUtil.setColor(TRANSPARENT);
        screenUtil.setStatusView(getWindow());



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fragment_main_recyclerview);
        adapter = new MainFragmentAdapter(this,R.layout.fragment_mian_adapter_m,pictureBeen);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                final int dividerLeft = 10;
                final int dividerRight = 10;
                final int dividerHeight = 10;

                int childPosition = parent.getChildAdapterPosition(view);
                RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager){
                    int spanCount = ((StaggeredGridLayoutManager)layoutManager).getSpanCount();
                    if ((childPosition ) % 5 == 0){
                        outRect.set(0,0,0,dividerHeight);
                    }else if ((childPosition + 2) % spanCount == 0){
                        outRect.set(dividerLeft,0,dividerRight,dividerHeight);
                    }else {
                        outRect.set(dividerLeft,0,0,dividerHeight);
                    }

                }
            }
        });


        FreshViewPager freshViewPager = (FreshViewPager) findViewById(R.id.main_freshviewpager);
        freshViewPager.addHeader(new HeaderView(MainActivity.this));
        freshViewPager.setOnPullListener(new OnPullListener() {
            @Override
            public boolean onRefresh(int diff) {
                //addFragment();
                showPopUpWindow();
                return true;
            }

            @Override
            public boolean onLoadMore() {
                return false;
            }
        });
    }

    public void  initMenu(){

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean intercept = false;
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: " + "Down" );
                lastMotionY = (int) event.getY();
                lastMotionX = (int) event.getX();
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent: " + " Move" );
                int diffY = (y - lastMotionY);
                int diffX = (y - lastMotionX);
                if (diffY > 20 && Math.abs(diffX) < Math.abs(diffY) * 0.5){
                    addFragment();
                    intercept = true;
                }
        }

        return intercept;
    }

    public void addFragment(){
        Log.e(TAG, "addFragment: " );
        MainFragment fragment = MainFragment.newInstance(null,null);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_activity,fragment)
                .show(fragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showPopUpWindow(){
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_layout,null,true);
        popUpWindow = new PopupWindow(view);
        popUpWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popUpWindow.setHeight(300);
        popUpWindow.setOutsideTouchable(true);
        popUpWindow.setBackgroundDrawable(new ColorDrawable(0));

        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popUpWindow.showAtLocation(rootview, Gravity.TOP,0,0);
    }
}
