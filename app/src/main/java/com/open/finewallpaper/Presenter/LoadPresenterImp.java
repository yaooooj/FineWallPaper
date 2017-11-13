package com.open.finewallpaper.Presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.open.finewallpaper.Bean.FinePic;
import com.open.finewallpaper.Bean.ItemBean;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.Model.LoadUrlModel;
import com.open.finewallpaper.Model.LoadUrlModelImp;
import com.open.finewallpaper.View.ActivityView;

import java.util.List;

/**
 * Created by SEELE on 2017/11/12.
 */

public class LoadPresenterImp implements LoadPresenter,OnLoadFinishListener {
    private final static String TAG = "LoadPresenterImp";

    private ActivityView mActivityView;
    private LoadUrlModel mLoadUrlModel;

    public LoadPresenterImp(ActivityView activityView) {
        mActivityView = activityView;
        mLoadUrlModel = new LoadUrlModelImp();
    }


    @Override
    public void loadBmobToRV(boolean isFresh) {
        if (mActivityView != null){
            mActivityView.isShowProgress(true);
        }

        //对recyclerView加载数据
        mLoadUrlModel.loadUrlForVP(isFresh,this);
        //对首页展示banner进行加载数据
        mLoadUrlModel.loadUrlForRV(isFresh,this);

    }

    @Override
    public void loadApiToRV(Context context, int type, int page) {
        Log.e(TAG, "loadApiToRV: " + "go to model" );
        mLoadUrlModel.loadUrlFromAPI(context,type,page,this);
    }

    @Override
    public void onDestroy() {
        if (mActivityView != null){
            mActivityView = null;
        }
    }


    @Override
    public void onFailed(String message,int type) {
        switch (type){
            case 1:

                mActivityView.isShowProgress(false);
                mActivityView.isShowError(true);
                break;
            case 2:
                mActivityView.isShowProgress(false);
                mActivityView.isShowError(true);
                break;
            case 3:
                if (message.equals("null")){
                    //显示没有更多的数据
                    mActivityView.isShowFooterError(false);
                }else {
                    //加载数据出错
                    mActivityView.isShowFooterError(true);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void  onSuccessRV(List<ItemBean> itemList) {
        mActivityView.isShowProgress(false);
        mActivityView.showDataRV(itemList);
    }

    @Override
    public void onSuccessVP(List<SetBean> itemList) {
        mActivityView.isShowProgress(false);
        mActivityView.showDataVP(itemList);
    }

    @Override
    public void onSuccessAPI(List<ItemBean> itemList) {
        Log.e(TAG, "onSuccessAPI: " + "to activity" );
        mActivityView.showDataRV(itemList);
    }


}
