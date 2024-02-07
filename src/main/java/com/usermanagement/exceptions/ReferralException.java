package com.usermanagement.exceptions;

public class ReferralException extends RuntimeException {

	public ReferralException(String msg, Throwable err){
		super(msg, err);
	}
	public ReferralException(String s) {
		// TODO Auto-generated constructor stub
		super(s);
	}

}
