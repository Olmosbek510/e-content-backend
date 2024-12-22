package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("select count (*) from Article ")
    Long countAllArticles();
}