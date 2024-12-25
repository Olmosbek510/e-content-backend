package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.entity.ContentType;
import com.inha.os.econtentbackend.entity.enums.ContentTypeName;
import com.inha.os.econtentbackend.exception.ContentTypeAlreadyExistsException;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;

public interface ContentTypeService {
    ContentType save(ContentType build) throws ContentTypeAlreadyExistsException;

    ContentType findByName(ContentTypeName contentTypeName) throws ContentTypeNotFoundExceptionHttp;

    void initDb();
}
