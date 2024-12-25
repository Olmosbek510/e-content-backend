package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.ArticleCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.ArticleResponseDto;
import com.inha.os.econtentbackend.dto.request.SubjectsGetRequestDto;
import com.inha.os.econtentbackend.dto.response.SubjectResponseDto;
import com.inha.os.econtentbackend.entity.Article;
import com.inha.os.econtentbackend.exception.ArticleNotFoundException;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
    Long getTotalActiclesCount();

    List<ArticleResponseDto> getSubjects(SubjectsGetRequestDto requestDto);

    String createArticle(ArticleCreateRequestDto articleCreateRequestDto) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException;

    Article getById(Integer id) throws ArticleNotFoundException;
}
