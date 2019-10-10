package com.murray.communications.exceptions;

import lombok.Getter;

@Getter
public class BandWidthException extends Exception {

    private int code;
    private String response;

    public BandWidthException(String message, int code, String response) {
        super(message);
        this.code = code;
        this.response = response;
    }
    public BandWidthException(String message) {
        super(message);
        this.code=-1;
        this.response="internal error";
    }

    public BandWidthException(String message, Throwable cause, int code, String response) {
        super(message, cause);
        this.code = code;
        this.response = response;
    }

    public BandWidthException(String message, Throwable cause){
        super(message, cause);
        this.code=-1;
        this.response="internal error";
    }

}
