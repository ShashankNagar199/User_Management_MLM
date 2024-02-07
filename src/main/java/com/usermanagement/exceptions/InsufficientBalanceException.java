package com.usermanagement.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String msg, Throwable err){
        super(msg, err);
    }
    public InsufficientBalanceException(String s) {
        super(s);
    }
}
