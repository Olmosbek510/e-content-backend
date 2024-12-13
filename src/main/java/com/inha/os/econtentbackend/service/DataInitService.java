package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.exception.RoleAlreadyExistsException;

public interface DataInitService {
    void initRoles() throws RoleAlreadyExistsException;
}
