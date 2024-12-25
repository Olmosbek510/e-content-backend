package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Subject;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findAllByMajorId(Integer majorId);

    @Query("select count(c) from Content c where c.subject.id = :subjectId")
    Integer getTotalMaterialsById(@Param("subjectId") Integer subjectId);

    boolean existsSubjectByName(String name);

    Optional<Subject> findSubjectById(Integer subjectId);
}