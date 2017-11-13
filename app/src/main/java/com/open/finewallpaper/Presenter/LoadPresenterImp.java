package com.open.finewallpaper.Presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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
        mLoadUrlModel.loadUrlForVP(isFresh,this);
        mLoadUrlModel.loadUrlForRV(isFresh,this);

    }

    @Override
    public void loadApiToRV(Context context, int type, int page) {
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
        mActivityView.isShowProgress(false);
        mActivityView.showDataRV(itemList);
    }


}
