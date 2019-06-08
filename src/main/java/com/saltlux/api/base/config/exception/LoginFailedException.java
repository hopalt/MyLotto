package com.saltlux.api.base.config.exception;

public class LoginFailedException extends RuntimeException {

	private static final long serialVersionUID = -1108991664069800760L;

	public LoginFailedException(String msg, Throwable t) {
		super(msg, t);
	}

	public LoginFailedException(String msg) {
		super(msg);
	}

	public LoginFailedException() {
		super();
	}

}
