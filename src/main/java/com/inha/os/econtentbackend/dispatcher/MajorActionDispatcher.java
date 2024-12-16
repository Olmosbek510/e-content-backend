package com.inha.os.econtentbackend.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.response.MajorsGetResponseDto;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.StudentNotFoundException;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.MajorService;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MajorActionDispatcher {

    private final AuthService authService;
    private final MajorService majorService;
    private final JwtUtil jwtUtil;
    private final Gson gson;

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, StudentAlreadyExistsException, UserAlreadyExistsException, UserNotFoundException, StudentNotFoundException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (action.equals(Actions.Majors.GET_MAJORS) && authService.canPerformAction(action, token)) {
            return handleGetMajors(token);
        }
        return "Unknown user action: " + action;
    }

    private String handleGetMajors(String token) {
        Set<String> roles = jwtUtil.extractRoles(token);
        MajorsGetResponseDto majors = majorService.getMajors();
        Optional<String> first = roles.stream().findFirst();
        first.ifPresent(majors::setRoleName);
        return gson.toJson(majors);
    }
}
