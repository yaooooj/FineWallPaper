package com.open.finewallpaper.TestFile;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.open.finewallpaper.Bean.HeadingBean;
import com.open.finewallpaper.CustomView.Aaa;
import com.open.finewallpaper.CustomView.MultiCharacterView;
import com.open.finewallpaper.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "SetActivity";
    private MultiCharacterView multiCharacterView;
    private MultiCharacterView multiCharacterView1;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewa);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        Toolbar mToolBar = (Toolbar) findViewById(R.id.viewa_tb);
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initVp();
        initRv();

    }


    private void initVp(){
        List<View> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.heading_item_view,null);
            data.add(view) ;
        }
        Aaa viewPager = (Aaa) findViewById(R.id.heading_vp);
        viewPager.setOffscreenPageLimit(3);
        //viewPager.setPageMargin(110);
        viewPager.setAdapter(new VpAdapter(data));
        viewPager.setPageTransformer(false,new ScaleTransformer());
    }

    public void initRv(){
        list = new ArrayList<>();
        for (int i = 0; i < 5;i++){
            list.add(" this is a recyclerView item " + i);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ts_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new RvAdapter(this,list));
    }

    @Override
    public void onClick(View v) {
        /*
        if (v.getId() == R.id.button){
            Log.e(TAG, "onClick: " );
           multiCharacterView.dismiss();
        }else if (v.getId() == R.id.refresh_bt){
            multiCharacterView.show();
        }
        */
    }

    private static class RvAdapter extends RecyclerView.Adapter{

        private List<String> data;
        private Context context;

        public RvAdapter(Context context,List<String> data) {
            this.data = data;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return  new RvViewHolder(LayoutInflater.from(context).inflate(R.layout.ad_layout,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (holder instanceof RvViewHolder){
                    ((RvViewHolder) holder).textView.setText(data.get(position));
                }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private static class RvViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public RvViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.ad_ts_tv);
        }
    }

    private static class HeadingAdapter extends BaseAdapter{
        List<HeadingBean> mHeadingBeanList;

        public HeadingAdapter(List<HeadingBean> headingBeanList) {
            mHeadingBeanList = headingBeanList;
        }

        @Override
        public int getCount() {
            return mHeadingBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mHeadingBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.heading_item_view,parent,false);
                viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mTextView.setText(mHeadingBeanList.get(position).getText());
            return convertView;
        }

        private class ViewHolder{
            TextView mTextView;

        }
    }

    private static class VpAdapter extends PagerAdapter{
        private List<View> data;

        public VpAdapter(List<View> data) {
            this.data = data;
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
            container.addView(data.get(position));
            return data.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(data.get(position));
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }
    }

    private static class ScaleTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.8f;
        private static final float MIN_ALPHA = 0.5f;
        private int elevationInit = 40;
        @Override
        public void transformPage(View page, float position) {

            CardView cardView = (CardView) page.findViewById(R.id.ad_vp_card);
            if (position > 1) {
                //page.setAlpha(MIN_ALPHA);
                float scale = 1.0f - 0.2f * position;
                float elevation = (1.0f - position ) * elevationInit;
                page.setScaleX(scale);
                page.setScaleY(scale);
                cardView.setCardElevation(elevation);
            }else if (position < -1){
                float scale = 1.0f + 0.2f * position;
                float elevation = (1.0f + position ) * elevationInit;
                page.setScaleX(scale);
                page.setScaleY(scale);
                cardView.setCardElevation(elevation);
            }
            else  { // [-1,1]
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                if (position < 0) {
                    //Log.e(TAG, "(transformPage: position < 0) = " + position );
                    float scaleX = 1.0f + 0.2f * position;
                    float elevation = (1.0f + position ) * elevationInit;
                   // Log.e("google_lenve_fb", "transformPage: scaleX:" + scaleX);
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                    cardView.setCardElevation(elevation);

                } else if (position > 0){
                   // Log.e(TAG, "(transformPage: position > 0) == " + position );
                    float scaleX = 1.0f - 0.2f * position;
                    float elevation = (1.0f - position ) * elevationInit;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                    cardView.setCardElevation(elevation);
                }
                //page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }
    }


}
