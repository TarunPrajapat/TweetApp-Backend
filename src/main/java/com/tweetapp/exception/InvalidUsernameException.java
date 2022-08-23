package com.tweetapp.exception;

/**
 * @author Tarun Prajapat
 */
public class InvalidUsernameException extends Exception {

	/**
	 * This exception is thrown when username is not available
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUsernameException(String msg) {
		super(msg);
	}

}
