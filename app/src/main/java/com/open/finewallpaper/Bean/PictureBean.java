package com.open.finewallpaper.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SEELE on 2017/10/15.
 */

public class PictureBean extends BmobObject {


    private String url;
    private BmobFile bitmap;
    private String picturename;

    public PictureBean() {
        this.setTableName("bitmap");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BmobFile getBitmap() {
        return bitmap;
    }

    public void setBitmap(BmobFile bitmap) {
        this.bitmap = bitmap;
    }

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }
}
