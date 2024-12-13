package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;

public interface UserService {
    User findByUsername(String username) throws UserNotFoundException;

    User setUpUser(StudentCreateDto studentCreateDto) throws UserAlreadyExistsException;

    User save(User user) throws UserAlreadyExistsException;

    User findByEmail(String email) throws UserNotFoundException;
}
