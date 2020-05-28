package org.ricardo.school_system.exceptions;

public class OperationNotAuthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OperationNotAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationNotAuthorizedException(String message) {
		super(message);
	}

	public OperationNotAuthorizedException(Throwable cause) {
		super(cause);
	}
}
