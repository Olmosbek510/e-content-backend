package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.entity.ContentType;
import com.inha.os.econtentbackend.exception.ContentTypeAlreadyExistsException;

public interface ContentTypeService {
    ContentType save(ContentType build) throws ContentTypeAlreadyExistsException;
}
