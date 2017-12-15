package com.open.finewallpaper.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.open.finewallpaper.R;

/**
 * Created by SEELE on 2017/11/15.
 */

public class LoadingFooter extends RelativeLayout{
    private final static String TAG = "LoadingFooter";
    protected State mState = State.Loading;
    private View mLoadingView;
    private View mNetworkErrorView;
    private View mTheEndView;
    //private ProgressBar mLoadingProgress;
    private TextView mLoadingText;

    public LoadingFooter(Context context) {
        super(context);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.list_footer, this);
        setOnClickListener(null);

        setState(State.Loading, true);
    }

    public State getState() {
        return mState;
    }

    public void setState(State status ) {
        setState(status, true);
    }

    public void setState(State status, boolean showView) {

        mState = status;
        Log.e(TAG, "setState: " + status );
        switch (status) {

            case Normal:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                }

                if (mTheEndView != null) {
                    mTheEndView.setVisibility(View.GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(View.GONE);
                }

                break;
            case Loading:
                setOnClickListener(null);
                if (mTheEndView != null) {
                    mTheEndView.setVisibility(View.GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(View.GONE);
                }

                if (mLoadingView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.load_viewstub);
                    mLoadingView = viewStub.inflate();

                    //mLoadingProgress = (ProgressBar) mLoadingView.findViewById(R.id.loading_progress);
                    mLoadingText = (TextView) mLoadingView.findViewById(R.id.footer_load_tv);
                } else {
                    mLoadingView.setVisibility(View.VISIBLE);
                }

                mLoadingView.setVisibility(showView ? View.VISIBLE : View.GONE);

               // mLoadingProgress.setVisibility(View.VISIBLE);
                Log.e(TAG, "setState: " + "正在加载");
                mLoadingText.setText(R.string.footer_load);
                break;
            case TheEnd:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(View.GONE);
                }

                if (mTheEndView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.footer_viewstub);
                    mTheEndView = viewStub.inflate();
                } else {
                    mTheEndView.setVisibility(View.VISIBLE);
                }

                mTheEndView.setVisibility(showView ? View.VISIBLE : View.GONE);
                break;
            case NetWorkError:

                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                }

                if (mTheEndView != null) {
                    mTheEndView.setVisibility(View.GONE);
                }

                if (mNetworkErrorView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.error_viewstub);
                    mNetworkErrorView = viewStub.inflate();
                } else {
                    mNetworkErrorView.setVisibility(View.VISIBLE);
                }

                mNetworkErrorView.setVisibility(showView ? View.VISIBLE : View.GONE);
                break;
            default:

                break;
        }
    }

    public static enum State {
        Normal/**正常*/, TheEnd/**加载到最底了*/, Loading/**加载中..*/, NetWorkError/**网络异常*/
    }
}
