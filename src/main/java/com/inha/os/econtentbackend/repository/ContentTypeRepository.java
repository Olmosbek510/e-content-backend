package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentTypeRepository extends JpaRepository<ContentType, Integer> {
}