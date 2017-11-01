package com.open.finewallpaper.SetWallPaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.open.finewallpaper.Util.FileUtil;
import com.open.finewallpaper.Util.GlideApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.FutureTask;

/**
 * Created by yaojian on 2017/11/1.
 */

public class DownloadImage {
    private static final String TAG = "DownloadImage";
    private static File targetDir;
    public static void downloadImage(final String url, final Context context){

        Glide.with(context).downloadOnly().load(url).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                if (resource != null){
                    String name = resource.getAbsolutePath();
                    Log.e(TAG, "onResourceReady: " + name );
                    File file1 = new File(name);
                    File outFile = new File(FileUtil.wallPaperPath,"image1");
                    try {
                        fis = new FileInputStream(file1);
                        int length = fis.available();
                        byte[] bytes = new byte[length];
                        fos = new FileOutputStream(outFile);
                        fos.write(fis.read(bytes));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if (fis != null){
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }
            }
        });
    }

    public static File copyFile(String url, final String name, final Context context){
        final String target = FileUtil.lockWallPaperPath;
        targetDir = new File(target);
        Glide.with(context).downloadOnly().load(url).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                String fromPath = resource.getAbsolutePath();
                Log.e(TAG, "onResourceReady: " + resource.getName() );
                File fileFrom = new File(fromPath);
                if (!fileFrom.exists()){
                    return ;
                }

                if (!targetDir.exists() ){
                    boolean io = targetDir.mkdirs();
                    Log.e(TAG, "onResourceReady: "  + io );
                }

                File[] childFile = targetDir.listFiles();

                for (File aChildFile : childFile) {
                    if (aChildFile.getName().equals(name)) {
                        return;
                    }
                }


                if (copySdCardFile(target + name+".jpg",fromPath) == 0){
                    Log.e(TAG, "onResourceReady: " + "success" );

                }else {
                    Log.e(TAG, "onResourceReady: " + "fail" );
                }
            }
        });

        return targetDir;
    }

    private static int  copySdCardFile(String toFile,String fromFile){
        OutputStream to = null;
        try {
            InputStream from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte bytes[] = new byte[1024];
            int c;
            while ((c = from.read(bytes)) > 0){
                to.write(bytes,0,c);
            }
            from.close();
            to.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return -1;
        }
        return 0;
    }
}
