package com.app.speer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserSelfShareException extends RuntimeException {
    public UserSelfShareException(String message) {
        super(message);
    }
}