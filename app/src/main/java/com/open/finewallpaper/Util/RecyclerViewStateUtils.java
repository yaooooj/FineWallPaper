package com.open.finewallpaper.Util;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.open.finewallpaper.Adapter.MainFragmentAdapter;
import com.open.finewallpaper.CoustomView.LoadingFooter;

/**
 * Created by SEELE on 2017/11/15.
 */

public class RecyclerViewStateUtils {
    private final static String TAG = "RecyclerViewStateUtils";
    public static void setFooterViewState(Activity instance, RecyclerView recyclerView, int pageItemSize, LoadingFooter.State state, View.OnClickListener errorListener) {

        if(instance==null || instance.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();

        if (outerAdapter == null || !(outerAdapter instanceof MainFragmentAdapter)) {
            return;
        }

        MainFragmentAdapter adapter = (MainFragmentAdapter) outerAdapter;

        //只有一页的时候，就别加什么FooterView了
        if (adapter.getItemCount() < pageItemSize) {
            return;
        }

        LoadingFooter footerView;

        //已经有footerView了
        if (adapter.getFooterViewSize() > 0) {
            Log.e(TAG, "setFooterViewState: " + "set footer view" );
            footerView = (LoadingFooter) adapter.getFooterView();
            footerView.setState(state);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        } else {
            Log.e(TAG, "setFooterViewState: "  + " init footer view " );
            footerView = new LoadingFooter(instance);
            footerView.setState(state);

            if (state == LoadingFooter.State.NetWorkError) {
                footerView.setOnClickListener(errorListener);
            }

            adapter.addFooterView(footerView);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    /**
     * 获取当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     */
    public static LoadingFooter.State getFooterViewState(RecyclerView recyclerView) {

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof MainFragmentAdapter) {
            if (((MainFragmentAdapter) outerAdapter).getFooterViewSize() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((MainFragmentAdapter) outerAdapter).getFooterView();
                return footerView.getState();
            }
        }

        return LoadingFooter.State.Normal;
    }

    /**
     * 设置当前RecyclerView.FooterView的状态
     *
     * @param recyclerView
     * @param state
     */
    public static void setFooterViewState(RecyclerView recyclerView, LoadingFooter.State state) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof MainFragmentAdapter) {
            if (((MainFragmentAdapter) outerAdapter).getFooterViewSize() > 0) {
                LoadingFooter footerView = (LoadingFooter) ((MainFragmentAdapter) outerAdapter).getFooterView();
                footerView.setState(state);
            }
        }
    }

}
