package com.inha.os.econtentbackend.controller;

import com.inha.os.econtentbackend.dto.request.ArticleCreateRequestDto;
import com.inha.os.econtentbackend.entity.Article;
import com.inha.os.econtentbackend.exception.ArticleNotFoundException;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.service.ArticleService;
import com.inha.os.econtentbackend.uri.URIS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(URIS.Article.BASE_URI)
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<String> createArticle(
            @ModelAttribute ArticleCreateRequestDto articleCreateRequestDto
    ) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException {
        log.info("article received: {}", articleCreateRequestDto.getTitle());

        String article = articleService.createArticle(articleCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    @GetMapping(URIS.Article.DOWNLOAD)
    @Transactional
    public ResponseEntity<byte[]> download(@PathVariable Integer id) throws ArticleNotFoundException {
        Article article = articleService.getById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + article.getTitle() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(article.getContent().getData());
    }
}
