package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Book;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsBookByTitleAndAuthor(String title, String author);

    boolean existsBookById(Integer id);

    @Query("from Book b where b.content.subject.id = :subjectId")
    List<Book> findBooksBySubjectId(@Param("subjectId") Integer subjectId);
}