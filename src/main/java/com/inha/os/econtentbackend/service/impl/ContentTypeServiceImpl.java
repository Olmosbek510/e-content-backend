package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.entity.Content;
import com.inha.os.econtentbackend.entity.ContentType;
import com.inha.os.econtentbackend.entity.enums.ContentTypeName;
import com.inha.os.econtentbackend.exception.ContentTypeAlreadyExistsException;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.repository.ContentRepository;
import com.inha.os.econtentbackend.repository.ContentTypeRepository;
import com.inha.os.econtentbackend.service.ContentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentTypeServiceImpl implements ContentTypeService {
    private final ContentTypeRepository contentTypeRepository;
    private final ContentRepository contentRepository;

    @Override
    public ContentType save(ContentType contentType) throws ContentTypeAlreadyExistsException {
        boolean existsContentTypeByName = contentTypeRepository.existsContentTypeByName(contentType.getName());
        if (existsContentTypeByName) {
            throw new ContentTypeAlreadyExistsException("content type '%s' already exists".formatted(contentType.getName()));
        }
        return contentTypeRepository.save(contentType);
    }

    @Override
    public ContentType findByName(ContentTypeName contentTypeName) throws ContentTypeNotFoundExceptionHttp {
        Optional<ContentType> optionalContentType = contentTypeRepository.findContentTypeByName(contentTypeName);
        if (optionalContentType.isEmpty()) {
            throw new ContentTypeNotFoundExceptionHttp("content '%s' not found");
        }
        return optionalContentType.get();
    }

    @Override
    public void initDb() {
        for (ContentTypeName value : ContentTypeName.values()) {
            contentTypeRepository.save(
                    ContentType.builder()
                            .name(value)
                            .build()
            );
        }
    }
}
