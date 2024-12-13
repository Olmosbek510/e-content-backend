package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.StudentCreateDto;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.exception.UserNotFoundException;
import com.inha.os.econtentbackend.mapper.UserMapper;
import com.inha.os.econtentbackend.repository.UserRepository;
import com.inha.os.econtentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with username '%s' not found".formatted(username));
        }
        return optionalUser.get();
    }

    @Override
    public User setUpUser(StudentCreateDto studentCreateDto) throws UserAlreadyExistsException {
        boolean b = userRepository.existsUserByUsernameOrEmail(studentCreateDto.getUsername(), studentCreateDto.getEmail());
        if (b) {
            throw new UserAlreadyExistsException("User already exists");
        }
        return userMapper.toEntity(studentCreateDto);
    }
}
