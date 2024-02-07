package com.usermanagement.exceptions;

public class UnableToCreateWalletAddress extends RuntimeException {

    public UnableToCreateWalletAddress(String msg, Throwable err){
        super(msg, err);
    }
    public UnableToCreateWalletAddress(String s) {
        super(s);
    }
}
