package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.dto.response.StudentCreateResponseDto;
import com.inha.os.econtentbackend.dto.response.StudentProfileDto;
import com.inha.os.econtentbackend.entity.Photo;
import com.inha.os.econtentbackend.entity.Role;
import com.inha.os.econtentbackend.entity.Student;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.exception.StudentAlreadyExistsException;
import com.inha.os.econtentbackend.exception.StudentNotFoundException;
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
import java.util.Base64;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;

    @Override
    @Transactional
    public StudentCreateResponseDto createStudent(StudentCreateDto studentCreateDto) throws UserAlreadyExistsException, StudentAlreadyExistsException, RoleNotFoundException {
        boolean exists = studentRepository.existsStudentByPhoneNumber(studentCreateDto.getPhoneNumber());
        if (exists) {
            throw new StudentAlreadyExistsException("phone number '%s' already taken".formatted(studentCreateDto.getPhoneNumber()));
        }

        boolean existsById = studentRepository.existsStudentByStudentId(studentCreateDto.getStudentId());
        if (existsById) {
            throw new StudentAlreadyExistsException("id '%s' already taken".formatted(studentCreateDto.getStudentId()));
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

    @Override
    public StudentProfileDto getProfile(String email) throws StudentNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findByEmail(email);

        if (optionalStudent.isEmpty()) {
            throw new StudentNotFoundException("student with email '%s' not found".formatted(email));
        }

        Student student = optionalStudent.get();

        Photo photo = student.getUser().getPhoto();
        String base64Photo = null;
        if (photo != null) {
            base64Photo = encoder.encodeToString(photo.getContent());
        }

        StudentProfileDto response = studentMapper.toProfileDto(student);

        response.setPhoto(base64Photo);
        Optional<Role> optionalRole = student.getUser().getRoles().stream().findFirst();

        optionalRole.ifPresent(role -> response.setRoleName(role.getRoleName().name()));

        response.setDateOfBirth(student.getBirthDate().toString());
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }

    @Override
    public Long getTotalStudentsCount() {
        return studentRepository.count();
    }
}
