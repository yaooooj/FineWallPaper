package com.open.finewallpaper.Presenter;

import com.open.finewallpaper.Bean.FinePic;
import com.open.finewallpaper.Bean.ItemBean;
import com.open.finewallpaper.Bean.SetBean;

import java.util.List;

/**
 * Created by SEELE on 2017/11/12.
 */

public interface OnLoadFinishListener {

    void onFailed(String message,int type);

    void onSuccessRV(List<ItemBean> itemList);

    void onSuccessVP(List<SetBean> itemList);

    void onSuccessAPI(List<ItemBean> itemList);
}
