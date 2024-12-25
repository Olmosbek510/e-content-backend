package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.ELetterCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.ELetterGetResponseDto;
import com.inha.os.econtentbackend.dto.request.ElettersGetRequestDto;
import com.inha.os.econtentbackend.dto.response.BaseResponse;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.*;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.ELetterService;
import com.inha.os.econtentbackend.util.JsonUtil;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ELetterDispatcher {
    private final AuthService authService;
    private final Gson gson;
    private final JwtUtil jwtUtil;
    private final ELetterService eLetterService;

    @Transactional
    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, StudentAlreadyExistsException, UserAlreadyExistsException, UserNotFoundException, StudentNotFoundException, MajorNotFoundException, MajorNameAlreadyExistsException, MajorAlreadyExistsException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.ELetter.CREATE_E_LETTER)) {
            return handleCreateELetter(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.ELetter.GET_E_LETTERS)) {
            return handleGetELetters(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.ELetter.DELETE_E_LETTER)) {
            return handleDeleteELetter(dataNode, token);
        }
        return "Unknown user action: " + action;
    }

    private String handleDeleteELetter(String dataNode, String token) {
        ELetterGetResponseDto requestDto = JsonUtil.getObject(ELetterGetResponseDto.class, dataNode);
        Map<String, String> data = new HashMap<>();
        String s = eLetterService.deleteById(requestDto.getELetterId());

        data.put("message", s);
        BaseResponse response = BaseResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .data(gson.toJson(data))
                .action(Actions.ELetter.DELETE_E_LETTER)
                .entity(Entity.E_LETTER).build();

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }

    @Transactional
    protected String handleGetELetters(String dataNode, String token) {
        ElettersGetRequestDto requestDto = JsonUtil.getObject(ElettersGetRequestDto.class, dataNode);
        List<ELetterGetResponseDto> eLetters = eLetterService.getELetters(requestDto);

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse response = BaseResponse.builder()
                .entity(Entity.E_LETTER)
                .action(Actions.ELetter.GET_E_LETTERS)
                .data(gson.toJson(eLetters))
                .status(ResponseStatus.SUCCESS)
                .build();

        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }

    private String handleCreateELetter(String dataNode, String token) {
        ELetterCreateRequestDto object = JsonUtil.getObject(ELetterCreateRequestDto.class, dataNode);
        Map<String, String> data = new HashMap<>();
        data.put("message", "e-letter created successfully");

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse response = BaseResponse.builder()
                .action(Actions.ELetter.CREATE_E_LETTER)
                .data(gson.toJson(data))
                .entity(Entity.E_LETTER)
                .status(ResponseStatus.SUCCESS)
                .build();

        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }
}
