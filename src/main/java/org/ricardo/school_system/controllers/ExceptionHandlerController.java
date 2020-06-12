package org.ricardo.school_system.controllers;

import org.ricardo.school_system.exceptions.AdminNotFoundException;
import org.ricardo.school_system.exceptions.ClassNotFoundException;
import org.ricardo.school_system.exceptions.ErrorResponse;
import org.ricardo.school_system.exceptions.GradeNotFoundException;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.SchoolNotFoundException;
import org.ricardo.school_system.exceptions.SessionNotFoundException;
import org.ricardo.school_system.exceptions.StudentNotFoundException;
import org.ricardo.school_system.exceptions.TeacherNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleGradeNotFoundxception(GradeNotFoundException exc) {

		ErrorResponse response = new ErrorResponse();

		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setMessage(exc.getMessage());
		response.setTimeStamp(System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleAdminNotFoundException(AdminNotFoundException exc) {

		ErrorResponse response = new ErrorResponse();

		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setMessage(exc.getMessage());
		response.setTimeStamp(System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleStudentNotFoundException(SchoolNotFoundException exc) {

		ErrorResponse response = new ErrorResponse();

		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setMessage(exc.getMessage());
		response.setTimeStamp(System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleStudentNotFoundException(StudentNotFoundException exc) {

		ErrorResponse response = new ErrorResponse();

		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setMessage(exc.getMessage());
		response.setTimeStamp(System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handlerClassNotFoundException(ClassNotFoundException exc) {

		ErrorResponse response = new ErrorResponse();

		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setMessage(exc.getMessage());
		response.setTimeStamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handlerTeacherNotFoundException(TeacherNotFoundException exc) {

		ErrorResponse response = new ErrorResponse();

		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setMessage(exc.getMessage());
		response.setTimeStamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handlerSessionNotFound(SessionNotFoundException exc) {

		ErrorResponse response = new ErrorResponse();

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setMessage(exc.getMessage());
		response.setTimeStamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleOperationNotCompleteException(OperationNotAuthorizedException exc) {

		ErrorResponse response = new ErrorResponse();

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setMessage(exc.getMessage());
		response.setTimeStamp(System.currentTimeMillis());

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

//	@ExceptionHandler
//	public ResponseEntity<ErrorResponse> handleGenericException(Exception exc) {
//		
//		ErrorResponse response = new ErrorResponse();
//
//		response.setStatus(HttpStatus.BAD_REQUEST.value());
//		response.setMessage("Bad request");
//		response.setTimeStamp(System.currentTimeMillis());
//
//		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//	}
	
}
