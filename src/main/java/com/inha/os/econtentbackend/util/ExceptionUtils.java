package com.inha.os.econtentbackend.util;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.response.BaseResponse;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.exception.ErrorResponse;

public class ExceptionUtils {
    private static final Gson GSON = new Gson();

    public static String respondWithError(ResponseStatus status, String message) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(message)
                .build();
        BaseResponse baseResponse = BaseResponse.builder()
                .data(GSON.toJson(errorResponse))
                .status(ResponseStatus.ERROR)
                .role(null)
                .action(null)
                .entity(null)
                .build();
        return GSON.toJson(baseResponse);
    }
}
