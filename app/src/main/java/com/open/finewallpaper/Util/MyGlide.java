package com.open.finewallpaper.Util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;

/**
 * Created by SEELE on 2017/10/15.
 */
@GlideModule
public class MyGlide extends AppGlideModule{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        int size = 1024 * 1024 * 50;
        builder.setDiskCache(new DiskLruCacheFactory(FileUtil.cachePath,"Cache",size));

    }
}
