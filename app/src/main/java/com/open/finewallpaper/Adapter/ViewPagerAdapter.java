package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.Util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/10/31.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    private List<SetBean> data = new ArrayList<>();
    private Context mContext;
    private List<ImageView> mImageViews;
    private int site;
    private OnViewPagerItemListener mListener;

    public ViewPagerAdapter(List<SetBean> data, int position, Context context) {
        this.data = data;
        mContext = context;
        site = position;
        if (mImageViews == null){
            mImageViews = new ArrayList<>();
        }

        for (int i = 0; i < data.size(); i++) {
            mImageViews.add(new ImageView(mContext));
        }
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
    public Object instantiateItem(ViewGroup container, final int position) {
       // ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
       //         ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT
       // );
       // params.width = DisplayUtil.getsWidthPixles(mContext);
        if (data.size() == 0) {
            return null;
        }
        ImageView imageView = mImageViews.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null){
                    mListener.click(position);
                }

            }
        });
       // imageView.setLayoutParams(params);

        GlideUtil.LoadImageToView(mContext,data.get(position).getUrl(), ImageView.ScaleType.CENTER_CROP, (float) 0.8,imageView);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(mImageViews.get(position));
    }

    public void setListener(OnViewPagerItemListener listener) {
        mListener = listener;
    }

    public interface OnViewPagerItemListener{
        void click(int position);
    }
}
