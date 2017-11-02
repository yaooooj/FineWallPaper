package com.open.finewallpaper.Util;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.open.finewallpaper.R;

import java.lang.ref.WeakReference;

/**
 * Created by SEELE on 2017/11/3.
 */

public class ToolBarBehavior extends CoordinatorLayout.Behavior<RecyclerView> {
    private static final String TAG = "ToolBarBehavior";
    private WeakReference<View> dependentView;

    public ToolBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {

        if (dependency != null && dependency.getId() == R.id.main_toolbar){
            dependentView = new WeakReference<>(dependency);
            return true;
        }

        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {

        Resources resources = getDependentView().getResources();
        Log.e(TAG, "onDependentViewChanged: " + child.getTranslationY() );
        final float progress = 1.f -
                Math.abs(child.getTranslationY() / (dependency.getHeight() - resources.getDimension(R.dimen.header_height)));

        child.setTranslationY(dependency.getHeight() + dependency.getTranslationY());

        //float scale = 1 + 0.4f * (1.f - progress);
        ///dependency.setScaleX(scale);
       // dependency.setScaleY(scale);

        dependency.getBackground().setAlpha((int) progress);

        return true;


    }

    private float getDependentViewCollapsedHeight() {
        return getDependentView().getResources().getDimension(R.dimen.header_height);
    }



    public View getDependentView(){

        return dependentView.get();
    }
}
