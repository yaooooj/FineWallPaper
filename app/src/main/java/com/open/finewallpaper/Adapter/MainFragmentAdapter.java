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
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.GlideApp;
import com.open.finewallpaper.Util.RvDecoration;

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
            viewHolder = new ViewPagerHolderVew(inflate.inflate(R.layout.adapter_1,parent,false));
        }else if (viewType == 2){
            viewHolder = new ItemViewHolderView(inflate.inflate(layoutResId,parent,false));
        } else  {
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
            ((ItemViewHolderView) holder).mTextView.setText(data.get(position).getType());
        }

        if (holder instanceof GrindLayoutHolderView){
            CustomLayout layoutManager = new CustomLayout(mContext,2);
            layoutManager.setScrollEnable(false);
            ((GrindLayoutHolderView) holder).recyclerView.setLayoutManager( layoutManager);
            ((GrindLayoutHolderView) holder).recyclerView.setAdapter(new CurrentAdapter(mContext));
            ((GrindLayoutHolderView) holder).recyclerView.addItemDecoration(new RvDecoration(mContext));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position  < 1) {
            return 1;
        } else if ((position+1)%2 == 1 || position == 1){
            return 2;
        }else if ((position + 1) / 2 == 0){
            return 3;
        }
        return 0;
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
        public ItemViewHolderView(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.adapter_2_tv);
        }
    }

    private class GrindLayoutHolderView extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        public GrindLayoutHolderView(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.adapter_3_rv);
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