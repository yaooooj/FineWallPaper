package com.open.finewallpaper.View;

import com.open.finewallpaper.Bean.FinePic;
import com.open.finewallpaper.Bean.ItemBean;
import com.open.finewallpaper.Bean.SetBean;

import java.util.List;

/**
 * Created by SEELE on 2017/11/12.
 */

public interface ActivityView {

    void isShowProgress(boolean isShow);

    void showDataRV(List<ItemBean> itemList);

    void showDataVP(List<SetBean> itemList);

    void isShowError(boolean isShow);

    void isShowFooterError(boolean isShow);

    void onRefresh();


}
