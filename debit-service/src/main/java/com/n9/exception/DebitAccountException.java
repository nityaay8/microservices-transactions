package com.n9.exception;

public class DebitAccountException extends RuntimeException {
    public DebitAccountException() {

    }

    public DebitAccountException(String msg) {
        super(msg);
    }

    public DebitAccountException(String msg, Exception e) {
        super(msg, e);
    }
}
