package com.open.finewallpaper.Model;

import android.content.Context;

import com.open.finewallpaper.HTTP.LoadSuccess;
import com.open.finewallpaper.Presenter.OnLoadFinishListener;

/**
 * Created by SEELE on 2017/11/12.
 */

public interface LoadUrlModel {

    void loadUrlForRV(boolean fresh, OnLoadFinishListener onLoadFinishListener);

    void loadUrlForVP(boolean fresh, OnLoadFinishListener onLoadFinishListener);

    void loadUrlFromAPI(Context context, int type, int page);
}
