package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Role;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsRoleByRoleName(RoleName value);
}