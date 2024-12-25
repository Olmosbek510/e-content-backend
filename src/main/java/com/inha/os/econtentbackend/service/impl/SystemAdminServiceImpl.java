package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.SystemAdminCreateRequestDto;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.service.SystemAdminService;
import com.inha.os.econtentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemAdminServiceImpl implements SystemAdminService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(SystemAdminCreateRequestDto build) throws UserAlreadyExistsException {
        User user = User.builder()
                .email(build.getEmail())
                .firstName(build.getFirstName())
                .lastName(build.getLastName())
                .password(passwordEncoder.encode(build.getPassword()))
                .build();
        userService.save(user);
    }
}
