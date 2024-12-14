package com.inha.os.econtentbackend.mapper;

import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.dto.response.StudentCreateResponseDto;
import com.inha.os.econtentbackend.dto.response.StudentProfileDto;
import com.inha.os.econtentbackend.entity.BaseEntity;
import com.inha.os.econtentbackend.entity.Student;
import com.inha.os.econtentbackend.entity.User;
import jakarta.persistence.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(source = "university", target = "university")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    Student toEntity(StudentCreateDto createDto);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "studentId", target = "studentId")
    StudentCreateResponseDto toCreateResponseDto(Student savedStudent);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "university", target = "university")
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "work", target = "work")
    StudentProfileDto toProfileDto(Student student);
//    public class Student extends BaseEntity {
//        @Id
//        @GeneratedValue(strategy = GenerationType.UUID)
//        private UUID id;
//        @OneToOne
//        private User user;
//        private String studentId;
//        private LocalDate birthDate;
//        private String work;
//        private String university;
//        @Column(nullable = false, unique = true)
//        private String phoneNumber;
//        private String address;
//    }

}
