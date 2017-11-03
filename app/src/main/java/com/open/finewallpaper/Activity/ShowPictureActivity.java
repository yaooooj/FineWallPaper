package com.open.finewallpaper.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.finewallpaper.Adapter.ShowAdapter;
import com.open.finewallpaper.Fragment.Fragment1;
import com.open.finewallpaper.Fragment.Fragment2;
import com.open.finewallpaper.R;

import java.util.ArrayList;
import java.util.List;

public class ShowPictureActivity extends AppCompatActivity {
    private final static String TAG = "ShowPictureActivity";
    private List<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        //setNeedGesture(true);
        init();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.show_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new ShowAdapter(data,this));

        ViewPager viewPager = (ViewPager) findViewById(R.id.show_vp);
        viewPager.setAdapter(new MyViewPagerAdapter(this,data));

    }

    public void init(){
        data = new ArrayList<>();
        for (int i = 0;i < 30;i++){
            data.add(" find toolbar + " + i);
        }
    }


    private static class MyViewPagerAdapter extends PagerAdapter{
        Context context;
        List<String> data;
        List<TextView> textViews;

        public MyViewPagerAdapter(Context context,List<String> data) {
            this.context = context;
            this.data = data;
            textViews = new ArrayList<>();

            for (int i =0;i < data.size();i++){
                textViews.add(new TextView(context));
            }

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
             ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                     ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            TextView textView = textViews.get(position);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setText(data.get(position));
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(textViews.get(position));

        }
    }




}
