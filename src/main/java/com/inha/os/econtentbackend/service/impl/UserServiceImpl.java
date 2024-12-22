package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.ContentManagerCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.entity.Photo;
import com.inha.os.econtentbackend.entity.Role;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.entity.enums.RoleName;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.mapper.UserMapper;
import com.inha.os.econtentbackend.repository.RoleRepository;
import com.inha.os.econtentbackend.repository.UserRepository;
import com.inha.os.econtentbackend.service.PhotoService;
import com.inha.os.econtentbackend.service.RoleService;
import com.inha.os.econtentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PhotoService photoService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User setUpUser(StudentCreateDto studentCreateDto) throws UserAlreadyExistsException {
        boolean b = userRepository.existsUserByEmail(studentCreateDto.getEmail());
        if (b) {
            throw new UserAlreadyExistsException("User already exists");
        }
        return userMapper.toEntity(studentCreateDto);
    }

    @Override
    public User save(User user) throws UserAlreadyExistsException {
        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User '%s' already exists".formatted(user.getEmail()));
        }
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isEmpty()) {
            throw new UserNotFoundException("User with email '%s' not found".formatted(email));
        }
        return userByEmail.get();
    }

    @Override
    public User setUpUser(ContentManagerCreateRequestDto contentManager) throws RoleNotFoundException, UserAlreadyExistsException {
        boolean existsUserByEmail = userRepository.existsUserByEmail(contentManager.getEmail());
        if (existsUserByEmail) {
            throw new UserAlreadyExistsException("email '%s' already taken".formatted(contentManager.getEmail()));
        }
        Role role = roleService.findByName(RoleName.ROLE_CONTENT_MANAGER);
        User build = User.builder()
                .firstName(contentManager.getFirstName())
                .password(passwordEncoder.encode(contentManager.getPassword()))
                .lastName(contentManager.getLastName())
                .roles(Set.of(role))
                .email(contentManager.getEmail())
                .build();

        if (contentManager.getBase64Photo() != null) {
            build.setPhoto(photoService.create(contentManager.getBase64Photo(), contentManager.getPhotoName()));
        }
        return build;
    }
}
