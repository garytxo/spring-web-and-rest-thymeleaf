package com.murray.communications.exceptions;

public class MessageCreationException extends RuntimeException {

    public MessageCreationException() {
    }

    public MessageCreationException(String message) {
        super(message);
    }

    public MessageCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
