package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.open.finewallpaper.Activity.ShowPictureActivity;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yaojian on 2017/11/3.
 */

public class ShowAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private static final  String TAG = "ShowAdapter";

    private Context context;
    private OnItemListener mOnItemListener;
    private String type;
    private ArrayList<SetBean> data;

    public ShowAdapter(Context context,String type) {

        this.context = context;
        this.type = type;
        Log.e(TAG, "ShowAdapter: " + type );
        init();
    }

    public void init(){
        data = new ArrayList<>();
        BmobQuery<PictureBean> query = new BmobQuery<>();
        query.addWhereEqualTo("type",type);
        //query.addQueryKeys(type +",url");
        query.order("createdAt");
        query.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(List<PictureBean> list, BmobException e) {
                if (e == null){
                    SetBean finePic;
                    for (int i =0 ;i < list.size();i++){
                        finePic = new SetBean();
                        finePic.setName(list.get(i).getPicturename());
                        finePic.setUrl(list.get(i).getUrl());
                        data.add(finePic);
                    }
                    notifyDataSetChanged();
                }else {
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_2_backup,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            ((MyViewHolder) holder).textView.setText(data.get(position).getName());

            GlideUtil.LoadImageToView(context,data.get(position).getUrl(),
                    ImageView.ScaleType.CENTER_CROP, (float) 1.5,((MyViewHolder) holder).mImageView);
            ((MyViewHolder) holder).itemView.setTag(holder.getAdapterPosition());
            holder.itemView.setOnClickListener(this);

        }
    }



    public void setOnItemListener(OnItemListener onItemListener) {
        mOnItemListener = onItemListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemListener != null){
            mOnItemListener.itemClick(data, (Integer) v.getTag());
        }
    }

    private static class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView mImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.show_tv);
            mImageView = (ImageView) itemView.findViewById(R.id.show_im);
        }
    }

    public interface OnItemListener{
        void itemClick(ArrayList<SetBean> data, int position);
    }
}
