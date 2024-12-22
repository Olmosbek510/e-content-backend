package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.ContentManagerCreateRequestDto;
import com.inha.os.econtentbackend.dto.response.ContentManagerCreateResponseDto;
import com.inha.os.econtentbackend.entity.ContentManager;
import com.inha.os.econtentbackend.entity.User;
import com.inha.os.econtentbackend.exception.UserAlreadyExistsException;
import com.inha.os.econtentbackend.repository.ContentManagerRepository;
import com.inha.os.econtentbackend.service.ContentManagerService;
import com.inha.os.econtentbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentManagerServiceImpl implements ContentManagerService {
    private final ContentManagerRepository contentManagerRepository;
    private final UserService userService;

    @Override
    public Long getTotalContentManagersCount() {
        return contentManagerRepository.countAllManagers();
    }

    @Override
    public ContentManagerCreateResponseDto createContentManager(ContentManagerCreateRequestDto contentManager) throws RoleNotFoundException, UserAlreadyExistsException {
        String rawPassword = contentManager.getPassword();
        User savedUser = userService.save(userService.setUpUser(contentManager));
        ContentManager manager = ContentManager.builder()
                .user(savedUser)
                .build();
        contentManagerRepository.save(manager);
        return ContentManagerCreateResponseDto.builder()
                .email(savedUser.getEmail())
                .rawPassword(rawPassword)
                .build();
    }
}
