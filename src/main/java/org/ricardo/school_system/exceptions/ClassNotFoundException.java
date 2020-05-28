package org.ricardo.school_system.exceptions;

public class ClassNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClassNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClassNotFoundException(String message) {
		super(message);
	}

	public ClassNotFoundException(Throwable cause) {
		super(cause);
	}
}
