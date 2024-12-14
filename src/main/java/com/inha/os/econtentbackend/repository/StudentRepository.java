package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Student;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    boolean existsStudentByPhoneNumber(@NotBlank(message = "phone number cannot be blank") String studentCreateDto);

    @Query("from Student s where s.user.email = :email")
    Optional<Student> findByEmail(@Param("email") String email);
}