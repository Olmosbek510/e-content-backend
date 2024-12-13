package com.inha.os.econtentbackend.exception;

import com.inha.os.econtentbackend.exception.exception.BaseException;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException(String invalidUsernameOrPassword) {
        super(invalidUsernameOrPassword);
    }
}
