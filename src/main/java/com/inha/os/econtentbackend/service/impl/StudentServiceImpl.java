package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.dto.response.StudentCreateResponseDto;
import com.inha.os.econtentbackend.entity.Student;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.mapper.StudentMapper;
import com.inha.os.econtentbackend.repository.StudentRepository;
import com.inha.os.econtentbackend.service.StudentService;
import com.inha.os.econtentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StudentCreateResponseDto createStudent(StudentCreateDto studentCreateDto) throws UserAlreadyExistsException, StudentAlreadyExistsException {
        boolean exists = studentRepository.existsStudentByPhoneNumber(studentCreateDto);
        if (exists) {
            throw new StudentAlreadyExistsException("student with phone number '%s'".formatted(studentCreateDto.getPhoneNumber()));
        }
        User user = userService.setUpUser(studentCreateDto);
        String plainPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(plainPassword));
        Student student = studentMapper.toEntity(studentCreateDto);
        student.setUser(user);

        Student savedStudent = studentRepository.save(student);
        StudentCreateResponseDto responseDto = studentMapper.toCreateResponseDto(savedStudent);
        responseDto.setPassword(plainPassword);
        responseDto.setStatus(ResponseStatus.SUCCESS.name());
        responseDto.setRoleName(RoleName.ROLE_STUDENT.name());
        return responseDto;
    }
}
