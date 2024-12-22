package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.entity.ContentType;
import com.inha.os.econtentbackend.exception.ContentTypeAlreadyExistsException;
import com.inha.os.econtentbackend.repository.ContentTypeRepository;
import com.inha.os.econtentbackend.service.ContentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentTypeServiceImpl implements ContentTypeService {
    private final ContentTypeRepository contentTypeRepository;

    @Override
    public ContentType save(ContentType contentType) throws ContentTypeAlreadyExistsException {
        boolean existsContentTypeByName = contentTypeRepository.existsContentTypeByName(contentType.getName());
        if (existsContentTypeByName) {
            throw new ContentTypeAlreadyExistsException("content type '%s' already exists".formatted(contentType.getName()));
        }
        return contentTypeRepository.save(contentType);
    }
}
