package com.open.finewallpaper.Fragment;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.open.finewallpaper.Adapter.SetWrapperAdapter;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/10/9.
 */

public  class SetWrapperFragment extends DialogFragment {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_paper_fragment,container);
        ArrayList<String> data = new ArrayList<>();
        data.add("Set Wrapper");
        data.add("Set Lock Wrapper");
        data.add("Set Both");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.wrapper_fragment_recyclerview);
        SetWrapperAdapter adapter = new SetWrapperAdapter(data,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceDecoration(SpaceDecoration.VERTICAL_LIST));
        adapter.setOnItemClickListener(new SetWrapperAdapter.OnItemClickListener() {
            @Override
            public void onClick(List<String> url, int position) {
                Toast.makeText(getContext(),"this is "+ position,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }



    public static SetWrapperFragment Instance(String url){
        SetWrapperFragment setWrapperFragment =  new SetWrapperFragment();
        Bundle args = new Bundle();
        args.putString("url",url);
        //args.putParcelable("recycler", (Parcelable) recyclerView);
        //mRecyclerView = recyclerView;
        setWrapperFragment.setArguments(args);
        return setWrapperFragment;
    }

}
