package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    boolean existsUserByUsernameOrEmail(@NotBlank(message = "username cannot be blank") String username, @NotBlank(message = "email cannot be blank") String email);

    Optional<User> findUserByEmail(String email);
}