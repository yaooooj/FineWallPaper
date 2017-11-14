package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Animator;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.open.finewallpaper.Bean.ItemBean;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.GlideUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/10/15.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter
        implements View.OnClickListener, View.OnLongClickListener{
    private final static String TAG = "MainFragmentAdapter";
    private List<String> data;
    private List<SetBean> urlList;
    private List<ItemBean> itemList;


    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();


    private Context mContext;
    private Fragment mFragment;
    private int layoutResId;
    private LayoutInflater inflate;
    private OnItemClickListener mOnItemClickListener;
    private OnTextViewClickListener mOnTextViewClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private int footerView = R.layout.adapter_footer_load;
    private int maxCount = 0;

    private boolean isFresh = false;

    public MainFragmentAdapter(Context context, int layoutResId,List<ItemBean> data) {
        this.itemList = data;
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
        urlList = new ArrayList<>();
        itemList = new ArrayList<>();

    }

    private boolean isFirstInGroup(int pos){
        //Log.e(TAG, "isFirstInGroup: " + pos );
        if (pos == itemList.size()){
            return false;
        }else {
            if (pos == 0){
                return true;
            }else {
                //Log.e(TAG, "isFirstInGroup: " + itemList.size() + " pos = " +pos );
                String preGroupId = itemList.get(pos-1).getImgType();
                String groupId = itemList.get(pos).getImgType();
                return !preGroupId .equals( groupId);
            }
        }
    }

    public void upData(List<ItemBean> data){
        if (itemList.size() == 0){
            this.itemList = data;
            notifyDataSetChanged();
        }else {
            Log.e(TAG, "upData: "+ "have data");
            for (int i = 0;i < data.size();i++){
                itemList.add(data.get(i));
            }
            notifyItemRangeChanged(itemList.size(),data.size());
        }


    }


    public void showError(){
        if (itemList != null){
            setFooterLayout(R.layout.adapter_footer_error);
        }
        //itemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
         if (viewType == 1){
            viewHolder = new GrindLayoutHolderView(inflate.inflate(R.layout.adapter_3,parent,false));
         }else if (viewType == 2){
             viewHolder = new EmptyViewHolder(inflate.inflate(R.layout.adapter_error,parent,false));
         }else if (viewType == 4){
             viewHolder = new FooterViewHolder(mFooterViews.get(0));
         }
        else {
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

            GlideUtil.LoadImage(
                    mContext,itemList.get(position).getImgBean().getImgUrl(), ImageView.ScaleType.FIT_XY,
                    (float) 1.5,((ItemViewHolderView) holder).mImageView);

            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(position);
        }else if (holder instanceof EmptyViewHolder){
            Log.e(TAG, "onBindViewHolder: " );
            //Animation a = AnimationUtils.loadAnimation(((EmptyViewHolder) holder).mProgressBar.getContext(),R.anim.ro);
            //((EmptyViewHolder) holder).mProgressBar.startAnimation(a);
        }
        else if (holder instanceof FooterViewHolder){
            Log.e(TAG, "onBindViewHolder: " + "footer" );

        } else if (holder instanceof GrindLayoutHolderView){

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
        if (itemList.isEmpty()){
            return 1;
        }
        return itemList.size() + mHeaderViews.size() + mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.isEmpty()){
            return 2;
        }else if (position == itemList.size()){
            return 4;
        }else if (isFirstInGroup(position) ){
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
                    if (viewType == 1 || viewType == 2){
                        return gridLayoutManager.getSpanCount();
                    }else if (viewType == 4){
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

    }

    public void setFooterLayout(int layoutResId){
        if (layoutResId != 0 ){
            footerView = layoutResId;

            notifyItemChanged(itemList.size()+1);
        }else {
           throw new IllegalArgumentException("argument set exception");
        }

    }

    public void addHeaderView(View view){
        if (view == null){
            throw  new RuntimeException("header is null");
        }
        mHeaderViews.add(view);
        notifyDataSetChanged();
    }

    public View getHeaderView(){
        return mHeaderViews.size() > 0 ? mHeaderViews.get(0) : null;
    }

    public void addFooterView(View view){
        if (view == null){
            throw  new RuntimeException("footer is null");
        }
        mFooterViews.add(view);
        notifyDataSetChanged();
    }


    public View getFooterView(){
        return mFooterViews.size() > 0 ? mFooterViews.get(0) : null;
    }


    public int getFooterViewSize(){
        return mFooterViews.size();
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
        int position=0;
        if (mOnItemClickListener != null){
            ArrayList<SetBean> url = new ArrayList<>();
            SetBean setBean;
            for (int i=0;i< itemList.size();i++){
                // Log.e(TAG, "onClick: " + itemList.get((Integer) v.getTag()).getImgType() );
                if (itemList.get((Integer) v.getTag()).getImgType().equals(itemList.get(i).getImgType())){
                    if (itemList.get(i).getImgBean().getImgUrl() != null){
                        if (position <=0 ){
                            position = i;
                        }
                        setBean = new SetBean();
                        setBean.setUrl(itemList.get(i).getImgBean().getImgUrl());
                        setBean.setName(itemList.get(i).getImgBean().getImgName());
                        url.add(setBean);
                    }
                }

            }
            mOnItemClickListener.onClick(url,(int) v.getTag() - position);
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

    private class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
    private class EmptyViewHolder extends RecyclerView.ViewHolder{
        //ImageView mProgressBar;
        public EmptyViewHolder(View itemView) {
            super(itemView);
           /// mProgressBar = (ImageView) itemView.findViewById(R.id.empty_load);
        }
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

}
