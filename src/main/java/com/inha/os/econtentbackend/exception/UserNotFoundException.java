package com.inha.os.econtentbackend.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String formatted) {
        super(formatted);
    }
}
