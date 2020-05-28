package org.ricardo.school_system.exceptions;

public class SessionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SessionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionNotFoundException(String message) {
		super(message);
	}

	public SessionNotFoundException(Throwable cause) {
		super(cause);
	}
}
