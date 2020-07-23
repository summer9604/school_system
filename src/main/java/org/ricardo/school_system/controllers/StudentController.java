package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ricardo.school_system.dto.LoginForm;
import org.ricardo.school_system.services.LoginService;
import org.ricardo.school_system.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/students")
public class StudentController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private StudentService studentService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm loginInfo) {
		return loginService.login(response, loginInfo, "student");
	}	
	
	@GetMapping("/me")
	public ResponseEntity<?> getInfo(HttpServletRequest request) {
		return studentService.getById(request);
	}
	
	@GetMapping("/grades")
	public ResponseEntity<?> getGrades(HttpServletRequest request){
		return studentService.getGradesByStudentId(request);
	}
	
}
