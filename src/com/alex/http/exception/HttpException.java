package com.alex.http.exception;

/**
 * 
 * 异常
 * 
 * @author Alex.Lu
 *
 */
public class HttpException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7168374754320081622L;

	
	public HttpException() {
		super();
	}

	public HttpException(String detailMessage) {
		super(detailMessage);
	}

	public HttpException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public HttpException(Throwable throwable) {
		super(throwable);
	}
}
