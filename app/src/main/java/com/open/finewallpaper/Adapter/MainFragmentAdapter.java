package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.open.finewallpaper.Activity.NextActivity;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.CoustomView.CustomLayout;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.GlideApp;
import com.open.finewallpaper.Util.GlideUtil;
import com.open.finewallpaper.Util.RvScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private List<SetBean> urlList;
    private Map<String,List<SetBean>> map;

    private Context mContext;
    private Fragment mFragment;
    private int layoutResId;
    private LayoutInflater inflate;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private boolean isFresh = false;

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
        map = new HashMap<>();
        urlList = new ArrayList<>();
        getDataFromNet(isFresh);
    }

    private void getDataFromNet(boolean isFresh){
        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        bmobQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        bmobQuery.addQueryKeys("type,url,name");
        bmobQuery.order("type");
        boolean isCache = bmobQuery.hasCachedResult(PictureBean.class);
        if (isFresh){
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }else {
            if (isCache){
                //bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            }else {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            }
        }
        bmobQuery.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(final List<PictureBean> list, BmobException e) {
                if (e == null){
                    sortData(list);
                    notifyDataSetChanged();
                }else {
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void sortData(List<PictureBean> list){

        for (int i =0 ;i < list.size();i++){
            SetBean setBean = new SetBean();
            setBean.setName(list.get(i).getType());
            setBean.setUrl(list.get(i).getUrl());
            urlList.add(setBean);
        }

    }

    private void sortData1(List<PictureBean> list){
        //data.add(list.get(0).getType());
        data.add(list.get(0).getType());

        List<SetBean> urls = new ArrayList<>();
        SetBean setBean = new SetBean();
        setBean.setName(list.get(0).getPicturename());
        setBean.setUrl(list.get(0).getUrl());
        urls.add(setBean);

        map.put(list.get(0).getType(),urls);
        for (int i = 1 ;i < list.size();i++){

                if (!list.get(i-1).getType().equals(list.get(i).getType())){
                    //Log.e(TAG, "done: " +list.get(i).getType()  );
                    data.add(list.get(i).getType());
                    Log.e(TAG, "sortData: " + list.get(i).getUrl() );
                    urls = new ArrayList<>();
                    map.put(list.get(i).getType(),urls);

                    //urls.add(list.get(i).getUrl());

                }
            setBean = new SetBean();
            setBean.setName(list.get(i).getPicturename());
            setBean.setUrl(list.get(i).getUrl());
            urls.add(setBean);
        }


    }

    public  void  updataData (boolean isFresh){
        data.clear();
        getDataFromNet(isFresh);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
         if (viewType == 1){
            viewHolder = new GrindLayoutHolderView(inflate.inflate(R.layout.adapter_3,parent,false));
        }else {
             viewHolder = new ItemViewHolderView(inflate.inflate(R.layout.adapter_item,parent,false));
         }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //holder.itemView.setBackgroundResource(R.drawable.item_selector);

        if (holder instanceof ItemViewHolderView) {
            ((ItemViewHolderView) holder).mTextView.setText(urlList.get(position).getName());
            GlideUtil.LoadImageToView(
                    mContext,urlList.get(position).getUrl(), ImageView.ScaleType.FIT_XY, (float) 1.5,((ItemViewHolderView) holder).mImageView);

        }

        if (holder instanceof GrindLayoutHolderView){

                ((GrindLayoutHolderView) holder).moreTextView.setVisibility(View.VISIBLE);
                ((GrindLayoutHolderView) holder).moreTextView.setTag(urlList.get(position).getName());
                ((GrindLayoutHolderView) holder).moreTextView.setOnClickListener(this);

        }
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFirstInGroup(position)){
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType == 1 ){
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

    }

    private boolean isFirstInGroup(int pos){
        Log.e(TAG, "isFirstInGroup: " + pos );
        if (pos == 0){
            return false;
        }else {
            String preGroupId = urlList.get(pos-1).getName();
            String groupId = urlList.get(pos).getName();
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
        void onClick(List<String> url, String type);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View view, int position);

    }




    private class ItemViewHolderView extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        public ItemViewHolderView(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_im);
            mTextView = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }

    private class GrindLayoutHolderView extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTextView;
        TextView moreTextView;
        public GrindLayoutHolderView(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.adapter_3_iv);
            mTextView = (TextView) itemView.findViewById(R.id.adapter_3_tv);
            moreTextView = (TextView) itemView.findViewById(R.id.adapter_more_tv);

        }
    }



    public  void hey(){
        /*
        CustomLayout layoutManager = new CustomLayout(mContext,3);
        layoutManager.setScrollEnable(false);
        ((GrindLayoutHolderView) holder).recyclerView.setLayoutManager( layoutManager);
        Log.e(TAG, "onBindViewHolder: " + data.get(1) );
        Log.e(TAG, "onBindViewHolder: "+ data.get(position));
        CurrentAdapter currentAdapter = new CurrentAdapter(mContext,map.get(data.get(position)),isFresh);
        ((GrindLayoutHolderView) holder).recyclerView.setAdapter(currentAdapter);
        ((GrindLayoutHolderView) holder).recyclerView.setNestedScrollingEnabled(false);
        ((GrindLayoutHolderView) holder).recyclerView.addOnScrollListener(new RvScrollListener() {
            @Override
            public void onLoadMore() {
                GlideApp.with(mContext).resumeRequests();
                Log.e(TAG, "onLoadMore: " + "onLoadMore onLoadMore onLoadMore" );
            }

            @Override
            public void onDragLoadMore() {
                Log.e(TAG, "onDragLoadMore: " );
                GlideApp.with(mContext).pauseRequests();
            }
        });
        currentAdapter.setOnItemListener(new CurrentAdapter.OnItemClickListener() {
            @Override
            public void onClick(ArrayList<SetBean> urls, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("url",urls);
                bundle.putInt("position",position);
                Intent intent = new Intent(mContext, NextActivity.class);
                intent.putExtra("urls",bundle);
                mContext.startActivity(intent);
                Toast.makeText(mContext,"click " + position,Toast.LENGTH_SHORT).show();
            }
        });

        ((GrindLayoutHolderView) holder).mTextView.setText(data.get(position));
        */
    }


}
