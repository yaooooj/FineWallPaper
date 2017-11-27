package com.open.finewallpaper.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.open.finewallpaper.Adapter.MainFragmentAdapter;
import com.open.finewallpaper.Adapter.ViewPagerAdapter;
import com.open.finewallpaper.BaseActivity;
import com.open.finewallpaper.Bean.ItemBean;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.CoustomView.LoadingFooter;
import com.open.finewallpaper.CoustomView.MyCustom;
import com.open.finewallpaper.HTTP.NetWorkUtils;
import com.open.finewallpaper.Presenter.LoadPresenterImp;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.GlideApp;
import com.open.finewallpaper.Util.RecyclerViewStateUtils;
import com.open.finewallpaper.Util.RvScrollListener;
import com.open.finewallpaper.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.TRANSPARENT;

public class MainActivity extends BaseActivity implements ActivityView  {
    private final static String TAG = "MainActivity";
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private Toolbar mToolbar;
    private AppBarLayout appBarLayout;

    private MainFragmentAdapter adapter;
    private List<ItemBean> itemList;

    private Dialog mCustomDialog;
    private LoadPresenterImp mLoadPresenterImp;

    private boolean isFresh = false;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAllowFullScreen(true);
        setContentView(R.layout.activity_main);
        setAllowScreenRoate(false);
        mCustomDialog = MyCustom.createLoadingDialog(this,"拼命加载中。。。");
        initFreshView();
        initToolbar();
        initViewPager();
        initRecyclerView();
        mLoadPresenterImp = new LoadPresenterImp(this);
    }

    @Override
    protected void onDestroy() {
        if (mCustomDialog != null){
            mCustomDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void isShowProgress(boolean isShow) {
        if (isShow){
            mCustomDialog.show();
        }else {
            mCustomDialog.dismiss();
            if (refreshLayout.isRefreshing()){
                refreshLayout.setRefreshing(false);
            }

        }
    }

    @Override
    public  void showDataRV(List<ItemBean> itemList) {
        if (page > 1){
            //这里只是临时展示数据,不需要在加载更多数据
            RecyclerViewStateUtils.setFooterViewState(MainActivity.this, recyclerView,30, LoadingFooter.State.TheEnd, null);
        }
        //更新RecyclerView中的数据
        adapter.upData(itemList,isFresh);
        isFresh = false;
    }

    @Override
    public  void showDataVP(final List<SetBean> finPics) {
        //更新ViewPager中的数据
        ViewPagerAdapter adapter = new ViewPagerAdapter(finPics,0,MainActivity.this);
        viewPager.setAdapter(adapter);
        adapter.setListener(new ViewPagerAdapter.OnViewPagerItemListener() {
            @Override
            public void click(int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("url", (ArrayList<? extends Parcelable>) finPics);
                bundle.putInt("position",position);
                Intent in = new Intent(MainActivity.this,NextActivity.class);
                in.putExtra("urls",bundle);
                startActivity(in);
            }
        });
    }


    @Override
    public void isShowError(boolean isShow) {
        adapter.showError();
    }

    @Override
    public void onRefresh() {
        mLoadPresenterImp.loadBmobToRV(false);
    }


    public void isShowFooterError(boolean isShow){
        if (isShow){
            //没有更多数据
            RecyclerViewStateUtils.setFooterViewState(recyclerView,LoadingFooter.State.TheEnd);
        }else {
            //网络出错或其他原因
            RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.NetWorkError);
        }

    }

    public void initFreshView(){
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_fresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFresh = true;
                mLoadPresenterImp.loadBmobToRV(true);
            }
        });


    }

    public void initToolbar(){
        ScreenUtil screenUtil = new ScreenUtil();
        screenUtil.setColor(TRANSPARENT);
        screenUtil.setStatusView(getWindow());


        appBarLayout = (AppBarLayout) findViewById(R.id.main_abl);
        //设置AppBarLayout透明度渐变
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mToolbar.setBackgroundColor(changeAlpha(Color.GRAY,
                        Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.getBackground().setAlpha(5);
        //改变Menu菜单的图标，使用自定义的图标代替
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.ic_menu_white_36dp));
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        setSupportActionBar(mToolbar);

    }

    public void initViewPager(){

        viewPager = (ViewPager) findViewById(R.id.main_vp);

    }

    public void initRecyclerView(){

        mLoadPresenterImp = new LoadPresenterImp(this);
        itemList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.current_rv);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,3);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (!TextUtils.isEmpty(itemList.get(position).getImgType())){
                    return 3;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RvScrollListener(appBarLayout) {
            @Override
            public void onLoadMore(View view) {

                if (page <= 1){
                    //显示一页后就不在显示了，数据太多，只做展示用
                    Log.e(TAG, "onLoadMore: " + " first load more" );
                    RecyclerViewStateUtils.setFooterViewState(MainActivity.this, recyclerView,30, LoadingFooter.State.Loading, null);
                    mLoadPresenterImp.loadApiToRV(MainActivity.this,4001, page);
                    page++;
                }
            }

            @Override
            public void onDragLoadMore() {

                GlideApp.with(MainActivity.this).pauseRequests();
            }
        });
        adapter = new MainFragmentAdapter(this,R.layout.adapter_2,itemList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MainFragmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(ArrayList<SetBean> url, int position) {
                if (url !=null){
                    Log.e(TAG, "onClick: " + "url not null" );
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("url",url);
                bundle.putInt("position",position);

                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                intent.putExtra("urls",bundle);
                startActivity(intent);
            }
        });
        adapter.setOnTextViewClickListener(new MainFragmentAdapter.OnTextViewClickListener() {
            @Override
            public void onClick(List<String> url, String type) {
                Bundle bundle = new Bundle();
                bundle.putString("type",type);
                Intent intent = new Intent(MainActivity.this,ShowPictureActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        //获取数据填充
        onRefresh();

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
                //Intent intent = new Intent(MainActivity.this,SetActivity.class);
                Intent intent = new Intent(MainActivity.this,TestActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }


    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction *0.7);
        return Color.argb(alpha, red, green, blue);
    }

}
