package com.hjc.baselibrary.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Json解析
 */
public class JsonUtil {

    private JsonUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("JsonUtil cannot be instantiated");
    }

    private static final Gson GSON = new Gson();

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return GSON.fromJson(json, classOfT);
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            return GSON.fromJson(json, type);
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }

    public static <T> List<T> fromJsons(String json, Type type) {
        try {
            return GSON.fromJson(json, type);
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }
}