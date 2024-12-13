package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}