package com.open.finewallpaper.Presenter;

import android.content.Context;

/**
 * Created by SEELE on 2017/11/12.
 */

public interface LoadPresenter {

    void loadBmobToRV(boolean isFresh);

    void loadApiToRV(Context context, int type, int page);

    void onDestroy();
}
