package com.open.finewallpaper.HTTP;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import com.open.finewallpaper.Bean.APIBean.PictureBean;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by SEELE on 2017/11/12.
 */

public class OkHttpUtil {

    private static final String TAG = "OkHttp3Util";
    public static final String showapi_appid = "42731";
    public static final String showapi_sign = "96039fbf84ee42afaad5d66f14159c31";

    private final static String url = "http://route.showapi.com/852-2?showapi_appid=myappid&type=&page=&showapi_sign=mysecret";

    private static OkHttpUtil mOkHttpUtil;

    private OkHttpUtil() {

    }

    public static OkHttpUtil newInstance(){
        if (mOkHttpUtil == null){
            mOkHttpUtil = new OkHttpUtil();
        }
        return mOkHttpUtil;
    }

    private Interceptor mInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response1 = chain.proceed(request);
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)){
                cacheControl = "public,max-age=60";
            }

            return response1.newBuilder()
                    .header("Cache-Control",cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
    };


    public void executeGet(Context context, int type, int page, final Class<?> claszz) {
        String url = "http://route.showapi.com/852-2?showapi_appid="+ showapi_appid +
                "&type=" + type+"&page="+ page +"&showapi_sign="+showapi_sign;

        File httpCacheDirectory  = new File(context.getCacheDir(),"mOkHttpResponse");


        Cache cache = new Cache(httpCacheDirectory,10 * 1024 * 1024);
        CacheControl cacheControl1 = new CacheControl.Builder()
                .maxAge(10, TimeUnit.MILLISECONDS)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        Request request2 = new Request.Builder()
                .url(url)
                .cacheControl(cacheControl1)
                .build();


        okHttpClient.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.e(TAG, "onFailure: " + "failure execute  request");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);

                if (response.code() != 504){

                    Object object;
                    if (response.code() == 200){
                        Log.e(TAG, "From NetWorkCacheControl: " + "NetWork NetWork NetWork NetWork NetWork ");
                        try {
                            object = GsonUtil.phraseJsonWithGson(response.body().string(),claszz);
                            PhraseUrl.phraserUrl((PictureBean) object);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }else {
                    Log.e(TAG, "onResponse: " + "The resource was not cached " );
                }
            }
        });
    }

}
