package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.exception.ErrorResponse;
import com.inha.os.econtentbackend.exception.exception.BaseException;
import com.inha.os.econtentbackend.exception.exception.InvalidActionTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CentralizedDispatcher {
    private final Gson gson;
    private final StudentActionDispatcher studentActionDispatcher;

    /**
     * Dispatches the request based on the entity and action.
     *
     * @param entity   The entity type.
     * @param action   The action to perform.
     * @param dataNode The JSON node containing data.
     * @param token
     * @return The response message or error response.
     */
    public String dispatch(String entity, String action, String dataNode, String token) {
        try {
            switch (entity.toUpperCase()) {
                case "STUDENT":
                    return studentActionDispatcher.dispatch(action, dataNode, token);
                default:
                    throw new InvalidActionTypeException("Unknown entity: " + entity);
            }
        } catch (BaseException e) {
            ErrorResponse errorResponse = (ErrorResponse) ErrorResponse.builder()
                    .message(e.getMessage())
                    .status("ERROR")
                    .build();
            try {
                return gson.toJson(errorResponse);
            } catch (Exception jsonException) {
                return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\",\"errorCode\":\"" + e.getClass().getSimpleName() + "\"}";
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message(e.getMessage())
                    .status("ERROR")
                    .build();
            try {
                return gson.toJson(errorResponse);
            } catch (Exception jsonException) {
                return "{\"status\":\"error\",\"message\":\"Internal server error.\",\"errorCode\":\"InternalError\"}";
            }
        }
    }
}
