package com.open.finewallpaper.Util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.open.finewallpaper.Bean.NameBean;
import com.open.finewallpaper.R;

import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by yaojian on 2017/10/26.
 */

public class RvDecoration extends RecyclerView.ItemDecoration {


    private static final String TAG = "RvDecoration";
    //private List<NameBean> data;
    private TextPaint textPaint;
    private Paint.FontMetrics fontMetrics;
    private Paint paint;
    private int bottomGap;
    private Paint paint1;

    private int leftGap;
    private int rightGap;
    private DecorationCallBack call;

    public RvDecoration(Context context){
        this(context,null);
    }

    public RvDecoration(Context context,DecorationCallBack call) {
        //this.data = data;
        this.call = call;

        Resources res = context.getResources();

        paint = new Paint();
        paint.setColor(Color.WHITE);

        paint1 = new Paint();
        paint1.setColor(Color.GRAY);

        fontMetrics = new Paint.FontMetrics();
        textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(50);
        textPaint.setColor(Color.BLACK);
        textPaint.getFontMetrics(fontMetrics);
        textPaint.setTextAlign(Paint.Align.LEFT);
        bottomGap = res.getDimensionPixelSize(R.dimen.sectioned_top);

        leftGap = res.getDimensionPixelSize(R.dimen.leftGap);
        rightGap = res.getDimensionPixelSize(R.dimen.rightGap);

    }



    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();


        if (layoutManager instanceof StaggeredGridLayoutManager){

            for (int i = 0;i < childCount; i++){
                final View view = parent.getChildAt(i);
                int childPosition = parent.getChildAdapterPosition(view);
                Log.e(TAG, "onDraw: "  + childPosition);

                String textLine = call.getGroupFirstLine(childPosition);
                if (TextUtils.isEmpty(textLine)){
                    final int top = view.getBottom();
                    final int bottom = view.getBottom() + bottomGap;
                    c.drawRect(25,top,right,bottom,paint);
                }else{
                    if (childPosition == 1 || isFirstInGroup(childPosition)){
                        final int top = view.getBottom();
                        final int bottom = view.getBottom() + bottomGap;
                        c.drawRect(25,top,right - 25,bottom,paint);
                        c.drawRect(10,top + 5,25,bottom - 5,paint1);
                        c.drawText(textLine,35,top + (bottom - top)/2 + 15,textPaint);
                    }
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int dividerHeight = 10;

        int childPosition = parent.getChildAdapterPosition(view);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if ( layoutManager instanceof GridLayoutManager){
            int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            Log.e(TAG, "getItemOffsets: " + spanCount +1 );
            if (childPosition % spanCount == 0){
                outRect.bottom = 10;
            }else {
                outRect.left = 10;
                outRect.bottom = 10;
            }

        }else if (layoutManager instanceof StaggeredGridLayoutManager){
            int spanCount = ((StaggeredGridLayoutManager)layoutManager).getSpanCount();
            if (childPosition == 1){
                outRect.top = bottomGap;
            }else if ( isFirstInGroup(childPosition)){
                outRect.set(0,bottomGap,view.getWidth()  * spanCount,0);
            }else if (childPosition == 0){
               outRect.top = 0;
            }else {
                outRect.top = 0;
            }
        }else {

                if (childPosition == 0){
                    outRect.set(0,0,0,0);
                }else {
                    outRect.set(leftGap,0,rightGap,dividerHeight);
                }



        }
    }

    private boolean isFirstInGroup(int pos){
        if (pos == 1){
            return true;
        }else if (pos == 0){
            return  false;
        }else {
            String preGroupId = call.getGroupId(pos-1);
            String groupId = call.getGroupId(pos);
            return !preGroupId .equals( groupId);
        }
    }

    public interface DecorationCallBack{
        String getGroupId(int position);

        String getGroupFirstLine(int position);
    }
}
