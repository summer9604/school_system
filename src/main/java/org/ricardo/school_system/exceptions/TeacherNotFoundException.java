package org.ricardo.school_system.exceptions;

public class TeacherNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TeacherNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public TeacherNotFoundException(String message) {
		super(message);
	}

	public TeacherNotFoundException(Throwable cause) {
		super(cause);
	}
}
