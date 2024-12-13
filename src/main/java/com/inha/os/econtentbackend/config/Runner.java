package com.inha.os.econtentbackend.config;

import com.inha.os.econtentbackend.service.DataInitService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final DataInitService dataInitService;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @SneakyThrows
    @Override
    public void run(String... args) {
        if (ddlAuto.equalsIgnoreCase("create")) {
            dataInitService.initRoles();
        }
    }
}
