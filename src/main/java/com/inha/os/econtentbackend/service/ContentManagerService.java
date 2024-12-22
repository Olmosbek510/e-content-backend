package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.ContentManagerCreateRequestDto;
import com.inha.os.econtentbackend.dto.response.ContentManagerCreateResponseDto;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;

import javax.management.relation.RoleNotFoundException;

public interface ContentManagerService {
    Long getTotalContentManagersCount();
    
    ContentManagerCreateResponseDto createContentManager(ContentManagerCreateRequestDto contentManagerService) throws RoleNotFoundException, UserAlreadyExistsException;
}
