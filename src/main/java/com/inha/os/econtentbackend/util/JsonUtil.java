package com.inha.os.econtentbackend.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

public class JsonUtil {
    private static final Gson GSON = new Gson();

    public static <T> T getObject(Class<T> clazz, String jsonString) {
        return GSON.fromJson(jsonString, clazz);
    }

}
