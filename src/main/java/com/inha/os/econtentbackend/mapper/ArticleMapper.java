package com.inha.os.econtentbackend.mapper;

import com.inha.os.econtentbackend.dto.request.ArticleCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.ArticleResponseDto;
import com.inha.os.econtentbackend.entity.Article;
import jakarta.persistence.Column;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(source = "id", target = "articleId")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "reference", target = "reference")
    @Mapping(source = "publishYear", target = "publishYear")
    ArticleResponseDto toResponseDto(Article article);


    Article toEntity(ArticleCreateRequestDto articleCreateRequestDto);
}
