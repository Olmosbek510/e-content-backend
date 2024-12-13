package com.inha.os.econtentbackend.config;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.RequestDto;
import com.inha.os.econtentbackend.service.DataInitService;
import com.inha.os.econtentbackend.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final DataInitService dataInitService;
    private final Gson gson;

    @SneakyThrows
    @Override
    public void run(String... args) {
        dataInitService.initRoles();
    }
}
