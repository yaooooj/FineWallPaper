package com.open.finewallpaper.CustomView;

/**
 * Created by SEELE on 2017/9/26.
 */

public interface OnPullListener {

    boolean onRefresh(int diff);

    boolean onLoadMore();

    void    onMoveLoad(int dx);
}
