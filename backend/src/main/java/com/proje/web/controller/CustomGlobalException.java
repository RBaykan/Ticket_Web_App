package com.proje.web.controller;


import com.proje.security.exceptions.TicketClosed;
import com.proje.security.exceptions.TicketNotFound;
import com.proje.security.exceptions.UserAlReady;
import com.proje.security.exceptions.UserNotFound;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CustomGlobalException {



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value()

        ), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = UserAlReady.class)
    public ResponseEntity<Object> userAllReady(UserAlReady ex) {
        return new ResponseEntity<>(Map.of(
                "message", ex.getMessage(),
                "status", HttpStatus.BAD_REQUEST.value()

        ), HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(value = UserNotFound.class)
    public ResponseEntity<Object> userNotFound(UserNotFound ex) {
        return new ResponseEntity<>(Map.of(
                "message", "User not found",
                "status", HttpStatus.NOT_FOUND.value()

        ), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = TicketNotFound.class)
    public ResponseEntity<Object> ticketNotFount(TicketNotFound ex) {
        return new ResponseEntity<>(Map.of(
                "message", "Ticket not found",
                "status", HttpStatus.NOT_FOUND.value()

        ), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = TicketClosed.class)
    public ResponseEntity<Object> ticketNotFount(TicketClosed ex) {
        return new ResponseEntity<>(Map.of(
                "message", "Ticket not found",
                "status", HttpStatus.BAD_REQUEST.value()

        ), HttpStatus.BAD_REQUEST);

    }











}
