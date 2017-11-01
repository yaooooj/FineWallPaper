package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by SEELE on 2017/9/25.
 */

public  class FreshViewPager extends ViewGroup {

    public View footer;
    public View header;

    public PullFooter mPullFooter;
    public PullHeader mPullHeader;

    public int bottomScroll;
    public int lastChildIndex;
    public static final String TAG = "FreshViewPager";


    private float mLastMotionY;
    private float mInitialMotionY;
    private float mLastMotionX;
    private float mInitialMotionX;
    private int mTouchSlop;

    private static final int MIN_DISTENCE = 50;

    private OnPullListener mListener;

    private PullStatus mStatus = PullStatus.DEFAULT;

    private float damp = 0.5f;

    private int SCROLl_TIME = 300;
    private boolean isRefreshSuccess = false;
    private boolean isLoadSuccess = false;

    private Scroller mScroller;

    private Fragment mFragment;

    private OnPullListener onPullListener;
    public FreshViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mScroller = new Scroller(context);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lastChildIndex = getChildCount() - 1;
    }

    public void setHeader(PullHeader pullHeader) {
        this.mPullHeader = pullHeader;
    }

    public void setFooter(PullFooter pullFooter) {
        this.mPullFooter = pullFooter;
    }

    public void addHeader(HeaderView header) {
        this.header = header;
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(header, layoutParams);
    }


    public void addFooter(FooterView footer) {
        this.footer = footer;
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(footer, layoutParams);
    }


    public void addFragment(PopupWindow fragment){

        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0;i < getChildCount();i++){
            View child = getChildAt(i);
            if (child.getVisibility() == GONE){
                continue;
            }
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int contentHeight = 0;

        for (int index = 0;index < getChildCount();index++){
            View child = getChildAt(index);

            if (child.getVisibility() == GONE){
                continue;
            }

            if (child == header){
                child.layout(0,0 - child.getMeasuredHeight(),child.getMeasuredWidth(),0);
            }
            else if(child == footer){
                child.layout(0,contentHeight,getMeasuredWidth(),contentHeight + child.getMeasuredHeight());
            }
            else {
                child.layout(0,contentHeight,child.getMeasuredWidth(),contentHeight + child.getMeasuredHeight());
                if (index <= lastChildIndex){
                    if (child instanceof ScrollView){
                        contentHeight += getMeasuredHeight();
                        continue;
                    }
                    contentHeight += child.getMeasuredHeight();
                }
            }
        }
        bottomScroll = contentHeight - getMeasuredHeight();
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = mInitialMotionY = ev.getY();
                mLastMotionX = mInitialMotionX = ev.getX();
                //lastYMove = y;
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float xDiff = Math.abs(x - mLastMotionX);
                final float yDiff = Math.abs(y - mInitialMotionY);
                if (y - mInitialMotionY > 20 && yDiff * 0.5f > xDiff ){
                    View child = getFirstVisibleChild();
                    if (child != null && isTop(child)){
                        intercept = true;
                    }
                }else if (mInitialMotionY - y > 20 && yDiff * 0.5f > xDiff){
                    View child = getLastVisibleChild();
                    if (child != null && isBottom(child)){
                        intercept = true;
                    }
                }else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int yDiff = 0;
        float y = ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                yDiff  = (int)( mLastMotionY - y);
                mLastMotionY = y;
                if (getScrollY() < 0){

                    scrollBy(0, yDiff / 2);

                    if (header != null && header instanceof HeaderView){
                        if (Math.abs(getScrollY()) >  header.getMeasuredHeight()){
                            updateStatus(PullStatus.DOWN_AFTER);
                            mStatus = PullStatus.DOWN_AFTER;
                        }else {
                            updateStatus(PullStatus.DOWN_BEFORE);
                            mStatus = PullStatus.DOWN_BEFORE;
                        }
                    }else {
                        mStatus = PullStatus.DEFAULT;
                    }
                }else {
                    scrollBy(0,yDiff / 2);
                    if (footer != null && footer instanceof  FooterView){
                        if ( Math.abs(getScrollY()) >=  (bottomScroll + footer.getMeasuredHeight())){
                            updateStatus(PullStatus.UP_AFTER);
                            mStatus = PullStatus.UP_AFTER;
                        }else {
                            updateStatus(PullStatus.UP_BEFORE);
                            mStatus = PullStatus.UP_BEFORE;
                        }
                    }else {
                        mStatus = PullStatus.DEFAULT;
                    }

                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                mLastMotionY = mInitialMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
                switch (mStatus){
                    case DEFAULT:
                        scrollToDefaultStatus(yDiff);
                        break;
                    case DOWN_BEFORE:
                        scrollToDefaultStatus(yDiff);
                        break;
                    case DOWN_AFTER:
                        mScroller.startScroll(0, yDiff,0, yDiff - header.getMeasuredHeight(),500);
                        postInvalidate();
                        scrollToRefreshStatus(yDiff);
                        break;
                    case UP_BEFORE:
                        scrollToDefaultStatus(yDiff);
                        break;
                    case UP_AFTER:
                        mScroller.startScroll(0, yDiff,0, yDiff + bottomScroll + footer.getMeasuredHeight(),500);
                        postInvalidate();
                        scrollToLoadStatus(yDiff);
                        break;
                    default:
                        break;
                }
                break;
        }
        return true;
    }
    private View getFirstVisibleChild() {

        for (int i =0; i < getChildCount();i++){
            View child = getChildAt(i);
            if (child.getVisibility() == GONE){
                continue;
            }else {
                return child;
            }
        }
        return null;
    }

    private View getLastVisibleChild() {
        for (int i = lastChildIndex; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            } else {
                return child;
            }
        }
        return null;
    }

    private boolean isTop(View view){
        boolean intercept = false;
        if (view instanceof ViewGroup) {
            if (view instanceof NestedScrollView){

                if (view.getScrollY() <= 0){
                    intercept = true;
                }
            }else if (view instanceof ScrollView){
                if (view.getScrollY() <= 0) {
                    intercept = true;
                }
            }else {
                intercept = isChildTop((ViewGroup) view);
            }

        }else {
            intercept = false;
        }
        return intercept;
    }

    private boolean isChildTop(ViewGroup viewGroup){
        int minY = 0;
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            int topMargin = 0;
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                topMargin = ((MarginLayoutParams) lp).topMargin;
            }
            int top = view.getTop() - topMargin;
            minY = Math.min(minY, top);
        }
        return minY >= 0;
    }

    private boolean isBottom(View view){
        boolean intercept = false;
        if (view instanceof ViewGroup) {
            if (view instanceof NestedScrollView){
                NestedScrollView nestedScrollView = (NestedScrollView) view;
                if (nestedScrollView.getScrollY() >= (nestedScrollView.getChildAt(0).getHeight() - nestedScrollView.getHeight())) {
                    intercept = true;
                }
            }else if (view instanceof ScrollView){
                ScrollView scrollView = (ScrollView) view;
                if (scrollView.getScrollY() >= scrollView.getChildAt(0).getHeight() - scrollView.getHeight()){
                    intercept = true;
                }
            }else {
                intercept =  isChildBottom((ViewGroup) view);
            }
        } else {
            intercept = false;
        }
        return intercept;
    }

    private boolean isChildBottom(ViewGroup viewGroup){
        int maxY = 0;
        int count = viewGroup.getChildCount();
        if (count == 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            int bottomMargin = 0;
            LayoutParams lp = view.getLayoutParams();
            if (lp instanceof MarginLayoutParams) {
                bottomMargin = ((MarginLayoutParams) lp).bottomMargin;
            }
            int bottom = view.getBottom() + bottomMargin;
            maxY = Math.max(maxY, bottom);
        }
        int h = viewGroup.getMeasuredHeight() - viewGroup.getPaddingBottom();
        return maxY <= h;
    }


    private void  onDefault(){
        isRefreshSuccess = false;
        isLoadSuccess = false;
    }

    private void updateStatus(PullStatus status) {
        this.mStatus = status;
        int scrollY = getScrollY();
        //LogUtil.print("status=" + status);
        // 判断本次触摸系列事件结束时,Layout的状态
        if (header instanceof HeaderView || footer instanceof FooterView){

            switch (status) {
                //默认状态
                case DEFAULT:
                    onDefault();
                    break;
                //下拉刷新
                case DOWN_BEFORE:
                    ((HeaderView)header).onDownBefore(scrollY);
                    break;
                case DOWN_AFTER:
                    ((HeaderView) header).onDownAfter(scrollY);
                    break;
                case REFRESH_SCROLLING:
                    ((HeaderView) header).onRefreshScrolling(scrollY);
                    break;
                case REFRESH_DOING:
                    ((HeaderView) header).onRefreshDoing(scrollY);
                    //mListener.onRefresh();
                    break;
                case REFRESH_COMPLETE_SCROLLING:
                    ((HeaderView) header).onRefreshCompleteScrolling(scrollY, isRefreshSuccess);
                    break;
                case REFRESH_CANCEL_SCROLLING:
                    ((HeaderView) header).onRefreshCancelScrolling(scrollY);
                    break;
                //上拉加载更多
                case UP_BEFORE:
                    ((FooterView)footer).onUpBefore(scrollY);
                    break;
                case UP_AFTER:
                    ((FooterView)footer).onUpAfter(scrollY);
                    break;
                case LOADMORE_SCROLLING:
                    ((FooterView)footer).onLoadScrolling(scrollY);
                    break;
                case LOADMORE_DOING:
                    ((FooterView)footer).onLoadDoing(scrollY);
                    //mListener.onLoadMore();
                    break;
                case LOADMORE_COMPLETE_SCROLLING:
                    ((FooterView)footer).onLoadCompleteScrolling(scrollY, isLoadSuccess);
                    break;
                case LOADMORE_CANCEL_SCROLLING:
                    ((FooterView)footer).onLoadCancelScrolling(scrollY);
                    break;
            }
        }

    }

    //滚动到加载状态
    private void scrollToLoadStatus(int yDiff) {

        updateStatus(PullStatus.LOADMORE_DOING);
        if (onPullListener != null){
            if (isRefreshSuccess = onPullListener.onLoadMore()){
                updateStatus(PullStatus.LOADMORE_COMPLETE_SCROLLING);
                //updateStatus(PullStatus.DEFAULT);
                mScroller.startScroll(0, yDiff,0, yDiff + bottomScroll,500);
                postInvalidate();
            }else {
                updateStatus(PullStatus.LOADMORE_CANCEL_SCROLLING);
            }

        }

    }

    //滚动到默认状态
    private void scrollToDefaultStatus(int yDiff){
        mScroller.startScroll(0,yDiff,0,0,500);
        postInvalidate();
    }
    //停止刷新
    public void stopRefresh(boolean isSuccess) {
        isRefreshSuccess = isSuccess;
        //scrollToDefaultStatus(PullStatus.REFRESH_COMPLETE_SCROLLING);
    }

    //停止加载更多
    public void stopLoadMore(boolean isSuccess) {
        isLoadSuccess = isSuccess;
        //scrollToDefaultStatus(PullStatus.LOADMORE_COMPLETE_SCROLLING);
    }

    //滚动到刷新状态
    private void scrollToRefreshStatus(int yDiff){
        updateStatus(PullStatus.REFRESH_DOING);
        if (onPullListener != null){

            if (isRefreshSuccess = onPullListener.onRefresh(yDiff)){
                updateStatus(PullStatus.REFRESH_COMPLETE_SCROLLING);
                //updateStatus(PullStatus.DEFAULT);
                mScroller.startScroll(0, yDiff,0, 0,500);
                postInvalidate();
            }else {
                updateStatus(PullStatus.REFRESH_CANCEL_SCROLLING);
            }

        }

    }


    public void setOnPullListener(OnPullListener onPullListener){
        this.onPullListener = onPullListener;
    }


}



















