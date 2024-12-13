package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.service.AuthenticationService;
import com.inha.os.econtentbackend.service.UserService;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
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
        if (action.startsWith("CREATE_USER")) {
            return roles.contains("ADMIN") || roles.contains("CONTENT_MANAGER");
        } else if (action.startsWith("DELETE_USER")) {
            return roles.contains("ADMIN");
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
}
