package com.proje.security.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class UserAlReady extends RuntimeException{

    private final String message;

    public UserAlReady(String message){
        super(message);
        this.message = message;

    }




}