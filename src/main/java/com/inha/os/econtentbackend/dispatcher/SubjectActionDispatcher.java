package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.SubjectCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.SubjectGetNamesRequestDto;
import com.inha.os.econtentbackend.dto.request.SubjectGetRequestDto;
import com.inha.os.econtentbackend.dto.request.SubjectUpdateRequestDto;
import com.inha.os.econtentbackend.dto.response.BaseResponse;
import com.inha.os.econtentbackend.dto.response.SubjectCreateResponseDto;
import com.inha.os.econtentbackend.dto.response.SubjectNameResponseDto;
import com.inha.os.econtentbackend.dto.response.SubjectsGetResponseDto;
import com.inha.os.econtentbackend.entity.Major;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.MajorNotFoundException;
import com.inha.os.econtentbackend.exception.SubjectAlreadyExistsException;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionTcp;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.MajorService;
import com.inha.os.econtentbackend.service.SubjectService;
import com.inha.os.econtentbackend.util.JsonUtil;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.representer.BaseRepresenter;

import java.nio.file.AccessDeniedException;
import java.util.List;
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

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, MajorNotFoundException, SubjectAlreadyExistsException, SubjectNotFoundExceptionTcp {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Subject.GET_SUBJECTS)) {
            return handleGetSubjects(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Subject.ADD_SUBJECT)) {
            return handleCreateSubject(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Subject.GET_SUBJECT_NAMES)) {
            return handleGetSubjectNames(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Subject.UPDATE_SUBJECT)) {
            return handleUpdateSubject(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Subject.DELETE_SUBJECT)) {
            return handleDeleteSubject(dataNode, token);
        }
        return "Unknown user action: " + action;
    }

    private String handleDeleteSubject(String dataNode, String token) throws SubjectNotFoundExceptionTcp {
        SubjectUpdateRequestDto requestDto = JsonUtil.getObject(SubjectUpdateRequestDto.class, dataNode);
        subjectService.deleteById(requestDto.getSubjectId());

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse baseResponse = BaseResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .entity(Entity.SUBJECT)
                .action(Actions.Subject.DELETE_SUBJECT)
                .build();

        optionalRole.ifPresent(baseResponse::setRole);
        return gson.toJson(baseResponse);
    }

    private String handleUpdateSubject(String dataNode, String token) throws SubjectAlreadyExistsException, SubjectNotFoundExceptionTcp {
        SubjectUpdateRequestDto requestDto = JsonUtil.getObject(SubjectUpdateRequestDto.class, dataNode);
        subjectService.updateSubject(requestDto);

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse response = BaseResponse.builder()
                .action(Actions.Subject.UPDATE_SUBJECT)
                .entity(Entity.SUBJECT)
                .status(ResponseStatus.SUCCESS)
                .build();
        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }

    private String handleGetSubjectNames(String dataNode, String token) {
        SubjectGetNamesRequestDto subjectGetNamesRequestDto = JsonUtil.getObject(SubjectGetNamesRequestDto.class, dataNode);
        List<SubjectNameResponseDto> subjectNames = subjectService.getSubjectNames(subjectGetNamesRequestDto);

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse baseResponse = BaseResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .data(gson.toJson(subjectNames))
                .action(Actions.Subject.GET_SUBJECT_NAMES)
                .build();

        optionalRole.ifPresent(baseResponse::setRole);
        return gson.toJson(baseResponse);
    }

    private String handleCreateSubject(String dataNode, String token) throws SubjectAlreadyExistsException, MajorNotFoundException {
        SubjectCreateRequestDto requestDto = JsonUtil.getObject(SubjectCreateRequestDto.class, dataNode);
        SubjectCreateResponseDto subject = subjectService.createSubject(requestDto);

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse response = BaseResponse.builder()
                .entity(Entity.BOOK)
                .action(Actions.Subject.ADD_SUBJECT)
                .data(gson.toJson(subject))
                .status(ResponseStatus.SUCCESS)
                .build();
        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }

    private String handleGetSubjects(String dataNode, String token) throws MajorNotFoundException {
        SubjectGetRequestDto getRequestDto = JsonUtil.getObject(SubjectGetRequestDto.class, dataNode);
        Set<String> roles = jwtUtil.extractRoles(token);
        Major major = majorService.getById(getRequestDto.getMajorId());

        var subjects = subjectService.getSubjects(getRequestDto.getMajorId());

//        SubjectsGetResponseDto responseDto = SubjectsGetResponseDto.builder()
//                .majorName(major.getName())
//                .majorId(major.getId())
//                .subjects(subjects)
//                .build();

        Optional<String> role = roles.stream().findFirst();

        BaseResponse baseResponse = BaseResponse.builder()
                .action(Actions.Subject.GET_SUBJECTS)
                .data(gson.toJson(subjects))
                .status(ResponseStatus.SUCCESS)
                .build();
        role.ifPresent(baseResponse::setRole);

        return gson.toJson(baseResponse);
    }
}
