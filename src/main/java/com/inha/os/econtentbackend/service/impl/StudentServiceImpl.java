package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.dto.response.StudentCreateResponseDto;
import com.inha.os.econtentbackend.entity.Role;
import com.inha.os.econtentbackend.entity.Student;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.mapper.StudentMapper;
import com.inha.os.econtentbackend.repository.StudentRepository;
import com.inha.os.econtentbackend.service.RoleService;
import com.inha.os.econtentbackend.service.StudentService;
import com.inha.os.econtentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    @Transactional
    public StudentCreateResponseDto createStudent(StudentCreateDto studentCreateDto) throws UserAlreadyExistsException, StudentAlreadyExistsException, RoleNotFoundException {
        boolean exists = studentRepository.existsStudentByPhoneNumber(studentCreateDto.getPhoneNumber());
        if (exists) {
            throw new StudentAlreadyExistsException("student with phone number '%s'".formatted(studentCreateDto.getPhoneNumber()));
        }
        User user = userService.setUpUser(studentCreateDto);
        String plainPassword = user.getPassword();
        LocalDate birthDate = LocalDate.parse(studentCreateDto.getBirthDate());

        Role role = roleService.findByName(RoleName.ROLE_STUDENT);

        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setRoles(Set.of(role));


        User savedUser = userService.save(user);
        Student student = studentMapper.toEntity(studentCreateDto);
        student.setBirthDate(birthDate);
        student.setUser(savedUser);

        Student savedStudent = studentRepository.save(student);
        StudentCreateResponseDto responseDto = studentMapper.toCreateResponseDto(savedStudent);
        responseDto.setPassword(plainPassword);
        responseDto.setStatus(ResponseStatus.SUCCESS);
        responseDto.setRoleName(RoleName.ROLE_STUDENT.name());
        return responseDto;
    }
}
