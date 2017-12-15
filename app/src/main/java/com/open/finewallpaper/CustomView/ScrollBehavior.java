package com.open.finewallpaper.CustomView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.open.finewallpaper.R;

import java.lang.ref.WeakReference;

/**
 * Created by SEELE on 2017-12-13.
 */

public class ScrollBehavior extends CoordinatorLayout.Behavior {
    private static final String TAG = "ScrollBehavior";

    WeakReference<View> childView ;
    WeakReference<View> dependencyView;
    private int offset;

    private int headerSize = -1;
    private int titleSize = -1;

    private boolean isScroll;
    private boolean isExpand;

    private Context context;


    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }



    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return super.onTouchEvent(parent, child, ev);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (child != null){
            childView = new WeakReference<>(child);
        }
        if (dependency != null && dependency instanceof RelativeLayout){
            dependencyView = new WeakReference<>(dependency);
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        child.layout(0,0,parent.getWidth(),(parent.getHeight() - dependencyView.get().getHeight()));
        if (headerSize == -1){
            headerSize = dependencyView.get().getHeight();
            //titleSize = dependencyView.get().findViewById(R.id.viewa_tb).getHeight();
            titleSize = 0;
            child.setTranslationY(headerSize);
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        View view  = dependencyView.get();
        float translationY = child.getTranslationY();
        float min = titleSize *1.0f/headerSize;
        float pro = translationY / headerSize;
        /*
        View titleView = dependencyView.get().findViewById(R.id.viewa_tb);
        titleView.setPivotX(0);
        titleView.setPivotY(0);
        titleView.setAlpha(1 - pro);
        if (pro  <= min + 1){
            titleView.setAlpha(1);
        }
        */
       // View viewpager = dependencyView.get().findViewById(R.id.heading_vp);
      //  int offset = viewpager.getHeight() - child.getTop();
      //  ViewCompat.offsetTopAndBottom(viewpager,offset);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        clearAnimator();
        isScroll = false;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        consumed[1] = scroll(coordinatorLayout,child,dy, titleSize,headerSize);
    }


    private int scroll(View parent,View child,int newOffset,int min,int max){
        Log.e(TAG, "scroll: new offset" + newOffset );
        int consume = 0;
        int nowOffset = getOffset() - newOffset;
        Log.e(TAG, "scroll: now offset" + nowOffset );
        final int currOffset = getOffset();
        View viewpager = dependencyView.get().findViewById(R.id.heading_vp);
        if (currOffset > min && currOffset < max){
            nowOffset = nowOffset < titleSize ? titleSize : (nowOffset > headerSize ? headerSize : nowOffset);
            if (currOffset != nowOffset){
                setOffset(nowOffset);
                child.offsetTopAndBottom(nowOffset);
                viewpager.offsetTopAndBottom(nowOffset);

                consume = currOffset - nowOffset;
            }
        }
        return consume;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed > 0) {
            return;
        }
        View view = dependencyView.get();

        ViewGroup.LayoutParams params = view.getLayoutParams();
        int height = (int) child.getTranslationY();
        if (dyUnconsumed < 0 && params!=null) {
            int h = height - dyUnconsumed;

            if (h >= 0 && h <= headerSize) {
                params.height = h;
                view.setLayoutParams(params);
                //view.offsetTopAndBottom(h);
                child.setTranslationY(h);
                if (child instanceof RecyclerView){
                   RecyclerView recycleView = (RecyclerView) child;
                    //recycleView.setViewHeight(recycleView.getEndView(),0);
                }
            }

        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return onStopDrag();
    }

    private boolean onStopDrag(){
        int height = dependencyView.get().getHeight();
        if (height> titleSize){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return true;
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        int height = dependencyView.get().getHeight();
        float translationY = childView.get().getTranslationY();
        if (translationY > height) {
            isExpand = true;
        } else {
            isExpand = false;
        }

        if (isExpand) {
            float pro = ((translationY - height) * 1.0f / headerSize);
            createExpendAnimator(translationY, height, (int) (500 * pro));
        }


        if (!isScroll && height > titleSize && height < headerSize) {
            childView.get().setScrollY(0);
            if (height < 0.7 * headerSize) {//上滑
                float pro = (height - titleSize) * 1.0f / (headerSize - titleSize);
                createAnimation(height, titleSize, (int) (500 * pro));
            } else {//下滑
                float pro = (headerSize - height) * 1.0f / (headerSize - titleSize);
                createAnimation(height, headerSize, (int) (500 * pro));
            }
            isScroll = true;
        }
    }

    private ValueAnimator animator;

    private void createAnimation(float start, float end, int duration) {
        clearAnimator();
        animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                View view = dependencyView.get();
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = (int) value;
                view.setLayoutParams(params);
                childView.get().setTranslationY(value);

            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    private void createExpendAnimator(float start, float end, int duration) {
        clearAnimator();
        animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
//                View view = dependentView.get();
//                ViewGroup.LayoutParams params = view.getLayoutParams();
//                params.height = (int) value;
//                view.setLayoutParams(params);
                childView.get().setTranslationY(value);

            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    private void clearAnimator(){
        if (animator != null) {
            animator.cancel();
        }
        isScroll = false;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
