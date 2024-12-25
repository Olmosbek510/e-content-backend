package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.ArticleCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.BookCreateRequestDtoHttp;
import com.inha.os.econtentbackend.dto.request.ELetterCreateRequestDto;
import com.inha.os.econtentbackend.entity.Content;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContentService {
    Content createBookContent(MultipartFile book, BookCreateRequestDtoHttp bookCreateRequestDtoHttp) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException;

    Content createELetterContent(ELetterCreateRequestDto eLetterCreateRequestDto) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException;

    Content createArticleContent(ArticleCreateRequestDto articleCreateRequestDto) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException;
}
