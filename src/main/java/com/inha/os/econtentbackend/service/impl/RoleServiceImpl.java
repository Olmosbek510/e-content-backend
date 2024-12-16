package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.entity.Role;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.repository.RoleRepository;
import com.inha.os.econtentbackend.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role setUpRole(RoleName name) {
        return Role.builder()
                .roleName(name)
                .build();
    }

    @Override
    public Role save(RoleName value) {
        if (roleRepository.existsRoleByRoleName(value)) {
            log.error("Role '{}' already exists", value.name());
            return null;
        }
        Role role = Role.builder()
                .roleName(value)
                .build();
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(RoleName roleName) throws RoleNotFoundException {
        Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
        if (optionalRole.isEmpty()) {
            throw new RoleNotFoundException("role '%s' not found".formatted(roleName.name()));
        }
        return optionalRole.get();
    }
}
