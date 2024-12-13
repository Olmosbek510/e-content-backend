package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.entity.Role;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.exception.RoleAlreadyExistsException;

public interface RoleService {
    Role setUpRole(RoleName name);

    Role save(RoleName value) throws RoleAlreadyExistsException;
}
