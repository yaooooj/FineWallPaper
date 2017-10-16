package com.open.finewallpaper.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SEELE on 2017/10/15.
 */

public class PictureBean extends BmobObject  {


    private String url;
    private BmobFile wrapper;
    private String picturename;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BmobFile getWrapper() {
        return wrapper;
    }

    public void setWrapper(BmobFile wrapper) {
        this.wrapper = wrapper;
    }

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }


}
