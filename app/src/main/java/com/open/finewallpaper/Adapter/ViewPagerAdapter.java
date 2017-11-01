package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.Util.DisplayUtil;
import com.open.finewallpaper.Util.GlideApp;
import com.open.finewallpaper.Util.GlideUtil;
import com.open.finewallpaper.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/10/31.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    private List<String> data = new ArrayList<>();
    private Context mContext;
    private List<ImageView> mImageViews;
    private int site;

    public ViewPagerAdapter(List<String> data,int position, Context context) {
        this.data = data;
        mContext = context;
        site = position;
        if (mImageViews == null){
            mImageViews = new ArrayList<>();
        }

        for (int i = 0; i < data.size(); i++) {
            mImageViews.add(new ImageView(mContext));
        }
    }


    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {


        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       // ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
       //         ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT
       // );
       // params.width = DisplayUtil.getsWidthPixles(mContext);
        if (data.size() == 0) {
            return null;
        }
        ImageView imageView = mImageViews.get(position);
       // imageView.setLayoutParams(params);

        GlideUtil.LoadImageToView(mContext,data.get(position), ImageView.ScaleType.CENTER_CROP, (float) 0.8,imageView);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(mImageViews.get(position));
    }
}
