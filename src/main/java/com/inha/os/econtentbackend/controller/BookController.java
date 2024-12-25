package com.inha.os.econtentbackend.controller;

import com.inha.os.econtentbackend.dto.request.BookCreateRequestDtoHttp;
import com.inha.os.econtentbackend.entity.Book;
import com.inha.os.econtentbackend.exception.BookAlreadyExistsExceptionHttp;
import com.inha.os.econtentbackend.exception.BookNotFoundException;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.service.BookService;
import com.inha.os.econtentbackend.uri.URIS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(URIS.Book.BASE_URI)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<String> uploadBook(
            @ModelAttribute BookCreateRequestDtoHttp bookCreateRequestDtoHttp
    ) throws ContentTypeNotFoundExceptionHttp, BookAlreadyExistsExceptionHttp, SubjectNotFoundExceptionHttp, IOException {
        log.info("Book create request: {}", bookCreateRequestDtoHttp.toString());
        String message = bookService.createBook(bookCreateRequestDtoHttp);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping(URIS.Book.DOWNLOAD)
    @Transactional
    public ResponseEntity<byte[]> downloadBook(@PathVariable(name = "id") Integer id) throws BookNotFoundException {
        Book book = bookService.findById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + book.getTitle() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(book.getContent().getData());
    }
}
