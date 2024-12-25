package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.ArticleCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.BookCreateRequestDtoHttp;
import com.inha.os.econtentbackend.dto.request.ELetterCreateRequestDto;
import com.inha.os.econtentbackend.entity.Content;
import com.inha.os.econtentbackend.entity.ContentType;
import com.inha.os.econtentbackend.entity.Subject;
import com.inha.os.econtentbackend.entity.enums.ContentTypeName;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.repository.ContentRepository;
import com.inha.os.econtentbackend.service.ContentService;
import com.inha.os.econtentbackend.service.ContentTypeService;
import com.inha.os.econtentbackend.service.SubjectService;
import com.inha.os.econtentbackend.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final ContentTypeService contentTypeService;
    private final SubjectService subjectService;

    @Override
    public Content createBookContent(MultipartFile book, BookCreateRequestDtoHttp bookCreateRequestDtoHttp) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException {
        ContentType contentType = contentTypeService.findByName(ContentTypeName.EBOOK);
        Subject subject = subjectService.findById(bookCreateRequestDtoHttp.getSubjectId());
        return contentRepository.save(Content.builder()
                .contentType(contentType)
                .subject(subject)
                .data(book.getBytes())
                .format(FileUtil.getFileExtension(book.getOriginalFilename()))
                .build());

    }

    @Override
    public Content createELetterContent(ELetterCreateRequestDto eLetterCreateRequestDto) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException {
        ContentType contentType = contentTypeService.findByName(ContentTypeName.E_LETTER);
        Subject subject = subjectService.findById(eLetterCreateRequestDto.getSubjectId());

        return contentRepository.save(Content.builder()
                .contentType(contentType)
                .subject(subject)
                .data(eLetterCreateRequestDto.getFile().getBytes())
                .format(FileUtil.getFileExtension(eLetterCreateRequestDto.getFile().getOriginalFilename()))
                .build());
    }

    @Override
    public Content createArticleContent(ArticleCreateRequestDto articleCreateRequestDto) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException {
        ContentType contentType = contentTypeService.findByName(ContentTypeName.ARTICLE);
        Subject subject = subjectService.findById(articleCreateRequestDto.getSubjectId());

        return contentRepository.save(
                Content.builder()
                        .contentType(contentType)
                        .subject(subject)
                        .data(articleCreateRequestDto.getFile().getBytes())
                        .format(FileUtil.getFileExtension(articleCreateRequestDto.getFile().getOriginalFilename()))
                        .build()
        );
    }
}
