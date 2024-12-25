package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.LoginRequestDto;
import com.inha.os.econtentbackend.dto.response.LoginResponseDto;
import com.inha.os.econtentbackend.entity.Role;
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
import java.util.Optional;
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
        } else if (action.startsWith(Actions.Subject.GET_SUBJECTS)) {
            return roles.contains(RoleName.ROLE_SYS_ADMIN.name()) ||
                    roles.contains(RoleName.ROLE_STUDENT.name()) ||
                    roles.contains(RoleName.ROLE_CONTENT_MANAGER.name());
        } else if (action.startsWith(Actions.Statistics.GET_STATISTICS)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name());
        } else if (action.startsWith(Actions.Majors.ADD_MAJOR)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name());
        } else if (action.startsWith(Actions.Majors.UPDATE_MAJOR)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name());
        } else if (action.startsWith(Actions.Majors.DELETE_MAJOR)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name());
        } else if (action.startsWith(Actions.Subject.UPDATE_SUBJECT)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name());
        } else if (action.startsWith(Actions.Subject.ADD_SUBJECT)) {
            return roles.contains(RoleName.ROLE_SYS_ADMIN.name()) ||
                    roles.contains(RoleName.ROLE_CONTENT_MANAGER.name());
        } else if (action.startsWith(Actions.Subject.DELETE_SUBJECT)) {
            return roles.contains(RoleName.ROLE_SYS_ADMIN.name()) ||
                    roles.contains(RoleName.ROLE_CONTENT_MANAGER.name());
        } else if (action.startsWith(Actions.Books.GET_BOOKS)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name()) ||
                    roles.contains(RoleName.ROLE_STUDENT.name());
        } else if (action.startsWith(Actions.Books.DELETE_BOOK)) {
            return roles.contains(RoleName.ROLE_SYS_ADMIN.name()) ||
                    roles.contains(RoleName.ROLE_CONTENT_MANAGER.name());
        } else if (action.startsWith(Actions.Article.GET_ARTICLES)) {
            return roles.contains(RoleName.ROLE_SYS_ADMIN.name()) ||
                    roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_STUDENT.name());
        } else if (action.startsWith(Actions.ELetter.CREATE_E_LETTER)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name());
        } else if (action.startsWith(Actions.ELetter.GET_E_LETTERS)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name()) ||
                    roles.contains(RoleName.ROLE_STUDENT.name());
        } else if (action.startsWith(Actions.ELetter.DELETE_E_LETTER)) {
            return roles.contains(RoleName.ROLE_CONTENT_MANAGER.name()) ||
                    roles.contains(RoleName.ROLE_SYS_ADMIN.name());
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

        Optional<Role> optionalRole = user.getRoles().stream().findFirst();
        String accessToken = jwtUtil.generateToken(loginRequestDto.getEmail(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), roles);


        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
