package com.usermanagement.exceptions;

public class PasswordsMismatchException extends RuntimeException {

    public PasswordsMismatchException(String msg, Throwable err){
        super(msg, err);
    }
    public PasswordsMismatchException(String s) {
        super(s);
    }
}
