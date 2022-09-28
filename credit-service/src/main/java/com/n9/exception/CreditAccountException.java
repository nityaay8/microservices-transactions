package com.n9.exception;

public class CreditAccountException extends RuntimeException {

    public CreditAccountException() {

    }

    public CreditAccountException(String msg) {
        super(msg);
    }

    public CreditAccountException(String msg, Exception e) {
        super(msg, e);
    }
}
