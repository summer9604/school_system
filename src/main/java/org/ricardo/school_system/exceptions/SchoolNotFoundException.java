package org.ricardo.school_system.exceptions;

public class SchoolNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SchoolNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SchoolNotFoundException(String message) {
		super(message);
	}

	public SchoolNotFoundException(Throwable cause) {
		super(cause);
	}
}
