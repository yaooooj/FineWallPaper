package com.open.finewallpaper.Activity;


import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.Fragment.MainFragment;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.ToastUtil;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{
    private final static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBmob();
        initView();
        MainFragment fragment = MainFragment.newInstance(null,null);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_activity,fragment)
                .commit();
    }
    public void initBmob(){
        //第一：默认初始化
        //Bmob.initialize(this, "565a11c9e57a1f1a61b20d5fb2d08134");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        .setApplicationId("565a11c9e57a1f1a61b20d5fb2d08134")
        ////请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
    }

    public void initView(){
        Button button = (Button) findViewById(R.id.show_me);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                BmobQuery bmobQuery = new BmobQuery("bitmap");
                //bmobQuery.addWhereContainedIn("bitmap", Arrays.asList(name));
                bmobQuery.addQueryKeys("url");
                bmobQuery.setLimit(2);
                bmobQuery.findObjectsByTable(new QueryListener<JSONArray>() {
                    @Override
                    public void done(JSONArray jsonArray, BmobException e) {
                        Log.e(TAG, "done: ");
                        if (e == null){
                            ToastUtil.show(MainActivity.this,"success");
                                Log.e(TAG, "done: " + "  ");
                        }else {
                            Log.e(TAG, "done: " + "bmob失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                */
                BmobQuery<PictureBean> bmobQuery = new BmobQuery<PictureBean>();
                bmobQuery.addQueryKeys("url,picturename,wrapper");
                bmobQuery.setLimit(12);
                bmobQuery.findObjects(new FindListener<PictureBean>() {
                    @Override
                    public void done(List<PictureBean> list, BmobException e) {
                        Log.e(TAG, "done: 0");
                        if (e == null){
                            ToastUtil.show(MainActivity.this,"success");
                            for (PictureBean p : list){
                                Log.e(TAG, "done: " + p.getUrl() +"   "+ p.getPicturename() );
                                downloadFile(p.getBitmap());
                            }
                        }else {
                            Log.e(TAG, "done: " + "bmob失败："+e.getMessage()+","+e.getErrorCode());
                        }

                    }
                });

            }
        });

        Button showPicture = (Button) findViewById(R.id.show_picture);
        showPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void downloadFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        //File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download( new DownloadFileListener() {

            @Override
            public void onStart() {

                ToastUtil.show(MainActivity.this,"开始下载...");
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    ToastUtil.show(MainActivity.this,"下载成功,保存路径:");

                }else{
                    ToastUtil.show(MainActivity.this,"下载失败："+e.getErrorCode()+","+e.getMessage());

                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
