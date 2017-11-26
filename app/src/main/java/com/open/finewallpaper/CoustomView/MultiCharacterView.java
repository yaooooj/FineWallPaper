package com.open.finewallpaper.CoustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.open.finewallpaper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/11/26.
 */

public class MultiCharacterView extends LinearLayout {
    private final static String TAG = "MultiCharacterView";
    private Context mContext;
    private List<String> mTarget = new ArrayList<>();
    private List<CharacterView> mViews = new ArrayList<>();
    private OnFreshListener mOnFreshListener;

    private int textSize;
    public MultiCharacterView(Context context) {
        super(context);
        mContext = context;
    }

    public MultiCharacterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MultiCharacterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiCharacterView);
        textSize = typedArray.getInteger(R.styleable.MultiCharacterView_textSize,130);
        //spiteCharacter("Loading");
        typedArray.recycle();

        setOrientation(HORIZONTAL);

        setGravity(Gravity.CENTER);
    }


    public void setText(String strings){
        if (TextUtils.isEmpty(strings)){
            throw new IllegalArgumentException("text can not be null");
        }
        if (strings.length() > 20){
            throw new IllegalArgumentException("length more than 20");
        }
        spiteCharacter(strings);
    }


    private void spiteCharacter(String strings){
        int times = 5;
        String str = String.valueOf(strings);
        Log.e(TAG, "spiteCharacter: " + str );
        char[] charArray = str.toCharArray();
        for (int i = 0;i < charArray.length;i++){
            Log.e(TAG, "spiteCharacter: " + charArray[i] );
            CharacterView characterView = new CharacterView(mContext);
            characterView.setCharacter(str.charAt(i),times);
            mViews.add(characterView);
            addView(characterView);
            times += 1;
        }
        mViews.get(mViews.size()-1).setOnFinish(new CharacterView.OnFinish() {
            @Override
            public void finished() {
                if (mOnFreshListener != null){
                    mOnFreshListener.onFresh();
                }
            }
        });
    }


    public void setOnFreshListener(OnFreshListener onFreshListener) {
        mOnFreshListener = onFreshListener;
    }

    public interface OnFreshListener{

        void onFresh();
    }
}
