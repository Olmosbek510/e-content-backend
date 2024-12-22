package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.DeleteMajorRequestDto;
import com.inha.os.econtentbackend.dto.request.MajorCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.MajorDeleteRequestDto;
import com.inha.os.econtentbackend.dto.request.MajorUpdateRequestDto;
import com.inha.os.econtentbackend.dto.response.*;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.*;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.MajorService;
import com.inha.os.econtentbackend.util.JsonUtil;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MajorActionDispatcher {
    private final AuthService authService;
    private final MajorService majorService;
    private final JwtUtil jwtUtil;
    private final Gson gson;

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, StudentAlreadyExistsException, UserAlreadyExistsException, UserNotFoundException, StudentNotFoundException, MajorNotFoundException, MajorNameAlreadyExistsException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (action.equals(Actions.Majors.GET_MAJORS) && authService.canPerformAction(action, token)) {
            return handleGetMajors(token);
        } else if (action.equals(Actions.Majors.ADD_MAJOR) && authService.canPerformAction(action, token)) {
            return handleAddMajor(dataNode, token);
        } else if (action.equals(Actions.Majors.UPDATE_MAJOR) && authService.canPerformAction(action, token)) {
            return handleUpdateMajor(dataNode, token);
        } else if (action.equals(Actions.Majors.GET_MAJOR_NAMES) && authService.canPerformAction(action, token)) {
            return handleGetMajorNames(token);
        } else if (action.equals(Actions.Majors.DELETE_MAJOR) && authService.canPerformAction(action, token)) {
            return handleDeleteMajor(dataNode, token);
        }
        return "Unknown user action: " + action;
    }

    private String handleDeleteMajor(String dataNode, String token) throws MajorNotFoundException {
        MajorDeleteRequestDto deleteRequestDto = JsonUtil.getObject(MajorDeleteRequestDto.class, dataNode);
        MajorDeleteResponseDto majorDeleteResponseDto = majorService.deleteMajor(deleteRequestDto);

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();
        BaseResponse baseResponse = BaseResponse.builder()
                .data(gson.toJson(majorDeleteResponseDto))
                .status(ResponseStatus.SUCCESS)
                .entity(Entity.MAJOR)
                .action(Actions.Majors.DELETE_MAJOR)
                .build();
        optionalRole.ifPresent(baseResponse::setRole);
        return gson.toJson(baseResponse);
    }

    private String handleGetMajorNames(String token) {
        List<MajorNameResponseDto> majorNames = majorService.getMajorNames();

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse baseResponse = BaseResponse.builder().data(gson.toJson(majorNames)).status(ResponseStatus.SUCCESS).action(Actions.Majors.GET_MAJOR_NAMES).entity(Entity.MAJOR).build();

        optionalRole.ifPresent(baseResponse::setRole);
        return gson.toJson(baseResponse);
    }

    private String handleUpdateMajor(String dataNode, String token) throws MajorNotFoundException, MajorNameAlreadyExistsException {
        MajorUpdateRequestDto majorUpdateRequestDto = JsonUtil.getObject(MajorUpdateRequestDto.class, dataNode);
        MajorUpdateResponseDto majorUpdateResponseDto = majorService.updateMajor(majorUpdateRequestDto);
        Set<String> roles = jwtUtil.extractRoles(token);

        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse baseResponse = BaseResponse.builder().data(gson.toJson(majorUpdateResponseDto)).status(ResponseStatus.SUCCESS).action(Actions.Majors.UPDATE_MAJOR).build();
        optionalRole.ifPresent(baseResponse::setRole);
        return gson.toJson(baseResponse);
    }

    private String handleAddMajor(String dataNode, String token) {
        MajorCreateRequestDto majorCreateDto = JsonUtil.getObject(MajorCreateRequestDto.class, dataNode);
        MajorCreateResponseDto major = majorService.createMajor(majorCreateDto);
        Set<String> roles = jwtUtil.extractRoles(token);

        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse response = BaseResponse.builder().entity(Entity.MAJOR).action(Actions.Majors.ADD_MAJOR).data(gson.toJson(major)).status(ResponseStatus.SUCCESS).build();
        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }

    private String handleGetMajors(String token) {
        Set<String> roles = jwtUtil.extractRoles(token);
        var majors = majorService.getMajors();

        BaseResponse response = BaseResponse.builder().action(Actions.Majors.GET_MAJORS).data(gson.toJson(majors)).status(ResponseStatus.SUCCESS).entity(Entity.MAJOR).build();

        Optional<String> optionalRole = roles.stream().findFirst();
        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }
}
