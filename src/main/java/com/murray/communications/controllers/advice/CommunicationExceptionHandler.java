package com.murray.communications.controllers.advice;

import com.murray.communications.exceptions.InvalidJwtAuthenticationException;
import com.murray.communications.exceptions.MessageCreationException;
import com.murray.communications.exceptions.MessageNotFoundException;
import com.murray.communications.exceptions.MessageProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

/**
 * This handler will catch all the exception thrown by the REST or Controller layer and convert them to an HTTP code and message.
 */
@Slf4j
@ControllerAdvice
public class CommunicationExceptionHandler {


    @ExceptionHandler(value = {InvalidJwtAuthenticationException.class})
    public ResponseEntity invalidJwtAuthentication(InvalidJwtAuthenticationException ex, WebRequest request) {
        log.error("InvalidJwtAuthenticationException...", ex);
        return status(UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = {MessageCreationException.class})
    public ResponseEntity messageCreationException(MessageCreationException ex, WebRequest request) {
        log.error("MessageCreationException...", ex);
        return status(NOT_ACCEPTABLE).build();
    }

    @ExceptionHandler(value = {MessageNotFoundException.class})
    public ResponseEntity messageNotfoundException(MessageNotFoundException ex, WebRequest request) {
        log.error("MessageNotFoundException...", ex);
        return status(NOT_FOUND).build();
    }

    @ExceptionHandler(value = {MessageProcessingException.class})
    public ResponseEntity messageProcessingException(MessageProcessingException ex, WebRequest request) {
        log.error("MessageProcessingException...", ex);
        return status(BAD_REQUEST).build();
    }



}
