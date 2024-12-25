package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.SystemAdminCreateRequestDto;
import com.inha.os.econtentbackend.entity.SystemAdmin;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.repository.SystemAdminRepository;
import com.inha.os.econtentbackend.service.RoleService;
import com.inha.os.econtentbackend.service.SystemAdminService;
import com.inha.os.econtentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemAdminServiceImpl implements SystemAdminService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SystemAdminRepository systemAdminRepository;
    private final RoleService roleService;

    @Override
    public void create(SystemAdminCreateRequestDto build) throws UserAlreadyExistsException, RoleNotFoundException {
        User user = User.builder()
                .email(build.getEmail())
                .firstName(build.getFirstName())
                .lastName(build.getLastName())
                .password(passwordEncoder.encode(build.getPassword()))
                .roles(Set.of(roleService.findByName(RoleName.ROLE_SYS_ADMIN)))
                .build();
        User savedUser = userService.save(user);

        var systemAdmin = SystemAdmin.builder().user(savedUser).build();

        systemAdminRepository.save(systemAdmin);
    }
}
