package com.inha.os.econtentbackend.exception;

import com.inha.os.econtentbackend.exception.exception.BaseException;

public class RoleAlreadyExistsException extends BaseException {
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}
