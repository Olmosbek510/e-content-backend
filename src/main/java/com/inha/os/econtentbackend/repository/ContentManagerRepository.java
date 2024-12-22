package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.ContentManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ContentManagerRepository extends JpaRepository<ContentManager, UUID> {
    @Query("SELECT COUNT(*) FROM ContentManager")
    Long countAllManagers();
}