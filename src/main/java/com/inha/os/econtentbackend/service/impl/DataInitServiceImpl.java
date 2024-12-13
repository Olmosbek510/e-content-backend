package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.exception.RoleAlreadyExistsException;
import com.inha.os.econtentbackend.service.DataInitService;
import com.inha.os.econtentbackend.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitServiceImpl implements DataInitService {
    private final RoleService roleService;

    @Override
    public void initRoles() throws RoleAlreadyExistsException {
        for (RoleName value : RoleName.values()) {
            roleService.save(value);
            log.info("role '{}' saved successfully", value);
        }
    }
}
