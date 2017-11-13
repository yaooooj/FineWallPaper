package com.open.finewallpaper.Model;

import android.content.Context;
import android.util.Log;

import com.open.finewallpaper.Bean.APIBean.PictureBeanAPI;
import com.open.finewallpaper.Bean.FinePic;
import com.open.finewallpaper.Bean.ImageBean;
import com.open.finewallpaper.Bean.ItemBean;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.HTTP.OkHttpUtil;
import com.open.finewallpaper.HTTP.PhraseUrl;
import com.open.finewallpaper.HTTP.Urls;
import com.open.finewallpaper.Presenter.OnLoadFinishListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by SEELE on 2017/11/12.
 */

public class LoadUrlModelImp implements LoadUrlModel{
    private final static String TAG = "LoadUrlModelImp";
    private int maxCount = 0;
    private List<ItemBean> itemList ;
    private List<SetBean> mFinePics;
    private List<ItemBean> apiItemList;



    @Override
    public void loadUrlForRV(boolean fresh, final OnLoadFinishListener onLoadFinishListener) {
        itemList = new ArrayList<>();
        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        bmobQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        bmobQuery.addQueryKeys("type,url,name,order");
        bmobQuery.order("order");
        boolean isCache = bmobQuery.hasCachedResult(PictureBean.class);
        if (fresh){
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }else {
            if (isCache){
                //必须从缓存中获取数据
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            }else {
                //必须从网络获取数据
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            }
        }
        bmobQuery.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(final List<PictureBean> list, BmobException e) {
                if (e == null){
                    //对返回的数据进行分组，按照类型分类，整合到一个list中
                    sortData2(list);
                    onLoadFinishListener.onSuccessRV(itemList);
                }else {

                    onLoadFinishListener.onFailed("done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode(),1);
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }

    @Override
    public void loadUrlForVP(boolean fresh, final OnLoadFinishListener onLoadFinishListener) {
        mFinePics = new ArrayList<>();
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
                    //对数据进行整理，按照类型整理
                    for (int i =0 ;i < list.size();i++){
                        finePic = new SetBean();
                        finePic.setName(list.get(i).getPic_name());
                        finePic.setUrl(list.get(i).getPic_url());
                        mFinePics.add(finePic);
                    }
                    onLoadFinishListener.onSuccessVP(mFinePics);

                }else {
                    onLoadFinishListener.onFailed("done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode(),2);
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }





    @Override
    public void loadUrlFromAPI(Context context, int type, int page,final OnLoadFinishListener onLoadFinishListener) {
        apiItemList = new ArrayList<>();
        OkHttpUtil.ResultCallback<PictureBeanAPI> resultCallback = new OkHttpUtil.ResultCallback<PictureBeanAPI>() {
            @Override
            public void onSuccess(PictureBeanAPI response) {
                //Log.e(TAG, "onSuccess: " + "success" );
                //API获取的数据和Bmob获取的数据不一样，手动进行转换
                factoryItemBean(PhraseUrl.phraseUrl(response));
                onLoadFinishListener.onSuccessAPI(apiItemList);

                if (response == null){
                    onLoadFinishListener.onFailed("null",3);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "onFailure: " + e );
                onLoadFinishListener.onFailed(e.getMessage(),3);
            }
        };

        OkHttpUtil.get(context, Urls.Urls(type,page),resultCallback);
    }




    private void sortData2(List<PictureBean> list){
        ItemBean itemBean;
        ImageBean imgBean;
        for (int i =0 ;i < list.size();i++) {
            if (isDiffType(list,i)){
                itemBean = new ItemBean();
                imgBean = new ImageBean();
                if (maxCount > 9 || i==0){
                    itemBean.setMore(true);
                }else {
                    itemBean.setMore(false);
                }
                itemBean.setImgType(list.get(i).getType());
                itemBean.setImgBean(imgBean);
                itemList.add(itemBean);
                maxCount = 0;
            }
            maxCount++;
            imgBean = new ImageBean();
            imgBean.setImgName(list.get(i).getPicturename());
            imgBean.setImgUrl(list.get(i).getUrl());
            itemBean = new ItemBean();
            itemBean.setImgBean(imgBean);
            itemBean.setImgType(list.get(i).getType());
            itemList.add(itemBean);
        }

    }


    private boolean isDiffType(List<PictureBean> list,int pos){
        if (pos == 0){
            return true;
        }else {
            String preGroupId = list.get(pos-1).getType();
            String groupId = list.get(pos).getType();
            return !preGroupId .equals(groupId);
        }

    }

    private void factoryItemBean(List<String> list){
        ItemBean itemBean;
        ImageBean imgBean;
        for (int i = 0; i < list.size();i++){
            imgBean = new ImageBean();
            imgBean.setImgName("api_pic" + i);
            imgBean.setImgUrl(list.get(i));
            itemBean = new ItemBean();
            itemBean.setImgBean(imgBean);
            itemBean.setImgType("api_pic");
            itemBean.setMore(false);
            apiItemList.add(itemBean);
        }
    }


}
