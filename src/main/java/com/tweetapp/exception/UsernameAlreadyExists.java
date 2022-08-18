package com.tweetapp.exception;

/**
 * @author Parichay Gupta
 */
public class UsernameAlreadyExists extends Exception {

	/**
	 * This exception is thrown when username/ email already exists
	 */
	private static final long serialVersionUID = 1L;

	public UsernameAlreadyExists(String msg) {
		super(msg);
	}
}
