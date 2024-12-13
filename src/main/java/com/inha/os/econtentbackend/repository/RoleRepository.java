package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Role;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsRoleByRoleName(RoleName value);

    Optional<Role> findByRoleName(RoleName roleName);
}