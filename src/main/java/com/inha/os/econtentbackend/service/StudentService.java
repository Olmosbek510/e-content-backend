package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.dto.response.StudentCreateResponseDto;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;

import javax.management.relation.RoleNotFoundException;

public interface StudentService {
    StudentCreateResponseDto createStudent(StudentCreateDto studentCreateDto) throws UserAlreadyExistsException, StudentAlreadyExistsException, RoleNotFoundException;
}
