package com.usermanagement.exceptions;

public class ValidationInputException extends RuntimeException {
    public ValidationInputException(String msg, Throwable err){
        super(msg, err);
    }

    public ValidationInputException(String msg){
        super(msg);
    }
}
