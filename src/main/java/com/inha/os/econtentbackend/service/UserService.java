package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.ContentManagerCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;

import javax.management.relation.RoleNotFoundException;

public interface UserService {

    User setUpUser(StudentCreateDto studentCreateDto) throws UserAlreadyExistsException;

    User save(User user) throws UserAlreadyExistsException;

    User findByEmail(String email) throws UserNotFoundException;

    User setUpUser(ContentManagerCreateRequestDto contentManager) throws RoleNotFoundException, UserAlreadyExistsException;
}
