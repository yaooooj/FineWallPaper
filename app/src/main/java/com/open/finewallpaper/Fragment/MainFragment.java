package com.open.finewallpaper.Fragment;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.open.finewallpaper.Activity.MainActivity;
import com.open.finewallpaper.Adapter.MainFragmentAdapter;
import com.open.finewallpaper.Bean.PictureBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private final static String TAG = "MainFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private List<PictureBean> pictureBeen;
    private MainFragmentAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, List<PictureBean> param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //args.putParcelableArrayList(ARG_PARAM2, (ArrayList<? extends Parcelable>) param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
           // pictureBeen = getArguments().getString(ARG_PARAM2);
           // pictureBeen =  getArguments().getParcelableArrayList(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initData();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_main_recyclerview);
        adapter = new MainFragmentAdapter(MainFragment.this,R.layout.fragment_mian_adapter_m,pictureBeen);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                final int dividerLeft = 10;
                final int dividerRight = 10;
                final int dividerHeight = 10;

                int childPosition = parent.getChildAdapterPosition(view);
                RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager){
                    int spanCount = ((StaggeredGridLayoutManager)layoutManager).getSpanCount();
                    if ((childPosition ) % 5 == 0){
                        outRect.set(0,0,0,dividerHeight);
                    }else if ((childPosition + 2) % spanCount == 0){
                        outRect.set(dividerLeft,0,dividerRight,dividerHeight);
                    }else {
                        outRect.set(dividerLeft,0,0,dividerHeight);
                    }

                }
            }
        });
        return view;
    }


    public void initData(){
        pictureBeen = new ArrayList<>();


        BmobQuery<PictureBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addQueryKeys("url,picturename");
        bmobQuery.setLimit(12);
        bmobQuery.findObjects(new FindListener<PictureBean>() {
            @Override
            public void done(List<PictureBean> list, BmobException e) {
                if (e == null){
                    ToastUtil.show(getActivity(),"success");
                    for (PictureBean pictureBean : list){
                        Log.e(TAG, "done: " + pictureBean.getUrl() );
                    }
                    adapter.updataData(list);
                }else {
                    Log.e(TAG, "done: " + "bmob失败：" +e.getMessage()+","+e.getErrorCode());
                }

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.main_menu,menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
