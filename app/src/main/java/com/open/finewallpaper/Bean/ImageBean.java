package com.open.finewallpaper.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SEELE on 2017/11/8.
 */

public class ImageBean implements Parcelable {
    private String imgName;
    private String imgUrl;

    public ImageBean(){

    }
    protected ImageBean(Parcel in) {
        imgName = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgName);
        dest.writeString(imgUrl);
    }
}
