package com.open.finewallpaper.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SEELE on 2017/11/8.
 */

public class ItemBean implements Parcelable {
    private String imgType;
    private boolean isMore;
    private ImageBean imgBean;

    public ItemBean(){

    }

    protected ItemBean(Parcel in) {
        imgType = in.readString();
        isMore = in.readByte() != 0;
    }

    public static final Creator<ItemBean> CREATOR = new Creator<ItemBean>() {
        @Override
        public ItemBean createFromParcel(Parcel in) {
            return new ItemBean(in);
        }

        @Override
        public ItemBean[] newArray(int size) {
            return new ItemBean[size];
        }
    };

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public ImageBean getImgBean() {
        return imgBean;
    }

    public void setImgBean(ImageBean imgBean) {
        this.imgBean = imgBean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgType);
        dest.writeByte((byte) (isMore ? 1 : 0));
    }
}
