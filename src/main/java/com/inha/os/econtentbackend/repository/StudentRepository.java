package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    boolean existsStudentByPhoneNumber(StudentCreateDto studentCreateDto);
}