package com.open.finewallpaper.Util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by yaojian on 2017/10/16.
 */

public final class FileUtil {
    private final static String TAG = "FileUtil";
    private final static String FINEWALLPAPER = "FineWallPaper";
    private final static String CACHE = "Cache";
    private final static String LOCKWALLPAPER = "LockWallPaper";
    private final static String WAlLPAPER = "WallPaper";
    private final static String LOG = "Log";
    //String Dir
    public static String fineWallPaperPath =
            Environment.getExternalStorageDirectory() + File.separator + FINEWALLPAPER + File.separator;
    public static String cachePath = fineWallPaperPath + CACHE;
    public static String wallPaperPath = fineWallPaperPath + WAlLPAPER;
    public static String lockWallPaperPath = fineWallPaperPath + LOCKWALLPAPER;
    public static String logPath = fineWallPaperPath + LOG;

    //File Path
    public static File  cacheDir = new File(cachePath);
    public static File wallPaperDir = new File(wallPaperPath);
    public static File lockWallPaperDir = new File(lockWallPaperPath);
    public static File logDir = new File(logPath);


    public  static synchronized void init(){
        FileUtil fileUtil = new FileUtil();
        fileUtil.createPublicStorageDir("FineWallPaper");
        fileUtil.createExternalStorageDir(fineWallPaperPath);
        fileUtil.createExternalStorageDir(cachePath);
        fileUtil.createExternalStorageDir(wallPaperPath);
        fileUtil.createExternalStorageDir(lockWallPaperPath);
        fileUtil.createExternalStorageDir(logPath);
    }

    private File createPublicStorageDir(String fileName){
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName);
        if (!file.exists()){
            if (!file.mkdirs()){
                Log.e(TAG, "createPublicStorageDir: " +"Directory not create" );
            }else {
                Log.e(TAG, "createPublicStorageDir: " +"create success" );
            }

        }
        return file;
    }

    public File getExternalStorageDir(String fileName){
        return new File(fileName);
    }

    private File createExternalStorageDir(String dir){

        File file = getExternalStorageDir(dir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(TAG, "createPublicStorageDir: " + "Directory not create");
            }
        }
        return file;
    }

    public boolean deleteFile(File file){
        if (file != null){
            file.delete();
        }
        return false;
    }

    public boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
