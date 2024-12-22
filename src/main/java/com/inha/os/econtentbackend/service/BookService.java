package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.BookCreateRequestDto;

public interface BookService {
    Long getTotalBooksCount();

    void createBook(BookCreateRequestDto bookCreateRequestDto);
}
