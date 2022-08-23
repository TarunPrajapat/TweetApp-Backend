package com.tweetapp.exception;

/**
 * @author Tarun Prajapat
 */
public class TweetNotFoundException extends Exception {

	/**
	 *  This exception is thrown when tweet is not found
	 */
	private static final long serialVersionUID = 1L;

	public TweetNotFoundException(String msg) {
		super(msg);
	}
}
