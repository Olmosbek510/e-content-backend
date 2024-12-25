package com.inha.os.econtentbackend.config;

import com.inha.os.econtentbackend.dto.request.*;
import com.inha.os.econtentbackend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final DataInitService dataInitService;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;
    private final StudentService studentService;
    private final MajorService majorService;
    private final SubjectService subjectService;
    private final ContentTypeService contentTypeService;
    private final ContentManagerService contentManagerService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final SystemAdminService adminService;

    @SneakyThrows
    @Override
    public void run(String... args) {
        if (ddlAuto.equalsIgnoreCase("update")) {
            contentTypeService.initDb();
            roleService.initDb();
            adminService.create(SystemAdminCreateRequestDto.builder()
                    .firstName("Olmosbek")
                    .lastName("Urazboev")
                    .password("olmos555%")
                    .email("urazboev04@gmail.com")
                    .build());
        }


    }
}
