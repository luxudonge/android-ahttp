package com.alex.http.request;

public class AHttpException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7168374754320081622L;

	
	public AHttpException() {
		super();
	}

	public AHttpException(String detailMessage) {
		super(detailMessage);
	}

	public AHttpException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AHttpException(Throwable throwable) {
		super(throwable);
	}
}
