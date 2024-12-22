package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.response.StatisticsResponseDto;
import com.inha.os.econtentbackend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    private final MajorService majorService;
    private final BookService bookService;
    private final StudentService studentService;
    private final ELetterService eLetterService;
    private final ContentManagerService contentManagerService;
    private final ArticleService articleService;

    @Override
    public StatisticsResponseDto getStatistics() {
        Long totalBooksCount = bookService.getTotalBooksCount();
        Long totalStudentsCount = studentService.getTotalStudentsCount();
        Long totalMajorsCount = majorService.getTotalMajorsCount();
        Long totalElettersCount = eLetterService.getTotalElettersCount();
        Long totalContentManagersCount = contentManagerService.getTotalContentManagersCount();
        Long totalArticlesCount = articleService.getTotalActiclesCount();
        Long totalContentsCount = totalBooksCount + totalElettersCount + totalArticlesCount;
        return StatisticsResponseDto.builder()
                .totalBooks(totalBooksCount)
                .totalUsers(totalStudentsCount)
                .totalContents(totalContentsCount)
                .totalELetters(totalElettersCount)
                .totalArticleCount(totalArticlesCount)
                .totalMajors(totalMajorsCount)
                .totalContentManagers(totalContentManagersCount)
                .build();
    }
}
