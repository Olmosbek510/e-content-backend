package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.repository.ArticleRepository;
import com.inha.os.econtentbackend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Override
    public Long getTotalActiclesCount() {
        return articleRepository.countAllArticles();
    }
}
