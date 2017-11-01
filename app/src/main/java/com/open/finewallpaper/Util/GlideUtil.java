package com.open.finewallpaper.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.open.finewallpaper.Adapter.CurrentAdapter;
import com.open.finewallpaper.R;

/**
 * Created by yaojian on 2017/11/1.
 */

public class GlideUtil {

    public static void  LoadImageToView(Context context, String url, final ImageView.ScaleType type, final float ration, final ImageView mImageView){
        GlideApp.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_favorite_border_black_24dp)
                .error(R.mipmap.ic_favorite_border_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (mImageView == null){
                            return false;
                        }

                        if (mImageView.getScaleType() != ImageView.ScaleType.FIT_XY){
                            mImageView.setScaleType(type);
                        }

                        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
                        int width = mImageView.getWidth() - mImageView.getPaddingLeft() - mImageView.getPaddingRight();
                        float scale = width / resource.getIntrinsicWidth();
                        int height =  Math.round((resource.getIntrinsicHeight() * scale) * ration);
                        params.height = height + mImageView.getPaddingTop() + mImageView.getPaddingBottom();
                        mImageView.setLayoutParams(params);
                        return false;
                    }
                })
                .dontAnimate()
                .into(mImageView);
    }
}
