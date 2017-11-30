package com.open.finewallpaper.Bean;

import android.widget.ImageView;

/**
 * Created by SEELE on 2017-11-30.
 */

public class HeadingBean {
    private String text;
    private int  imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
