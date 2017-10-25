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
 * Created by yaojian on 2017/10/25.
 */

public class CurrentAdapter extends RecyclerView.Adapter {
    private List<String> data;
    private LayoutInflater inflate;

    public CurrentAdapter(Context context,List<String> data) {
        this.data = data;
        inflate = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentVH(inflate.inflate(R.layout.current_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CurrentVH){
                ((CurrentVH) holder).textView.setText(data.get(position));
            }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class CurrentVH extends RecyclerView.ViewHolder {
        TextView textView;
        public CurrentVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.current_tv);
        }
    }
}
