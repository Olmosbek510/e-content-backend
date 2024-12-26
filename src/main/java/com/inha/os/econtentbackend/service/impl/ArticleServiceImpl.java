package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.ArticleCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.ArticleResponseDto;
import com.inha.os.econtentbackend.dto.request.SubjectsGetRequestDto;
import com.inha.os.econtentbackend.entity.Article;
import com.inha.os.econtentbackend.entity.Content;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.ArticleNotFoundException;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.mapper.ArticleMapper;
import com.inha.os.econtentbackend.repository.ArticleRepository;
import com.inha.os.econtentbackend.service.ArticleService;
import com.inha.os.econtentbackend.service.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ContentService contentService;

    @Override
    public Long getTotalActiclesCount() {
        return articleRepository.countAllArticles();
    }

    @Override
    public List<ArticleResponseDto> getSubjects(SubjectsGetRequestDto requestDto) {
        List<Article> articles = articleRepository.findBySubjectId(requestDto.getSubjectId());
        return articles.stream().map(articleMapper::toResponseDto).toList();
    }

    @Override
    public String createArticle(ArticleCreateRequestDto articleCreateRequestDto) throws
            ContentTypeNotFoundExceptionHttp,
            SubjectNotFoundExceptionHttp,
            IOException {
        Article article = articleMapper.toEntity(articleCreateRequestDto);
        Content articleContent = contentService.createArticleContent(articleCreateRequestDto);
        article.setContent(articleContent);
        articleRepository.save(article);

        return "article '%s' created successfully".formatted(articleCreateRequestDto.getTitle());
    }

    @Override
    @Transactional
    public Article getById(Integer id) throws ArticleNotFoundException {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isEmpty()) {
            throw new ArticleNotFoundException("Article with Id not ");
        }
        return optionalArticle.get();
    }

    @Override
    public String deleteById(ArticleResponseDto articleDto) throws ArticleNotFoundException {
        Integer articleId = articleDto.getArticleId();
        if (!articleRepository.existsArticleById(articleId)) {
            throw new ArticleNotFoundException("article with id '%s' not found".formatted(articleId));
        }
        articleRepository.deleteById(articleId);
        return "article with id '%s' deleted".formatted(articleId);
    }
}
