package com.open.finewallpaper.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
                        float itemWidth = DisplayUtil.getsWidthPixles(mImageView.getContext()) / 3;
                        params.width = (int) itemWidth;
                        float scale = itemWidth / resource.getIntrinsicWidth();
                        float itemHeight = resource.getIntrinsicHeight() * scale;
                        params.height = (int) itemHeight;
                        mImageView.setLayoutParams(params);
                        return false;
                    }
                })
                .dontAnimate()
                .into(mImageView);
    }
    public static void LoadImage(final Context context, final String url, final ImageView.ScaleType type,
                                 final float ration, final ImageView mImageView){

        float itemWidth = (DisplayUtil.getsWidthPixles(mImageView.getContext()) + 5 *3) / 3;
        //final int width = mImageView.getMeasuredWidth() / 3;
        float itemHeight = itemWidth * ration;
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .dontAnimate()
                .placeholder(R.mipmap.ic_favorite_border_black_24dp)
                .error(R.mipmap.ic_favorite_border_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override((int)itemWidth,(int)itemHeight)
                .centerCrop()
                .into(mImageView);
    }
}
