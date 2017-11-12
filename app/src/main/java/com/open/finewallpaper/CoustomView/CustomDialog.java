package com.open.finewallpaper.CoustomView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.open.finewallpaper.R;

/**
 * Created by SEELE on 2017/11/12.
 */

public class CustomDialog extends ProgressDialog {
    private String messaage = "";

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme, String message) {
        super(context, theme);
        this.messaage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);

        //点击物理按键消失，点击屏幕不会消失
        this.setCanceledOnTouchOutside(false);

        //点击物理按键和屏幕都不会消失
        //this.setCancelable(false);
        TextView textView = (TextView) findViewById(R.id.progress_tv);

        if (messaage != null){
           textView.setText(messaage);
        }
    }
}
