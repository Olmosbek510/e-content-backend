package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.response.BaseResponse;
import com.inha.os.econtentbackend.dto.response.BaseResponseDto;
import com.inha.os.econtentbackend.dto.response.StatisticsResponseDto;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.StudentNotFoundException;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.StatisticsService;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardDispatcher {
    private final AuthService authService;
    private final StatisticsService statisticsService;
    private final Gson gson;
    private final JwtUtil jwtUtil;

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (action.equals(Actions.Statistics.GET_STATISTICS) && authService.canPerformAction(action, token)) {
            return handleStatistics(token);
        }
        return "Unknown user action: " + action;
    }

    private String handleStatistics(String token) {
        StatisticsResponseDto statistics = statisticsService.getStatistics();
        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();
        BaseResponse response = BaseResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .data(gson.toJson(statistics))
                .action(Actions.Statistics.GET_STATISTICS)
                .entity(Entity.STATISTICS_DASHBOARD)
                .build();
        optionalRole.ifPresent(response::setRole);
        return gson.toJson(response);
    }

}
