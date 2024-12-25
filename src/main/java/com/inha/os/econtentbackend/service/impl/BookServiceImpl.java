package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.BookCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.BookCreateRequestDtoHttp;
import com.inha.os.econtentbackend.dto.request.BookDeleteRequestDto;
import com.inha.os.econtentbackend.dto.response.BookGetResponseDto;
import com.inha.os.econtentbackend.entity.Book;
import com.inha.os.econtentbackend.entity.Content;
import com.inha.os.econtentbackend.exception.BookAlreadyExistsExceptionHttp;
import com.inha.os.econtentbackend.exception.BookNotFoundException;
import com.inha.os.econtentbackend.exception.ContentTypeNotFoundExceptionHttp;
import com.inha.os.econtentbackend.exception.SubjectNotFoundExceptionHttp;
import com.inha.os.econtentbackend.repository.BookRepository;
import com.inha.os.econtentbackend.repository.ContentTypeRepository;
import com.inha.os.econtentbackend.repository.SubjectRepository;
import com.inha.os.econtentbackend.service.BookService;
import com.inha.os.econtentbackend.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final SubjectRepository subjectRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final Base64.Decoder decoder;
    private final ContentService contentService;

    @Override
    public Long getTotalBooksCount() {
        return bookRepository.count();
    }

    @Override
    public void createBook(BookCreateRequestDto bookCreateRequestDto) {
    }

    @Override
    public String createBook(BookCreateRequestDtoHttp bookCreateRequestDtoHttp) throws ContentTypeNotFoundExceptionHttp, SubjectNotFoundExceptionHttp, IOException, BookAlreadyExistsExceptionHttp {
        boolean exists = bookRepository.existsBookByTitleAndAuthor(bookCreateRequestDtoHttp.getTitle(), bookCreateRequestDtoHttp.getAuthor());
        if (exists) {
            throw new BookAlreadyExistsExceptionHttp("book '%s' already exists".formatted(bookCreateRequestDtoHttp.getTitle()));
        }
        Content bookContent = contentService.createBookContent(bookCreateRequestDtoHttp.getBook(), bookCreateRequestDtoHttp);
        Book createdBook = Book.builder()
                .description(bookCreateRequestDtoHttp.getDescription())
                .content(bookContent)
                .title(bookCreateRequestDtoHttp.getTitle())
                .author(bookCreateRequestDtoHttp.getAuthor())
                .page(bookCreateRequestDtoHttp.getPageCount())
                .build();
        bookRepository.save(createdBook);
        return "book '%s' created successfully".formatted(bookCreateRequestDtoHttp.getTitle());
    }

    @Override
    @Transactional
    public Book findById(Integer id) throws BookNotFoundException {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("book with id '%s' not found".formatted(id));
        }
        return optionalBook.get();
    }

    @Override
    @Transactional
    public List<BookGetResponseDto> getBooks(Integer subjectId) {
        List<Book> books = bookRepository.findBooksBySubjectId(subjectId);
        List<BookGetResponseDto> responseDtos = new LinkedList<>();
        for (Book book : books) {
            responseDtos.add(
                    BookGetResponseDto.builder()
                            .bookId(book.getId())
                            .author(book.getAuthor())
                            .title(book.getTitle())
                            .pageCount(book.getPage())
                            .description(book.getDescription())
                            .build()
            );
        }
        return responseDtos;
    }

    @Override
    @Transactional
    public String deleteById(BookDeleteRequestDto requestDto) throws BookNotFoundException {
        if (!bookRepository.existsBookById(requestDto.getBookId())) {
            throw new BookNotFoundException("Book with id '%s' not found".formatted(requestDto.getBookId()));
        }
        bookRepository.deleteById(requestDto.getBookId());
        return "book with id '%d' deleted".formatted(requestDto.getBookId());
    }
}
