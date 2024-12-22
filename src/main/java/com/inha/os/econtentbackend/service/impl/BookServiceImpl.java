package com.inha.os.econtentbackend.service.impl;

import com.inha.os.econtentbackend.dto.request.BookCreateRequestDto;
import com.inha.os.econtentbackend.entity.Book;
import com.inha.os.econtentbackend.entity.Content;
import com.inha.os.econtentbackend.repository.BookRepository;
import com.inha.os.econtentbackend.repository.ContentTypeRepository;
import com.inha.os.econtentbackend.repository.SubjectRepository;
import com.inha.os.econtentbackend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final SubjectRepository subjectRepository;
    private final ContentTypeRepository contentTypeRepository;
    private final Base64.Decoder decoder;

    @Override
    public Long getTotalBooksCount() {
        return bookRepository.count();
    }

    @Override
    public void createBook(BookCreateRequestDto bookCreateRequestDto) {
    }
}
