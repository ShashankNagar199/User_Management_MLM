package com.usermanagement.exceptions;

public class InvalidAuthenticationException extends RuntimeException{
    public InvalidAuthenticationException(String msg, Throwable err){
        super(msg, err);
    }

    public InvalidAuthenticationException(String msg){
        super(msg);
    }
}
