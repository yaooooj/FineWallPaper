package com.open.finewallpaper.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yaojian on 2017/11/1.
 */

public class SetBean implements Parcelable{
    private String name;
    private String url;

    public SetBean(){

    }

    protected SetBean(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<SetBean> CREATOR = new Creator<SetBean>() {
        @Override
        public SetBean createFromParcel(Parcel in) {
            return new SetBean(in);
        }

        @Override
        public SetBean[] newArray(int size) {
            return new SetBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }
}
