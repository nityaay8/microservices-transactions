package com.n9.exception;

public class AccountException extends RuntimeException {
    public AccountException() {

    }

    public AccountException(String msg) {
        super(msg);
    }

    public AccountException(String msg, Exception e) {
        super(msg, e);
    }
}
