package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.ContentType;
import com.inha.os.econtentbackend.entity.enums.ContentTypeName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentTypeRepository extends JpaRepository<ContentType, Integer> {
    boolean existsContentTypeByName(ContentTypeName name);

    Optional<ContentType> findContentTypeByName(ContentTypeName contentTypeName);
}