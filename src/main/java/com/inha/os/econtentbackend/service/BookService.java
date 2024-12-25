package com.inha.os.econtentbackend.service;

import com.inha.os.econtentbackend.dto.request.BookCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.BookCreateRequestDtoHttp;
import com.inha.os.econtentbackend.dto.request.BookDeleteRequestDto;
import com.inha.os.econtentbackend.dto.response.BookGetResponseDto;
import com.inha.os.econtentbackend.entity.Book;
import com.inha.os.econtentbackend.exception.BookAlreadyExistsExceptionHttp;
import com.inha.os.econtentbackend.exception.BookNotFoundException;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;

import java.io.IOException;
import java.util.List;

public interface BookService {
    Long getTotalBooksCount();

    void createBook(BookCreateRequestDto bookCreateRequestDto);

    String createBook(BookCreateRequestDtoHttp bookCreateRequestDtoHttp) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException, BookAlreadyExistsExceptionHttp;

    Book findById(Integer id) throws BookNotFoundException;

    List<BookGetResponseDto> getBooks(Integer subjectId);

    String deleteById(BookDeleteRequestDto requestDto) throws BookNotFoundException;
}
