package com.inha.os.econtentbackend.repository;

import com.inha.os.econtentbackend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}