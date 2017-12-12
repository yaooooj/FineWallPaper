package com.open.finewallpaper.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.CuVp;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.open.finewallpaper.Adapter.ViewPagerAdapter;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.CoustomView.FreshViewPager;
import com.open.finewallpaper.CoustomView.HeaderView;
import com.open.finewallpaper.CoustomView.OnPullListener;
import com.open.finewallpaper.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NextFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NextFragment extends Fragment{
    private static final String TAG = "NextFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CuVp mViewPager;

    // TODO: Rename and change types of parameters
    private ArrayList<SetBean> mParam1;
    private int mParam2;

    private OnFragmentInteractionListener mListener;

    public NextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NextFragment newInstance(ArrayList<? extends Parcelable> param1, int param2) {
        NextFragment fragment = new NextFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        if (param1 == null){
            Log.e(TAG, "newInstance: " + "params is null"  );
        }
        args.putParcelableArrayList(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelableArrayList(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        if (mParam1 != null){

            view = inflater.inflate(R.layout.fragment_next, container, false);
            Toolbar toolbar = (Toolbar)view.findViewById(R.id.next_tb);
            toolbar.getBackground().setAlpha(2);
            toolbar.setOverflowIcon(ContextCompat.getDrawable(getContext(),R.drawable.ic_favorite_border_black_24dp));

            final AppCompatActivity activity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            activity.setSupportActionBar(toolbar);
            if (activity.getSupportActionBar()!= null){
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });

            mViewPager = (CuVp) view.findViewById(R.id.nextfragment_vp);
            mViewPager.setAdapter(new ViewPagerAdapter(mParam1,mParam2,getActivity()));
            mViewPager.setCurrentItem(mParam2);


            FreshViewPager freshViewPager = (FreshViewPager) view.findViewById(R.id.nextfragment_fvp);
            freshViewPager.addHeader(new HeaderView(getContext()));
            freshViewPager.setOnPullListener(new OnPullListener() {
                @Override
                public boolean onRefresh(int diff) {
                    Log.e(TAG, "onRefresh: " + "fresh" );
                    //DownloadImage.downloadImage();
                    ArrayList<SetBean> nameAndUrl = new ArrayList<SetBean>();
                    SetBean setBean = new SetBean();
                    setBean.setUrl(mParam1.get(mViewPager.getCurrentItem()).getUrl());
                    setBean.setName(mParam1.get(mViewPager.getCurrentItem()).getName());
                    nameAndUrl.add(setBean);
                    SetWrapperFragment setWrapperFragment =  SetWrapperFragment.Instance(nameAndUrl);
                    setWrapperFragment.show(getFragmentManager(),"dialog");
                    return true;
                }

                @Override
                public boolean onLoadMore() {
                    return false;
                }

                @Override
                public void onMoveLoad(int dx) {

                }
            });
        }else {
            view = inflater.inflate(R.layout.viewpager_error,container,false);
        }
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_menu,menu);
        //super.onCreateOptionsMenu(menu, inflater);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nvg:
                Toast.makeText(getContext(),"like",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
