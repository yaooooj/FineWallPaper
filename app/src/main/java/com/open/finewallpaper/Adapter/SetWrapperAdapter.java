package com.open.finewallpaper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.finewallpaper.R;

import java.util.List;

/**
 * Created by SEELE on 2017/10/9.
 */

public class SetWrapperAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<String> data;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;


    public SetWrapperAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SetWallPaperViewHolder(inflater.inflate(R.layout.set_paper_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SetWallPaperViewHolder){
            ((SetWallPaperViewHolder) holder).textView.setText(data.get(position));
            holder.itemView.setTag(holder.getAdapterPosition());
            holder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null){
            onItemClickListener.onClick(data, (Integer) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private class SetWallPaperViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public SetWallPaperViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.set_item_tv);
        }

    }

    public interface OnItemClickListener {
        void onClick(List<String> url, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View view, int position);

    }
}
