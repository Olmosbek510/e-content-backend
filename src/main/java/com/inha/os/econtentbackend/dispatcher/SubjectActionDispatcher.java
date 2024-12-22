package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.SubjectCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.SubjectGetRequestDto;
import com.inha.os.econtentbackend.dto.response.BaseResponse;
import com.inha.os.econtentbackend.dto.response.SubjectsGetResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.exception.MajorNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.MajorService;
import com.inha.os.econtentbackend.service.SubjectService;
import com.inha.os.econtentbackend.util.JsonUtil;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubjectActionDispatcher {
    private final SubjectService subjectService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final MajorService majorService;
    private final Gson gson;

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, MajorNotFoundException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Subject.GET_SUBJECTS)) {
            return handleGetSubjects(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Subject.ADD_SUBJECT)) {
            return handleCreateSubject(dataNode, token);
        }
        return "Unknown user action: " + action;
    }

    private String handleCreateSubject(String dataNode, String token) {
        SubjectCreateRequestDto requestDto = JsonUtil.getObject(SubjectCreateRequestDto.class, dataNode);
        subjectService.createSubject(requestDto);
    }

    private String handleGetSubjects(String dataNode, String token) throws MajorNotFoundException {
        SubjectGetRequestDto getRequestDto = JsonUtil.getObject(SubjectGetRequestDto.class, dataNode);
        Set<String> roles = jwtUtil.extractRoles(token);
        Major major = majorService.getById(getRequestDto.getId());

        var subjects = subjectService.getSubjects(getRequestDto.getId());

        SubjectsGetResponseDto responseDto = SubjectsGetResponseDto.builder()
                .majorName(major.getName())
                .majorId(major.getId())
                .subjects(subjects)
                .build();

        Optional<String> role = roles.stream().findFirst();

        BaseResponse baseResponse = BaseResponse.builder()
                .action(Actions.Subject.GET_SUBJECTS)
                .data(gson.toJson(responseDto))
                .status(ResponseStatus.SUCCESS)
                .build();
        role.ifPresent(baseResponse::setRole);

        return gson.toJson(baseResponse);
    }
}
