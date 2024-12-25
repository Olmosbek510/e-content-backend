package com.inha.os.econtentbackend.exception;


public class ArticleNotFoundException extends Throwable {
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
