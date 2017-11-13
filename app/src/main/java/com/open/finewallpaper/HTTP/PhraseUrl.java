package com.open.finewallpaper.HTTP;

import com.open.finewallpaper.Bean.APIBean.PictureBeanAPI;
import com.open.finewallpaper.Bean.APIBean.PictureBody;
import com.open.finewallpaper.Bean.APIBean.PictureContentList;
import com.open.finewallpaper.Bean.APIBean.PictureList;
import com.open.finewallpaper.Bean.APIBean.PicturePageBean;


import java.util.List;

/**
 * Created by SEELE on 2017/11/12.
 */

public class PhraseUrl {

    private static final String TAG = "PhraseUrl ";
    private static PictureBody pictureBody;
    private static PicturePageBean picturePageBean;
    private static PictureContentList pictureContentList;
    private static PictureList pictureList;
    private static List<String> bitmapList;


    public static List<String> phraseUrl(PictureBeanAPI pictureBeanAPI){
        if (pictureBeanAPI != null) {
            if (pictureBeanAPI.getShowapi_res_body() != null) {
                pictureBody = pictureBeanAPI.getShowapi_res_body();
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
            return  bitmapList;
        }
        return null;
    }


    public PictureBody getPictureBody() {
        return pictureBody;
    }

    public PicturePageBean getPicturePageBean() {
        return picturePageBean;
    }

    public PictureContentList getPictureContentList() {
        return pictureContentList;
    }

    public PictureList getPictureList() {
        return pictureList;
    }

    public List<String> getBitmapList() {
        return bitmapList;
    }

}
