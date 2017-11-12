package com.open.finewallpaper.Bean.APIBean;

import java.util.List;

/**
 * Created by yaojian on 2017/7/23.
 */

public class PictureContentList {
    private String typeName;
    private String title;
    private List<PictureList> list;
    private String itemId;
    private String type;
    private String ct;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<PictureList> getLists() {
        return list;
    }

    public void setLists(List<PictureList> lists) {
        this.list = lists;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }
}
