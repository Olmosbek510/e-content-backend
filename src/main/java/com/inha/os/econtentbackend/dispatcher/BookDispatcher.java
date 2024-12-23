package com.inha.os.econtentbackend.dispatcher;

import com.inha.os.econtentbackend.dto.request.BookCreateRequestDto;
import com.inha.os.econtentbackend.entity.interfaces.Actions;
import com.inha.os.econtentbackend.service.AuthService;
import com.inha.os.econtentbackend.service.BookService;
import com.inha.os.econtentbackend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class BookDispatcher {
    private final BookService bookService;
    private final AuthService authService;

    public String dispatch(String action, String dataNode, String token) throws AccessDeniedException {
        if (token != null && !authService.canPerformAction(action, token)) {
            throw new AccessDeniedException("Unauthorized access: You do not have permission to perform this action.");
        } else if (action.equals(Actions.Books.CREATE_BOOK)) {
            return handleSaveBook(dataNode);
        } else if (action.equalsIgnoreCase(Actions.Books.GET_BOOK_NAMES)) {
            return handleGetBookNames(dataNode, token);
        }
        return "Unknown user action: " + action;
    }

    private String handleGetBookNames(String dataNode, String token) {
//        BookGetNamesRequestDto
        return "";
    }

    private String handleSaveBook(String dataNode) {
        BookCreateRequestDto bookCreateRequestDto = JsonUtil.getObject(BookCreateRequestDto.class, dataNode);
        bookService.createBook(bookCreateRequestDto);
        return "";
    }
}
