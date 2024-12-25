package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.ELetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ELetterRepository extends JpaRepository<ELetter, Integer> {
    @Query("select count(*) from ELetter")
    Long countMaterials();

    boolean existsELetterByAuthorAndTitle(String author, String title);

    @Query("from ELetter e where e.content.subject.id = :subjectId")
    List<ELetter> findBySubjectId(@Param("subjectId") Integer subjectId);
}