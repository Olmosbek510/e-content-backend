package com.inha.os.econtentbackend.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.StudentNotFoundException;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class MajorActionDispatcher {

    private final AuthService authService;
    private final MajorService majorService;

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, StudentAlreadyExistsException, UserAlreadyExistsException, JsonProcessingException, RoleNotFoundException, UserNotFoundException, StudentNotFoundException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (action.equals(Actions.Majors.GET_MAJORS)) {
            return handleGetMajors();
        }
        return "Unknown user action: " + action;
    }

    private String handleGetMajors() {
        return "majorService.getMajors();";
    }
}
