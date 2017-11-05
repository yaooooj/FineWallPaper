package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by yaojian on 2017/10/25.
 */

public class CurrentAdapter extends RecyclerView.Adapter implements View.OnClickListener,View.OnLongClickListener{
    private final static String TAG = "CurrentAdapter";
    private List<PictureBean> data;
    private LayoutInflater inflate;
    private Context mContext;
    private String type;

    private OnItemClickListener onItemListener;
    private OnItemLongClickListener onItemLongLinstener;

    private boolean isFresh;
    private List<SetBean> url;



    public CurrentAdapter(Context context,List<SetBean> url,boolean isFresh) {
        this.mContext = context;
        this.url = url;
        this.isFresh = isFresh;
        inflate = LayoutInflater.from(context);
        //init();
    }

    private void init(){
        data = new ArrayList<>();
        /*
        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        //bmobQuery.addQueryKeys("url,picturename,type");
        bmobQuery.addWhereEqualTo("type",type);
        bmobQuery.setLimit(9);
        boolean isCache = bmobQuery.hasCachedResult(PictureBean.class);
        if (isFresh){
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }else {
            if (isCache){
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            }else {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            }
        }
        //bmobQuery.order("type");
        bmobQuery.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(final List<PictureBean> list, BmobException e) {

                if (e == null){
                    if (list != null){
                        Log.e(TAG, "done: "  + "request type url ");
                        data = list;
                        notifyDataSetChanged();
                    }
                }else {
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        */
    }
    public  void  updataData (List<PictureBean> data ){
        this.data =  data;
        notifyDataSetChanged();
        //notifyItemRangeInserted(0,data.size());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentVH(inflate.inflate(R.layout.current_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


            if (holder instanceof CurrentVH){
                //Log.e(TAG, "onBindViewHolder: " +  data.get(position).getUrl());
                GlideUtil.LoadImageToView(
                        mContext,url.get(position).getUrl(), ImageView.ScaleType.FIT_XY, (float) 1.5,((CurrentVH) holder).mImageView);

                ((CurrentVH) holder).cardView.setOnClickListener(this);
                ((CurrentVH) holder).cardView.setOnLongClickListener(this);
                ((CurrentVH) holder).cardView.setTag(holder.getAdapterPosition());
            }

    }


    @Override
    public int getItemCount() {
        return url.size();
    }

    public void setOnItemListener(OnItemClickListener itemClickListener) {
        this.onItemListener = itemClickListener;
    }

    public void setOnItemLongLinstener(OnItemLongClickListener longClickListener) {
        this.onItemLongLinstener = longClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemListener != null && url.size() != 0){

            ArrayList<SetBean> urls = new ArrayList<>();
            SetBean setBean;
            for (int i =0 ;i < url.size(); i++){
                setBean = new SetBean();
                setBean.setUrl(url.get(i).getUrl());
                setBean.setName(url.get(i).getName());
                urls.add(setBean);
            }
            onItemListener.onClick(urls, (Integer) v.getTag());

        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    private static class CurrentVH extends RecyclerView.ViewHolder {
        //TextView textView;
        ImageView   mImageView;
        TextView textView;
        CardView cardView;
        public CurrentVH(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.current_iv);
            textView = (TextView) itemView.findViewById(R.id.current_tv);
            cardView = (CardView) itemView.findViewById(R.id.current_cv);
        }
    }

    public interface OnItemClickListener {
        void onClick(ArrayList<SetBean> url, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View view, int position);

    }
}
