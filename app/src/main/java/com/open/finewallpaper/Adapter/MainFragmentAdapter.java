package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.open.finewallpaper.Activity.NextActivity;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.GlideApp;
import com.open.finewallpaper.Util.RvDecoration;
import com.open.finewallpaper.Util.RvScrollListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by SEELE on 2017/10/15.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter
        implements View.OnClickListener, View.OnLongClickListener{
    private final static String TAG = "MainFragmentAdapter";
    private List<String> data;
    private Context mContext;
    private Fragment mFragment;
    private int layoutResId;
    private LayoutInflater inflate;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public MainFragmentAdapter(Context context, int layoutResId,List<String> data) {
        this.data = data;
        this.mContext = context;
        this.layoutResId = layoutResId;
        inflate = LayoutInflater.from(context);
        init();

    }

    public MainFragmentAdapter(Fragment fragment, int layoutResId,List<String> data) {
        this.data = data;
        this.mFragment = fragment;
        this.layoutResId = layoutResId;
        inflate = LayoutInflater.from(fragment.getContext());
    }
    public void init(){
        data = new ArrayList<>();
        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addQueryKeys("type");
        //bmobQuery.setLimit(12);
        bmobQuery.order("type");
        bmobQuery.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(final List<PictureBean> list, BmobException e) {
                if (e == null){
                    data.add(list.get(0).getType());
                    for (int i = 1 ;i < list.size();i++){
                        if (!list.get(i-1).getType().equals(list.get(i).getType())){
                            Log.e(TAG, "done: " +list.get(i).getType()  );
                            data.add(list.get(i).getType());
                        }
                    }
                    notifyDataSetChanged();
                }else {
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    public  void  updataData (List<String> data ){
        this.data =  data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 1){
            viewHolder = new ViewPagerHolderVew(inflate.inflate(R.layout.adapter_1,parent,false));
        }else if (viewType == 3){
            viewHolder = new GrindLayoutHolderView(inflate.inflate(R.layout.adapter_3,parent,false));
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
            ((ItemViewHolderView) holder).mTextView.setText(data.get(position));
        }

        if (holder instanceof GrindLayoutHolderView){
            ((GrindLayoutHolderView) holder).textView.setText(data.get(position - 1));
            ((GrindLayoutHolderView) holder).moreTextView.setTag("moreTextView");
            ((GrindLayoutHolderView) holder).moreTextView.setOnClickListener(this);

            CustomLayout layoutManager = new CustomLayout(mContext,3);
            layoutManager.setScrollEnable(false);
            ((GrindLayoutHolderView) holder).recyclerView.setLayoutManager( layoutManager);
            CurrentAdapter currentAdapter = new CurrentAdapter(mContext,data.get(position -1));
            ((GrindLayoutHolderView) holder).recyclerView.setAdapter(currentAdapter);
            ((GrindLayoutHolderView) holder).recyclerView.addOnScrollListener(new RvScrollListener() {
                @Override
                public void onLoadMore() {
                    GlideApp.with(mContext).resumeRequests();

                }

                @Override
                public void onDragLoadMore() {
                   GlideApp.with(mContext).pauseRequests();
                }
            });
            currentAdapter.setOnItemLinstener(new CurrentAdapter.OnItemClickListener() {
                @Override
                public void onClick(List<PictureBean> url, int position) {
                    Intent intent = new Intent(mContext, NextActivity.class);
                    mContext.startActivity(intent);
                    Toast.makeText(mContext,"click " + position,Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return data.size()  + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position  < 1) {
            return 1;
        }
        return 3;
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

    private boolean isFirstInGroup(int pos){
        Log.e(TAG, "isFirstInGroup: " + pos );
        if (pos == 1){
            return true;
        }else if (pos == 0){
            return  false;
        }else {
            String preGroupId = data.get(pos-1);
            String groupId = data.get(pos);
            return !preGroupId .equals( groupId);
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
            mOnItemClickListener.onClick(data, (String) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null){
            mOnItemLongClickListener.onLongClick(v, (Integer) v.getTag());
        }
        return true;
    }



    public interface OnItemClickListener {
        void onClick(List<String> url, String position);
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
        public ItemViewHolderView(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.adapter_2_tv);
        }
    }

    private class GrindLayoutHolderView extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        TextView textView;
        TextView moreTextView;
        public GrindLayoutHolderView(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.adapter_3_rv);
            textView = (TextView) itemView.findViewById(R.id.adapter_3_tv);
            moreTextView = (TextView) itemView.findViewById(R.id.adapter_more_tv);
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