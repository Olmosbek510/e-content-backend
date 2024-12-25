package com.inha.os.econtentbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidMediaTypeException.class)
    public ResponseEntity<ErrorResponseHttp> handleInvalidMediaTypeException(InvalidMediaTypeException exception) {
        ErrorResponseHttp responseHttp = ErrorResponseHttp.builder()
                .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(responseHttp);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseHttp> handleBookNotFoundException(BookNotFoundException exception) {
        ErrorResponseHttp build = ErrorResponseHttp.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(build);
    }

    @ExceptionHandler(ELetterAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseHttp> handleELetterAlreadyExistsException(ELetterAlreadyExistsException ex) {
        ErrorResponseHttp response = ErrorResponseHttp.builder().message(ex.getMessage())
                .code(HttpStatus.CONFLICT.value()).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ELetterNotFoundException.class)
    public ResponseEntity<ErrorResponseHttp> handleELetterNotFoundException(ELetterNotFoundException e) {
        ErrorResponseHttp responseHttp = ErrorResponseHttp.builder()
                .message(e.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseHttp);
    }
}
