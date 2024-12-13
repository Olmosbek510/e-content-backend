package com.inha.os.econtentbackend.util;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.exception.ErrorResponse;

public class ExceptionUtils {
    private static final Gson GSON = new Gson();
    public static String respondWithError(ResponseStatus status, String message) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .message(message)
                .build();
        return GSON.toJson(errorResponse);
    }
}
