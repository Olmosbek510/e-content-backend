package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.LoginRequestDto;
import com.inha.os.econtentbackend.dto.response.LoginResponseDto;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.exception.InvalidCredentialsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.UserService;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String authenticate(String username, String password) throws Exception {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (!matches) {
            throw new RuntimeException("Invalid username or password");
        }

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toSet());
        return jwtUtil.generateToken(username, roles);
    }

    @Override
    public boolean canPerformAction(String action, String token) {
        Set<String> roles = jwtUtil.extractRoles(token);
        return isAuthorizedForAction(action, roles);
    }

    @Override
    public boolean isAuthorizedForAction(String action, Set<String> roles) {
        if (action.startsWith(Actions.Student.GET_PROFILE)) {
            return roles.contains(RoleName.ROLE_STUDENT.name()) ||
                    roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name());

        } else if (action.startsWith(Actions.Majors.GET_MAJORS)) {
            return roles.contains(RoleName.ROLE_SYS_ADMIN.name()) ||
                    roles.contains(RoleName.ROLE_STUDENT.name()) ||
                    roles.contains(RoleName.ROLE_CONTENT_MANAGER.name());
        } else if (action.startsWith("UPDATE_USER")) {
            return roles.contains("ADMIN") || roles.contains("CONTENT_MANAGER");
        } else if (action.startsWith("CREATE_TRAINER")) {
            return roles.contains("ADMIN") || roles.contains("TRAINER_MANAGER");
        } else if (action.startsWith("DELETE_TRAINER")) {
            return roles.contains("ADMIN");
        } else if (action.startsWith("UPDATE_TRAINER")) {
            return roles.contains("ADMIN") || roles.contains("TRAINER_MANAGER");
        }
        return false;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws UserNotFoundException, InvalidCredentialsException {
        User user = userService.findByEmail(loginRequestDto.getEmail());

        log.info("row password: {}", loginRequestDto.getPassword());

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        Set<String> roles = user.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toSet());

        String accessToken = jwtUtil.generateToken(loginRequestDto.getEmail(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), roles);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roles(roles.stream().toList())
                .build();
    }

    @Override
    public void validate(String email, String token) throws AccessDeniedException {
        String emailAddress = jwtUtil.extractEmail(token);
        if (!emailAddress.equals(email)) {
            throw new AccessDeniedException("Access denied. You can perform operation with your own account");
        }
    }
}
