package com.open.finewallpaper.CustomView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.open.finewallpaper.R;

/**
 * Created by SEELE on 2017/9/26.
 */

public class HeaderView extends FrameLayout implements PullHeader{

    public TextView tvPullDown;
    public HeaderView(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pull_header_layout, this, true);
        tvPullDown = (TextView) findViewById(R.id.tv);
    }

    @Override
    public void onDownBefore(int scrollY) {
        tvPullDown.setText("下拉刷新");
    }

    @Override
    public void onDownAfter(int scrollY) {
        tvPullDown.setText("松开刷新");
    }

    @Override
    public void onRefreshScrolling(int scrollY) {
        tvPullDown.setText("准备刷新");
    }

    @Override
    public void onRefreshDoing(int scrollY) {
        tvPullDown.setText("正在刷新……");
    }

    @Override
    public void onRefreshCompleteScrolling(int scrollY, boolean isRefreshSuccess) {
        tvPullDown.setText(isRefreshSuccess ? "刷新成功" : "刷新失败");
    }

    @Override
    public void onRefreshCancelScrolling(int scrollY) {
        tvPullDown.setText("取消刷新");
    }
}
