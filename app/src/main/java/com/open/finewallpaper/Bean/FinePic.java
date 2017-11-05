package com.open.finewallpaper.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SEELE on 2017/11/4.
 */

public class FinePic extends BmobObject{
    private String  pic_url;
    private String pic_name;
    private String pic_type;
    private BmobFile pic;

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPic_name() {
        return pic_name;
    }

    public void setPic_name(String pic_name) {
        this.pic_name = pic_name;
    }

    public String getPic_type() {
        return pic_type;
    }

    public void setPic_type(String pic_type) {
        this.pic_type = pic_type;
    }

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }
}
