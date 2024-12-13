package com.inha.os.econtentbackend.exception;

import com.inha.os.econtentbackend.exception.exception.BaseException;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
