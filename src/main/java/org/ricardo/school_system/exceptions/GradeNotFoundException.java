package org.ricardo.school_system.exceptions;

public class GradeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GradeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public GradeNotFoundException(String message) {
		super(message);
	}

	public GradeNotFoundException(Throwable cause) {
		super(cause);
	}
}
