package com.inha.os.econtentbackend.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.dto.request.StudentProfileRequestDto;
import com.inha.os.econtentbackend.dto.response.StudentCreateResponseDto;
import com.inha.os.econtentbackend.dto.response.StudentProfileDto;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.StudentNotFoundException;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.MajorService;
import com.inha.os.econtentbackend.service.StudentService;
import com.inha.os.econtentbackend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleNotFoundException;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class StudentActionDispatcher {
    private final StudentService studentService;
    private final Gson gson;
    private final AuthService authService;

    /**
     * Dispatches the request based on the action.
     *
     * @param action   The action to perform.
     * @param dataNode The JSON node containing data.
     * @return The response message.
     */
    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, StudentAlreadyExistsException, UserAlreadyExistsException, JsonProcessingException, RoleNotFoundException, UserNotFoundException, StudentNotFoundException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (token == null && action.equals(Actions.Student.CREATE_STUDENT)) {
            return handleCreateStudent(dataNode);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Student.GET_PROFILE)) {
            StudentProfileRequestDto requestDto = JsonUtil.getObject(StudentProfileRequestDto.class, dataNode);
            authService.validate(requestDto.getEmail(), token);
            return handleGetProfile(requestDto.getEmail());
        }
        return "Unknown user action: " + action;
    }

    private String handleGetProfile(String email) throws UserNotFoundException, StudentNotFoundException {
        StudentProfileDto profile = studentService.getProfile(email);
        return gson.toJson(profile);
    }

    private String handleCreateStudent(String dataNode) throws StudentAlreadyExistsException, UserAlreadyExistsException, JsonProcessingException, RoleNotFoundException {
        StudentCreateDto studentCreateDto = JsonUtil.getObject(StudentCreateDto.class, dataNode);
        StudentCreateResponseDto student = studentService.createStudent(studentCreateDto);
        return gson.toJson(student);
    }
}
