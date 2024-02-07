package com.usermanagement.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException(String msg, Throwable err){
		super(msg, err);
	}

	public UserAlreadyExistsException(String msg){
		super(msg);
	}

}
