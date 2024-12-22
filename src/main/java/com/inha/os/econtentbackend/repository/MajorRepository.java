package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    Optional<Major> findMajorById(Integer id);

    boolean existsMajorByName(String name);
}