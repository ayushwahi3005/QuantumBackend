package com.quantummaintenance.users.exception;

import org.springframework.mail.MailException;

public class TheMailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6432688625962585803L;
	public TheMailException(String message, MailException ex) {
		super(message);
	}
}
