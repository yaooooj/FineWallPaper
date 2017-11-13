package com.open.finewallpaper.HTTP;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.google.gson.internal.$Gson$Types;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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


    private OkHttpClient mOkHttpClient;
    private static OkHttpUtil mOkHttpUtil;
    private Context context;
    private Handler handler;

    private OkHttpUtil(Context context) {
        this.context = context;
        File httpCacheDirectory  = new File(context.getCacheDir(),"mOkHttpResponse");
        Cache cache = new Cache(httpCacheDirectory,10 * 1024 * 1024);

        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
        clientBuilder.readTimeout(30,TimeUnit.SECONDS);
        clientBuilder.connectTimeout(10,TimeUnit.SECONDS);
        clientBuilder.writeTimeout(60,TimeUnit.SECONDS);
        clientBuilder.cache(cache);
        clientBuilder.addInterceptor(CacheIntercept());
        mOkHttpClient = clientBuilder.build();
        handler = new Handler(Looper.getMainLooper());
    }

    private static OkHttpUtil getInstance(Context context){
        if (mOkHttpUtil == null){
            mOkHttpUtil = new OkHttpUtil(context);
        }
        return mOkHttpUtil;
    }

    private void getRequest(String url,final ResultCallback callback) {
        //设置缓存最大保存时间
        final CacheControl cacheControl1 = new CacheControl.Builder()
                .maxAge(9600, TimeUnit.MILLISECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .cacheControl(cacheControl1)
                .build();

        deliveryResult(callback, request);
    }

    private void deliveryResult(final ResultCallback callback, Request request) {

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailCallback(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);

                try {
                    String str = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessCallBack(callback, str);
                    } else {

                        Object object = GsonUtil.deserialize(str,callback.mType);
                        sendSuccessCallBack(callback, object);
                    }
                } catch (Exception e) {

                    sendFailCallback(callback, e);
                }


            }
        });
    }


    private void sendFailCallback(final ResultCallback callback, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    private void sendSuccessCallBack(final ResultCallback callback, final Object obj) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(obj);
                }
            }
        });
    }



    private Interceptor CacheIntercept(){
        return  new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                boolean connected = NetWorkUtils.isNetworkConnected(context);
                if (!connected){
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                }
                return chain.proceed(request);
            }
        };
    }

    /**********************对外接口************************/

    /**
     * get请求
     * @param url  请求url
     * @param callback  请求回调
     */
    public static void get(Context context,String url, ResultCallback callback) {
        getInstance(context).getRequest(url, callback);
    }


    public static abstract class ResultCallback<T> {

        Type mType;

        public ResultCallback(){
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         * @param response
         */
        public abstract void onSuccess(T response);

        /**
         * 请求失败回调
         * @param e
         */
        public abstract void onFailure(Exception e);
    }

}
