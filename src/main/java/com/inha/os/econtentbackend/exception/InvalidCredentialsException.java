package com.inha.os.econtentbackend.exception;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException(String invalidUsernameOrPassword) {
        super(invalidUsernameOrPassword);
    }
}
