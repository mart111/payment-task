package com.payment.exception;

public class AuthenticationTokenNotValidException extends RuntimeException {

    public AuthenticationTokenNotValidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
