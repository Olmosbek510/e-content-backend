package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Integer> {
}