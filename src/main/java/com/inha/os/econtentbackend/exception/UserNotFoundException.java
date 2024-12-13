package com.inha.os.econtentbackend.exception;

import com.inha.os.econtentbackend.exception.exception.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String formatted) {
        super(formatted);
    }
}
