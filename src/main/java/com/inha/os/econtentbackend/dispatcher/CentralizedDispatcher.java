package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.BookNotFoundException;
import com.inha.os.econtentbackend.exception.InvalidEntityException;
import com.inha.os.econtentbackend.exception.BaseException;
import com.inha.os.econtentbackend.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CentralizedDispatcher {
    private final Gson gson;
    private final StudentActionDispatcher studentActionDispatcher;
    private final AuthorizationDispatcher authorizationDispatcher;
    private final MajorActionDispatcher majorActionDispatcher;
    private final SubjectActionDispatcher subjectActionDispatcher;
    private final SystemAdminDispatcher systemAdminDispatcher;
    private final BookDispatcher bookDispatcher;
    private final DashboardDispatcher dashboardDispatcher;
    private final ArticleDispatcher articleDispatcher;
    private final ELetterDispatcher eLetterDispatcher;

    /**
     * Dispatches the request based on the entity and action.
     *
     * @param entity   The entity type.
     * @param action   The action to perform.
     * @param dataNode The JSON node containing data.
     * @param token
     * @return The response message or error response.
     */
    public String dispatch(String entity, String action, String dataNode, String token) throws BookNotFoundException {
        try {
            if (entity.equalsIgnoreCase(Entity.AUTH)) {
                return authorizationDispatcher.dispatch(action, dataNode, token);
            } else if (entity.equalsIgnoreCase(Entity.STUDENT)) {
                return studentActionDispatcher.dispatch(action, dataNode, token);
            } else if (entity.equalsIgnoreCase(Entity.MAJOR)) {
                return majorActionDispatcher.dispatch(action, dataNode, token);
            } else if (entity.equalsIgnoreCase(Entity.SUBJECT)) {
                return subjectActionDispatcher.dispatch(action, dataNode, token);
            } else if (entity.equalsIgnoreCase(Entity.BOOK)) {
                return bookDispatcher.dispatch(action, dataNode, token);
            } else if (entity.equalsIgnoreCase(Entity.SYSTEM_ADMIN)) {
                return systemAdminDispatcher.dispatch(action, dataNode, token);
            } else if (entity.equalsIgnoreCase(Entity.STATISTICS_DASHBOARD)) {
                return dashboardDispatcher.dispatch(action, dataNode, token);
            } else if (entity.equalsIgnoreCase(Entity.ARTICLE)) {
                return articleDispatcher.dispatch(action, dataNode, token);
            } else if (entity.equalsIgnoreCase(Entity.E_LETTER)) {
                return eLetterDispatcher.dispatch(action, dataNode, token);
            } else {
                throw new InvalidEntityException("entity '%s' is invalid".formatted(entity));
            }
        } catch (BaseException e) {
            e.printStackTrace();
            try {
                return ExceptionUtils.respondWithError(ResponseStatus.ERROR, e.getMessage());
            } catch (Exception jsonException) {
                jsonException.printStackTrace();
                return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\",\"errorCode\":\"" + e.getClass().getSimpleName() + "\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return ExceptionUtils.respondWithError(ResponseStatus.ERROR, e.getMessage());
            } catch (Exception jsonException) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"message\":\"Internal server error.\",\"errorCode\":\"InternalError\"}";
            }
        }
    }
}
