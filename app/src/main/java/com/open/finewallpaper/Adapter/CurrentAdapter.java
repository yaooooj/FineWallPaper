package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.open.finewallpaper.Activity.MainActivity;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.DisplayUtil;
import com.open.finewallpaper.Util.GlideApp;
import com.open.finewallpaper.Util.RvDecoration;

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

    private OnItemClickListener onItemLinstener;
    private OnItemLongClickListener onItemLongLinstener;

    public CurrentAdapter(Context context,String type) {
        this.mContext = context;
        this.type = type;
        inflate = LayoutInflater.from(context);
        init();
    }

    private void init(){
        data = new ArrayList<>();

        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        //bmobQuery.addQueryKeys("url,picturename,type");
        bmobQuery.addWhereEqualTo("type",type);
        bmobQuery.setLimit(9);
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
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
                //((CurrentVH) holder).textView.setText(data.get(position).getPicturename());
                //((CurrentVH) holder).mImageView.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                Log.e(TAG, "onBindViewHolder: " +  data.get(position).getUrl());
                GlideApp.with(mContext)
                        .load(data.get(position).getUrl())
                        .placeholder(R.mipmap.ic_favorite_border_black_24dp)
                        .error(R.mipmap.ic_favorite_border_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .into(((CurrentVH) holder).mImageView);

                ((CurrentVH) holder).cardView.setOnClickListener(this);
                ((CurrentVH) holder).cardView.setOnLongClickListener(this);
                ((CurrentVH) holder).cardView.setTag(holder.getAdapterPosition());
            }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemLinstener(OnItemClickListener itemClickListener) {
        this.onItemLinstener = itemClickListener;
    }

    public void setOnItemLongLinstener(OnItemLongClickListener longClickListener) {
        this.onItemLongLinstener = longClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemLinstener != null && data.size() != 0){

            List<String> urls = new ArrayList<>();
            for (int i =0 ;i < data.size(); i++){
                urls.add(data.get(i).getUrl());
            }
            onItemLinstener.onClick(urls, (Integer) v.getTag());

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
        void onClick(List<String> url, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View view, int position);

    }
}
