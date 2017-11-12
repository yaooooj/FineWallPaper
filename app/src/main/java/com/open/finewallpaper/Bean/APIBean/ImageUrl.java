package com.open.finewallpaper.Bean.APIBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/8/6.
 */

public class ImageUrl {
    private static final String TAG = "ImageUrl";
    private PictureBean pictureBean;
    private PictureBody pictureBody;
    private PicturePageBean picturePageBean;
    private PictureContentList pictureContentList;
    private PictureList pictureList;
    private List<String> bitmapList;

    public ImageUrl(PictureBean pictureBean) {
        this.pictureBean = pictureBean;
        bitmapList = new ArrayList<>();
        praserUrl();

    }

    public void praserUrl(){
        if (pictureBean != null) {
            if (pictureBean.getShowapi_res_body() != null) {
                pictureBody = pictureBean.getShowapi_res_body();
            }
            if (pictureBody.getPagebean() != null) {
                picturePageBean = pictureBody.getPagebean();
            }
            if (picturePageBean.getContentlist() != null) {
                for (int i = 0; i < picturePageBean.getContentlist().size(); i++) {
                    pictureContentList = picturePageBean.getContentlist().get(i);
                    if (pictureContentList.getLists() != null) {
                        for (int j = 0; j < pictureContentList.getLists().size(); j++) {
                            pictureList = pictureContentList.getLists().get(j);
                            bitmapList.add(pictureContentList.getLists().get(j).getBig());
                        }
                    }
                }
            }
        }
    }
    public PictureBean getPictureBean() {
        return pictureBean;
    }

    public void setPictureBean(PictureBean pictureBean) {
        this.pictureBean = pictureBean;
    }

    public PictureBody getPictureBody() {
        return pictureBody;
    }

    public void setPictureBody(PictureBody pictureBody) {
        this.pictureBody = pictureBody;
    }

    public PicturePageBean getPicturePageBean() {
        return picturePageBean;
    }

    public void setPicturePageBean(PicturePageBean picturePageBean) {
        this.picturePageBean = picturePageBean;
    }

    public PictureContentList getPictureContentList() {
        return pictureContentList;
    }

    public void setPictureContentList(PictureContentList pictureContentList) {
        this.pictureContentList = pictureContentList;
    }

    public PictureList getPictureList() {
        return pictureList;
    }

    public void setPictureList(PictureList pictureList) {
        this.pictureList = pictureList;
    }

    public List<String> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(List<String> bitmapList) {
        this.bitmapList = bitmapList;
    }
}
