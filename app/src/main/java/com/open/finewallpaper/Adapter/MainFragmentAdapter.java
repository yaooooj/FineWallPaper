package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.open.finewallpaper.Activity.NextActivity;
import com.open.finewallpaper.Bean.ImageBean;
import com.open.finewallpaper.Bean.ItemBean;
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
    private List<ItemBean> itemList;


    private Context mContext;
    private Fragment mFragment;
    private int layoutResId;
    private LayoutInflater inflate;
    private OnItemClickListener mOnItemClickListener;
    private OnTextViewClickListener mOnTextViewClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private int maxCount = 0;

    private boolean isFresh = false;

    public MainFragmentAdapter(Context context, int layoutResId,List<ItemBean> data) {
        this.itemList = data;
        this.mContext = context;
        this.layoutResId = layoutResId;
        inflate = LayoutInflater.from(context);
        //init();

    }

    public MainFragmentAdapter(Fragment fragment, int layoutResId,List<String> data) {
        this.data = data;
        this.mFragment = fragment;
        this.layoutResId = layoutResId;
        inflate = LayoutInflater.from(fragment.getContext());
    }
    public void init(){

        urlList = new ArrayList<>();
        itemList = new ArrayList<>();
    }



    private boolean isFirstInGroup(int pos){
        Log.e(TAG, "isFirstInGroup: " + pos );
        if (pos == 0){
            return true;
        }else {
            String preGroupId = itemList.get(pos-1).getImgType();
            String groupId = itemList.get(pos).getImgType();
            return !preGroupId .equals( groupId);
        }
    }

    public void upData(List<ItemBean> data){
        this.itemList = data;
        notifyDataSetChanged();
    }

    public  void  updataData (boolean isFresh){
        data.clear();

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
        final int realPosition = position;
        if (holder instanceof ItemViewHolderView) {
            ((ItemViewHolderView) holder).mTextView.setText(itemList.get(position).getImgBean().getImgName());

            GlideUtil.LoadImageToView(
                    mContext,itemList.get(position).getImgBean().getImgUrl(), ImageView.ScaleType.FIT_XY, (float) 1.5,((ItemViewHolderView) holder).mImageView);

            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(position);
        }

        if (holder instanceof GrindLayoutHolderView){

            if (itemList.get(position).isMore()){
                ((GrindLayoutHolderView) holder).moreTextView.setVisibility(View.VISIBLE);
            }else {
                ((GrindLayoutHolderView) holder).moreTextView.setVisibility(View.GONE);
            }
            ((GrindLayoutHolderView) holder).mTextView.setText(itemList.get(position).getImgType());
            ((GrindLayoutHolderView) holder).moreTextView.setTag(itemList.get(position).getImgType());
            ((GrindLayoutHolderView) holder).moreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnTextViewClickListener.onClick(data, itemList.get(realPosition).getImgType());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFirstInGroup(position)){
            return 1;
        }
        return 3;
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


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnTextViewClickListener(OnTextViewClickListener onTextViewClickListener) {
        mOnTextViewClickListener = onTextViewClickListener;
    }

    @Override
    public void onClick(View v) {

        if (mOnItemClickListener != null){
            ArrayList<SetBean> url = new ArrayList<>();
            SetBean setBean;
            for (int i=0;i< itemList.size();i++){
                if (v.getTag() != null){
                    Log.e(TAG, "onClick: " + itemList.get((Integer) v.getTag()).getImgType() );
                    if (itemList.get((Integer) v.getTag()).getImgType().equals(itemList.get(i).getImgType())){
                        if (itemList.get(i).getImgBean().getImgUrl() != null){
                            setBean = new SetBean();
                            setBean.setUrl(itemList.get(i).getImgBean().getImgUrl());
                            setBean.setName(itemList.get(i).getImgBean().getImgName());
                            url.add(setBean);
                        }

                    }
                }


            }

            mOnItemClickListener.onClick(url,(int)v.getTag()-1);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null){
            mOnItemLongClickListener.onLongClick(v, (Integer) v.getTag());
        }
        return true;
    }

    public interface OnTextViewClickListener{
        void onClick(List<String> url, String type);
    }

    public interface OnItemClickListener {
        void onClick(ArrayList<SetBean> url, int position);
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
