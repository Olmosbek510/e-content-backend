package com.inha.os.econtentbackend.exception;

import com.inha.os.econtentbackend.exception.exception.BaseException;

public class StudentAlreadyExistsException extends BaseException {
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}
