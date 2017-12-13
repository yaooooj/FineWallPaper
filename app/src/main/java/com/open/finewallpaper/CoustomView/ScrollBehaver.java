package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.open.finewallpaper.R;

import java.lang.ref.WeakReference;

/**
 * Created by SEELE on 2017-12-13.
 */

public class ScrollBehaver extends CoordinatorLayout.Behavior {
    WeakReference<View> childView ;
    WeakReference<View> dependencyView;
    private int headerSize;
    private int minHeader;
    private boolean isScroll;
    public ScrollBehaver() {
        super();
    }

    public ScrollBehaver(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            minHeader = 50;
            child.setTranslationY(headerSize);
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        View view  = dependencyView.get();
        float translationY = child.getTranslationY();
        float min = minHeader*1.0f/headerSize;
        float pro = translationY / headerSize;
        View child1 = view.findViewById(R.id.heading_vp);
        child1.setPivotY(0);
        child1.setPivotX(0);
        View titleView = dependencyView.get().findViewById(R.id.viewa_tb);
        titleView.setPivotX(0);
        titleView.setPivotY(0);
        titleView.setAlpha(1 - pro);
        if (pro  <= min + 1){
            titleView.setAlpha(1);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        clearAnimation();
        isScroll = false;

    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }



    private void clearAnimation(){

    }
}
