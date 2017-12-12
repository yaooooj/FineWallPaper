package com.open.finewallpaper.Adapter;

import android.support.v4.view.CuVp;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yaojian on 2017/8/1.
 */

class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mItemView;
    private String tag;
    public BaseViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    public  <T extends View> T getView(int viewResId){
        View view = mViews.get(viewResId);
        if (view == null){
            view = mItemView.findViewById(viewResId);
            mViews.put(viewResId,view);
        }

        return (T)view;
    }
    public BaseViewHolder setTextView(int resId,CharSequence text){
        TextView textView = getView(resId);
        textView.setText(text);
        return this;
    }

    public BaseViewHolder setImageViewResource(int resId,int imageResource){
        ImageView view = getView(resId);
        view.setImageResource(imageResource);
        return this;
    }
    public BaseViewHolder setImageView(ImageView imageView,int imageResource){
        if (imageView != null){
            imageView.setImageResource(imageResource);
        }
        return this;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public CuVp setViewPager(int resId){
        return getView(resId);
    }
}
