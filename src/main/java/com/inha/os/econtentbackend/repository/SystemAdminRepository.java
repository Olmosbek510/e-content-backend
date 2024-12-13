package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.SystemAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SystemAdminRepository extends JpaRepository<SystemAdmin, UUID> {
}