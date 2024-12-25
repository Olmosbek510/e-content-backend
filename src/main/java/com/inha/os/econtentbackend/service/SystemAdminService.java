package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.SystemAdminCreateRequestDto;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;

import javax.management.relation.RoleNotFoundException;

public interface SystemAdminService {
    void create(SystemAdminCreateRequestDto build) throws UserAlreadyExistsException, RoleNotFoundException;
}
