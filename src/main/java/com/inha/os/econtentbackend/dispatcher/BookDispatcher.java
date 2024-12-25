package com.inha.os.econtentbackend.dispatcher;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.BookCreateRequestDto;
import com.inha.os.econtentbackend.dto.request.BookDeleteRequestDto;
import com.inha.os.econtentbackend.dto.request.BooksGetRequestDto;
import com.inha.os.econtentbackend.dto.response.BaseResponse;
import com.inha.os.econtentbackend.dto.response.BookGetResponseDto;
import com.inha.os.econtentbackend.entity.enums.ResponseStatus;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.entity.interfaces.Entity;
import com.inha.os.econtentbackend.exception.BookNotFoundException;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.BookService;
import com.inha.os.econtentbackend.util.JsonUtil;
import com.inha.os.econtentbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookDispatcher {
    private final BookService bookService;
    private final AuthService authService;
    private final Gson gson;
    private final JwtUtil jwtUtil;

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException, BookNotFoundException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (action.equals(Actions.Books.CREATE_BOOK)) {
            return handleSaveBook(dataNode);
        } else if (action.equalsIgnoreCase(Actions.Books.GET_BOOK_NAMES)) {
            return handleGetBookNames(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Books.GET_BOOKS)) {
            return handleGetBooks(dataNode, token);
        } else if (authService.canPerformAction(action, token) && action.equalsIgnoreCase(Actions.Books.DELETE_BOOK)) {
            return handleDeleteBook(dataNode, token);
        }
        return "Unknown user action: " + action;
    }

    private String handleDeleteBook(String dataNode, String token) throws BookNotFoundException {
        BookDeleteRequestDto requestDto = JsonUtil.getObject(BookDeleteRequestDto.class, dataNode);

        String message = bookService.deleteById(requestDto);

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        Map<String, String> data = new HashMap<>();
        data.put("message", message);

        BaseResponse baseResponse = BaseResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .action(Actions.Books.DELETE_BOOK)
                .data(gson.toJson(data))
                .entity(Entity.BOOK)
                .build();
        optionalRole.ifPresent(baseResponse::setRole);
        return gson.toJson(baseResponse);
    }

    @Transactional
    protected String handleGetBooks(String dataNode, String token) {
        BooksGetRequestDto requestDto = JsonUtil.getObject(BooksGetRequestDto.class, dataNode);
        List<BookGetResponseDto> books = bookService.getBooks(requestDto.getSubjectId());

        Set<String> roles = jwtUtil.extractRoles(token);
        Optional<String> optionalRole = roles.stream().findFirst();

        BaseResponse baseResponse = BaseResponse.builder()
                .data(gson.toJson(books))
                .action(Actions.Books.GET_BOOKS)
                .entity(Entity.BOOK)
                .status(ResponseStatus.SUCCESS)
                .build();

        optionalRole.ifPresent(baseResponse::setRole);
        return gson.toJson(baseResponse);
    }

    private String handleGetBookNames(String dataNode, String token) {
        return "";
    }

    private String handleSaveBook(String dataNode) {
        BookCreateRequestDto bookCreateRequestDto = JsonUtil.getObject(BookCreateRequestDto.class, dataNode);
        bookService.createBook(bookCreateRequestDto);
        return "";
    }
}
