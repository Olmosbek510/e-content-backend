package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}