package com.open.finewallpaper.HTTP;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by SEELE on 2017/11/12.
 */

public class GsonUtil {
    private static Gson gson = new Gson();

    public static <T> T  phraseJsonWithGson(String jsonData,Class<T> type ) throws JsonSyntaxException{

        return gson.fromJson(jsonData,type);
    }

    /**
     * 将json字符串转换为对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Type type) throws JsonSyntaxException {
        return gson.fromJson(json, type);
    }

    /**
     * 将json对象转换为实体对象
     * @param json
     * @param clz
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T deserialize(JsonObject json, Class<T> clz) throws JsonSyntaxException {
        return gson.fromJson(json, clz);
    }

    /**
     * 将json字符串转换为对象
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Class<T> clz) throws JsonSyntaxException {
        return gson.fromJson(json, clz);
    }

    /**
     * 将对象准换为json字符串
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String serialize(T object) {
        return gson.toJson(object);
    }
}
