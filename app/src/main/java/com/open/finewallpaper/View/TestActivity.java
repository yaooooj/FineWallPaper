package com.open.finewallpaper.View;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.CuVp;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.open.finewallpaper.Bean.HeadingBean;
import com.open.finewallpaper.CoustomView.Aaa;
import com.open.finewallpaper.CoustomView.MultiCharacterView;
import com.open.finewallpaper.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "SetActivity";
    private MultiCharacterView multiCharacterView;
    private MultiCharacterView multiCharacterView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewa);

        /*
        multiCharacterView = (MultiCharacterView) findViewById(R.id.multi_view);
        multiCharacterView.setText("正在加载");

        multiCharacterView1 = (MultiCharacterView) findViewById(R.id.load_more);
        multiCharacterView1.setText("Refresh", MultiCharacterView.Type.EN);
        multiCharacterView1.setOnFreshListener(new MultiCharacterView.OnFreshListener() {
            @Override
            public void onFresh() {
                //Toast.makeText(TestActivity.this,"refresh complete",Toast.LENGTH_SHORT).show();
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        Button button1 = (Button) findViewById(R.id.refresh_bt);
        button1.setOnClickListener(this);


        List<HeadingBean> data = new ArrayList<>();
        HeadingBean headingBean;
        for (int i = 0; i < 4; i++) {
            headingBean = new HeadingBean();
            headingBean.setText("hah" + i);
            data.add(headingBean);
        }

        HeadingView headingView = (HeadingView) findViewById(R.id.heading);
        headingView.setAdapter(new HeadingAdapter(data));
        */

        initVp();

    }


    private void initVp(){
        List<View> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.heading_item_view,null);
            data.add(view) ;
        }

        Aaa viewPager = (Aaa) findViewById(R.id.heading_vp);
        viewPager.setPageTransformer(false,new ScaleTransformer());
        viewPager.setOffscreenPageLimit(3);
        //viewPager.setPageMargin(20);
        viewPager.setAdapter(new VpAdapter(data));


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
        private static final float MIN_SCALE = 0.80f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(View page, float position) {
            if (position < -1 || position > 1) {
                //page.setAlpha(MIN_ALPHA);
                //Log.e("google_lenve_fb", "transformPage: scaleX:"+ position);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            } else  { // [-1,1]
                //Log.e("google_lenve_fb", "transformPage: scaleX:");
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                if (position <= 0) {
                    float scaleX = 0.8f + 0.3f * position;
                    Log.e("google_lenve_fb", "transformPage: scaleX:" + scaleX);
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);

                } else if (position > 0){
                    float scaleX = 0.8f - 0.3f * position;
                    //Log.d("google_lenve_fb", "transformPage: scaleX:" + scaleX);
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                }
                //page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }
    }


}
