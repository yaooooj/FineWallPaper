package com.open.finewallpaper.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.open.finewallpaper.Adapter.CurrentAdapter;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.Fragment.Fragment1;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Window.VerticalDrawerLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CurrentActivity extends AppCompatActivity {
    private final static String TAG = "CurrentActivity";

    private VerticalDrawerLayout  mVerticalDrawerLayout;
    private RecyclerView recyclerView;

    private List<PictureBean> data;
    private CurrentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_activtiy);


        init();

        recyclerView = (RecyclerView) findViewById(R.id.current_rv);

       // recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        //adapter = new CurrentAdapter(CurrentActivity.this,data);
        //recyclerView.setAdapter(adapter);





    }

    private void init(){
        data = new ArrayList<>();

        /*
        PictureBean pictureBean;
        for (int i =0; i < 10;i ++){
            pictureBean = new PictureBean();
            pictureBean.setUrl("http://bmob-cdn-14274.b0.upaiyun.com/2017/09/25/2011054c40e5900d80e1705e92975c56.jpg");
            data.add(pictureBean);

        }
        */


        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addQueryKeys("url,picturename");
        bmobQuery.setLimit(12);
        bmobQuery.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(List<PictureBean> list, BmobException e) {

                if (e == null){
                    //data= list;
                    //adapter.notifyDataSetChanged();
                    adapter.updataData(list);

                    for (int i =0;i < list.size();i++){
                        Log.e(TAG, "initData: "  + list.get(i).getUrl());
                    }
                }else {
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    }





}
