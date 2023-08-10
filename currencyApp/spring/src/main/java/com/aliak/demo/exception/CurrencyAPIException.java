package com.aliak.demo.exception;

import org.springframework.http.HttpStatus;

public class CurrencyAPIException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public CurrencyAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public CurrencyAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
