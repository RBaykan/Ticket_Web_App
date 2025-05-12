package com.proje.security.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomJWTException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public CustomJWTException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

}
