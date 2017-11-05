package com.open.finewallpaper.Util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

import com.open.finewallpaper.R;

import java.lang.ref.WeakReference;

/**
 * Created by SEELE on 2017/11/3.
 */

public class ToolBarBehavior extends CoordinatorLayout.Behavior<RecyclerView> {
    private static final String TAG = "ToolBarBehavior";
    private WeakReference<View> dependentView;
    private int offset;
    private int endOffset;
    private int startOffset;
    private OverScroller scroller;
    private Handler handler;
    private Toolbar mToolbar;
    private boolean isScrolling;

    public ToolBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new OverScroller(context);
        handler = new Handler();
    }

    private float getDependentViewCollapsedHeight() {
        return getDependentView().getResources().getDimension(R.dimen.header_height);
    }

    private View getDependentView(){

        return dependentView.get();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (layoutParams != null && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT){
            child.layout(0,0,parent.getWidth(),parent.getHeight());
            return true;
        }

        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {

        if (dependency != null && dependency.getId() == R.id.main_vp){
            dependentView = new WeakReference<>(dependency);
            for (int i =0; i < parent.getChildCount(); i++){
                if (parent.getChildAt(i) instanceof  Toolbar){
                    mToolbar = (Toolbar) parent.getChildAt(i);
                }
            }
            return true;
        }

        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {

        Resources resources = getDependentView().getResources();
       // Log.e(TAG, "onDependentViewChanged: " + child.getTranslationY() );
        final float progress =
                Math.abs(dependency.getTranslationY() / dependency.getHeight());

        child.setTranslationY(dependency.getHeight() + dependency.getTranslationY());

        float scale = 1 + 0.4f * (1.f - progress);
        dependency.setScaleX(scale);
        dependency.setScaleY(scale);
        if (mToolbar != null){
            mToolbar.getBackground().setAlpha((int) progress);
        }
        return true;


    }



    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, RecyclerView child, View directTargetChild, View target, int nestedScrollAxes) {
        isScrolling = false;
        scroller.abortAnimation();
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll: " + "dy    " + dy );
        if (dy  < 0){
            return;
        }
        View dependentView = getDependentView();
        float transY = dependentView.getTranslationY()  - dy;
        if ( transY < 0 && - transY < getDependentViewCollapsedHeight()){
            dependentView.setTranslationY(transY);
            consumed[1] = dy;
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0){
            return;
        }
        View dependentView = getDependentView();
        float transY = dependentView.getTranslationY() - dyConsumed;
        if (transY < 0){
            dependentView.setTranslationY(transY);
        }

        Log.e(TAG, "onNestedScroll: " + "dyConsumed   " + dyConsumed );
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout,
                                    RecyclerView child, View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling: " + "velocityY  "  + velocityY);
        return onUseStopDragging(velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target) {
        if (!isScrolling){
            onUseStopDragging(800);
        }
    }

    private boolean onUseStopDragging(float velocityY){
        View dependentView = getDependentView();
        float transY = dependentView.getTranslationY();
        float minHeaderTranslate = - (dependentView.getY() + getDependentViewCollapsedHeight());
        if (transY == 0 || transY == -getDependentViewCollapsedHeight()){
            return false;
        }
        boolean status;
        if (Math.abs(velocityY) <= 800){
            if (Math.abs(transY) < Math.abs(transY - minHeaderTranslate)){
                status = false;
            }else {
                status = true;
            }
            velocityY = 800;
        }else {
            if (velocityY > 0){
                status = true;
            }else {
                status = false;
            }
        }
        float targetTransY = status ? minHeaderTranslate : -dependentView.getY();
        scroller.startScroll(0,(int) transY,0,(int) targetTransY, (int) ( 10000 / Math.abs(velocityY)));
        handler.post(flingRunnable);
        isScrolling = true;
        return status;
    }

    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()){
                getDependentView().setTranslationY(scroller.getCurrY());
                handler.post(this);
            }else {
                isScrolling = false;
            }
        }
    };
}
