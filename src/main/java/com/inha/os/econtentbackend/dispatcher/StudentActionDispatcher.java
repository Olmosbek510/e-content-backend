package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.dto.response.StudentCreateResponseDto;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.service.AuthenticationService;
import com.inha.os.econtentbackend.service.StudentService;
import com.inha.os.econtentbackend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class StudentActionDispatcher {
    private final StudentService studentService;
    private final Gson gson;
    private final AuthenticationService authenticationService;

    /**
     * Dispatches the request based on the action.
     *
     * @param action   The action to perform.
     * @param dataNode The JSON node containing data.
     * @return The response message.
     */
    public String dispatch(String action, String dataNode, String token) throws
            AccessDeniedException,
            StudentAlreadyExistsException,
            UserAlreadyExistsException {
        if (token != null && !authenticationService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (token == null && action.equals("CREATE_STUDENT")) {
            return handleCreateStudent(dataNode);
        }

        return "Unknown user action: " + action;
    }

    private String handleCreateStudent(String dataNode) throws StudentAlreadyExistsException, UserAlreadyExistsException {
        StudentCreateDto studentCreateDto = JsonUtil.getObject(StudentCreateDto.class, dataNode);
        StudentCreateResponseDto student = studentService.createStudent(studentCreateDto);
        return gson.toJson(student);
    }
}
