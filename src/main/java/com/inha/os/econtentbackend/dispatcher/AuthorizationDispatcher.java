package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.LoginRequestDto;
import com.inha.os.econtentbackend.dto.response.LoginResponseDto;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.exception.InvalidCredentialsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationDispatcher {
    private final Gson gson;
    private final AuthService authService;

    public String dispatch(String action, String dataNode, String token) throws UserNotFoundException, InvalidCredentialsException {
        if (action.equalsIgnoreCase(Actions.Authorization.LOGIN)) {
            return handleLogin(dataNode);
        }
        return ExceptionUtils.respondWithError(ResponseStatus.ERROR, "invalid action '%s' type".formatted(action));
    }

    private String handleLogin(String dataNode) throws UserNotFoundException, InvalidCredentialsException {
        LoginRequestDto loginRequestDto = gson.fromJson(dataNode, LoginRequestDto.class);
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return gson.toJson(loginResponseDto);
    }
}
