package com.alpha.www.ElectronicStore.exception;

public class BadApiRequestException extends RuntimeException {

	public BadApiRequestException() {
		super("bad request");
	}

	public BadApiRequestException(String message) {
		super(message);
	}

	
}
