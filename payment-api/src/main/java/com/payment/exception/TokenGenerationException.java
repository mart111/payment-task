package com.payment.exception;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(String msg) {
        super(msg);
    }
}
