package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}