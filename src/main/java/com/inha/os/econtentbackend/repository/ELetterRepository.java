package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.ELetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ELetterRepository extends JpaRepository<ELetter, Integer> {
    @Query("select count(*) from ELetter")
    Long countMaterials();
}