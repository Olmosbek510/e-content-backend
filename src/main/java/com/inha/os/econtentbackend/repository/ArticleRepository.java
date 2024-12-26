package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Article;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("select count (*) from Article ")
    Long countAllArticles();

    @Query("from Article a where a.content.subject.id = :subjectId")
    List<Article> findBySubjectId(@Param("subjectId") Integer subjectId);

    boolean existsArticleById(Integer articleId);
}