package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.open.finewallpaper.Activity.MainActivity;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/10/15.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter
        implements View.OnClickListener, View.OnLongClickListener{
    private final static String TAG = "MainFragmentAdapter";
    private List<PictureBean> data;
    private Context mContext;
    private Fragment mFragment;
    private int layoutResId;
    private LayoutInflater inflate;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public MainFragmentAdapter(Context context, int layoutResId, List<PictureBean> data) {
        this.data = data;
        this.mContext = context;
        this.layoutResId = layoutResId;
        inflate = LayoutInflater.from(context);

    }

    public MainFragmentAdapter(Fragment fragment, int layoutResId, List<PictureBean> data) {
        this.data = data;
        this.mFragment = fragment;
        this.layoutResId = layoutResId;
        inflate = LayoutInflater.from(fragment.getContext());
    }

    public  void  updataData (List<PictureBean> data ){
        this.data =  data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 1){
            viewHolder = new ViewPagerHolderVew(inflate.inflate(R.layout.fragment_main_adapter,parent,false));
        }else {
            viewHolder = new ItemViewHolderView(inflate.inflate(layoutResId,parent,false));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //holder.itemView.setBackgroundResource(R.drawable.item_selector);
        if (holder instanceof ViewPagerHolderVew) {
            ((ViewPagerHolderVew) holder).mViewPager.setAdapter(new MainViewPagerAdapter());
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
            holder.itemView.setTag(holder.getLayoutPosition());
        }

        if (holder instanceof ItemViewHolderView) {
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
            holder.itemView.setTag(holder.getLayoutPosition());
           ((ItemViewHolderView) holder).mTextView.setText(data.get(position -1).getPicturename());
            Log.e(TAG, "onBindViewHolder: "  + data.get(position -1).getUrl() );
            GlideApp.with(mContext)
                    .load(data.get(position -1).getUrl())
                    .placeholder(R.mipmap.ic_favorite_border_black_24dp)
                    .error(R.mipmap.ic_favorite_border_black_24dp)
                    .centerCrop()
                    .dontAnimate()
                    .into(((ItemViewHolderView) holder).imageView);
        }

    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position  < 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int viewType = holder.getItemViewType();
        if (viewType ==  1){
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams)lp;
                p.setFullSpan(true);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onClick(data, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null){
            mOnItemLongClickListener.onLongClick(v, (Integer) v.getTag());
        }
        return true;
    }



    public interface OnItemClickListener<T> {
        void onClick(List<T> url, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View view, int position);

    }


    private static class ViewPagerHolderVew extends RecyclerView.ViewHolder {
        ViewPager mViewPager;

        public ViewPagerHolderVew(View itemView) {
            super(itemView);
            mViewPager = (ViewPager) itemView.findViewById(R.id.fragment_main_adapter_viewpager);
        }
    }

    private class ItemViewHolderView extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView imageView;
        public ItemViewHolderView(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.fragment_main_adapter_textview);
            imageView = (ImageView) itemView.findViewById(R.id.fragment_main_adapter_imageview);
        }
    }

    private class MainViewPagerAdapter extends PagerAdapter {
        List<String> headerUrl;
        List<ImageView> mImageViews;


        public MainViewPagerAdapter() {
            if (headerUrl == null){
                headerUrl = new ArrayList<>();
            }
            headerUrl.add("http://bmob-cdn-14274.b0.upaiyun.com/2017/09/25/178dd21d40e43154806e2bfbd5b0e4a9.jpg");
            headerUrl.add("http://bmob-cdn-14274.b0.upaiyun.com/2017/09/25/c1534cf940fe271780fd922a37ebf55f.jpg");
            headerUrl.add("http://bmob-cdn-14274.b0.upaiyun.com/2017/10/15/17fc0aeb40f47427803907a0aefad518.jpg");
            headerUrl.add("http://bmob-cdn-14274.b0.upaiyun.com/2017/10/15/c154e7b94027983280b2c18f1c27423c.jpg");
            headerUrl.add("http://bmob-cdn-14274.b0.upaiyun.com/2017/10/15/2f9f099a40b30cc38040ecf5835e746f.JPG");
            if (mImageViews == null){
                mImageViews = new ArrayList<>();
            }
            for (int i = 0; i < 5; i++) {
                mImageViews.add(new ImageView(mContext));

            }
        }

        @Override
        public int getCount() {
            return headerUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT
            );
            ImageView imageView = mImageViews.get(position);
            imageView.setLayoutParams(params);

            GlideApp.with(mContext)
                    .load(headerUrl.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .dontAnimate()
                    .into(imageView);
            container.addView(imageView);


            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }
    }
}