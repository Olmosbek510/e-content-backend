package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.LoginRequestDto;
import com.inha.os.econtentbackend.dto.response.BaseResponse;
import com.inha.os.econtentbackend.dto.response.LoginResponseDto;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.InvalidCredentialsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.util.ExceptionUtils;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationDispatcher {
    private final Gson gson;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public String dispatch(String action, String dataNode, String token) throws UserNotFoundException, InvalidCredentialsException {
        if (action.equalsIgnoreCase(Actions.Authorization.LOGIN)) {
            return handleLogin(dataNode);
        }
        return ExceptionUtils.respondWithError(ResponseStatus.ERROR, "invalid action '%s' type".formatted(action));
    }

    private String handleLogin(String dataNode) throws UserNotFoundException, InvalidCredentialsException {
        LoginRequestDto loginRequestDto = gson.fromJson(dataNode, LoginRequestDto.class);
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        Set<String> roles = jwtUtil.extractRoles(loginResponseDto.getAccessToken());
        Optional<String> optionalRole = roles.stream().findFirst();
        BaseResponse response = BaseResponse.builder().status(ResponseStatus.SUCCESS).entity(Entity.AUTH).action(Actions.Authorization.LOGIN).data(gson.toJson(loginResponseDto)).build();
        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }
}
