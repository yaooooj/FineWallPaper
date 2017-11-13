package com.open.finewallpaper.HTTP;

/**
 * Created by yaojian on 2017/11/13.
 */

public class Urls {
    private static final String showapi_appid = "42731";
    private static final String showapi_sign = "96039fbf84ee42afaad5d66f14159c31";

    public static String Urls(int type, int page){

        return "http://route.showapi.com/852-2?showapi_appid="+ showapi_appid +
                "&type=" + type+"&page="+ page +"&showapi_sign="+showapi_sign;
    }
}
