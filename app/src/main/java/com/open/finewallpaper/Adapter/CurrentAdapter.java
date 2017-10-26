package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.open.finewallpaper.Activity.MainActivity;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.R;
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

public class CurrentAdapter extends RecyclerView.Adapter {
    private final static String TAG = "CurrentAdapter";
    private List<PictureBean> data;
    private LayoutInflater inflate;
    private Context mContext;

    public CurrentAdapter(Context context) {
        this.mContext = context;
        inflate = LayoutInflater.from(context);
        init();
    }

    private void init(){
        data = new ArrayList<>();
        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addQueryKeys("url,picturename,type");
        bmobQuery.setLimit(12);
        bmobQuery.order("type");
        bmobQuery.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(final List<PictureBean> list, BmobException e) {

                if (e == null){
                    data = list;
                    notifyDataSetChanged();
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CurrentVH){
                //((CurrentVH) holder).textView.setText(data.get(position).getPicturename());
                //((CurrentVH) holder).mImageView.setImageResource(R.mipmap.ic_favorite_border_black_24dp);

                GlideApp.with(mContext)
                        .load(data.get(position).getUrl())
                        .placeholder(R.mipmap.ic_favorite_border_black_24dp)
                        .error(R.mipmap.ic_favorite_border_black_24dp)
                        .centerCrop()
                        .dontAnimate()
                        .into((((CurrentVH) holder).mImageView));
            }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class CurrentVH extends RecyclerView.ViewHolder {
        //TextView textView;
        ImageView   mImageView;
        public CurrentVH(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.current_iv);
        }
    }
}
