package com.open.finewallpaper;


import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.open.finewallpaper.CoustomView.CustomDialog;
import com.open.finewallpaper.Util.FileUtil;

import cn.bmob.v3.Bmob;


public abstract class BaseActivity extends AppCompatActivity {
    private boolean isShowToolbar;
    private boolean mAllowFullScreen = true;
    private boolean isSetStatusBar = true;
    private boolean isAllowScreenRoate;

    private CustomDialog mCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mAllowFullScreen){
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        mCustomDialog = new CustomDialog(this,R.style.progress_dialog_loading,"拼命加载中。。。");
        initBmob();
        initFile();




        if (isSetStatusBar){
            steepStatusBar();
        }

        /*
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        */

        initView();

        initEvent();

    }

    public abstract void initView();


    public abstract void initEvent();

    public void initBmob(){
        //第一：默认初始化
        Bmob.initialize(this, "565a11c9e57a1f1a61b20d5fb2d08134");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");
    }

    public void initFile(){
        FileUtil.init();
    }



    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void showDialog(){
        mCustomDialog.show();
    }

    public void hideDialog(){
        if (mCustomDialog != null && mCustomDialog.isShowing())
        mCustomDialog.hide();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit();
    }

    private long lastClickTime;

    /**
     * 实现连续点击两次才退出应用程序
     */
    public void exit() {
        if ((System.currentTimeMillis() - lastClickTime) > 2000) {
            //showToast("再按一次退出" + context.getResources().getString(R.string.app_name));
            lastClickTime = System.currentTimeMillis();
        } else {
            //ActivityCollector.removeAll();
            System.exit(0);
        }
    }


    /**
     * Toast工具类
     */
    public Toast toast;
    public void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public void showToast(BaseActivity activity, String text) {
        if (toast == null) {
            toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public boolean isShowToolbar() {
        return isShowToolbar;
    }

    public void setShowToolbar(boolean showToolbar) {
        isShowToolbar = showToolbar;
    }

    public boolean isAllowFullScreen() {
        return mAllowFullScreen;
    }

    public void setAllowFullScreen(boolean allowFullScreen) {
        mAllowFullScreen = allowFullScreen;
    }

    public boolean isSetStatusBar() {
        return isSetStatusBar;
    }

    public void setSetStatusBar(boolean setStatusBar) {
        isSetStatusBar = setStatusBar;
    }

    public boolean isAllowScreenRoate() {
        return isAllowScreenRoate;
    }

    public void setAllowScreenRoate(boolean allowScreenRoate) {
        isAllowScreenRoate = allowScreenRoate;
    }

}




