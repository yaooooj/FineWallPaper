package com.open.finewallpaper.Bean.APIBean;

/**
 * Created by yaojian on 2017/7/23.
 */

public class PictureBody {
    private String ret_code;
    private PicturePageBean pagebean;

    public  String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public PicturePageBean getPagebean() {
        return pagebean;
    }

    public void setPagebean(PicturePageBean pagebean) {
        this.pagebean = pagebean;
    }
}
