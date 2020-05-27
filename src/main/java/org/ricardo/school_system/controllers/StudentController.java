package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.assemblers.StudentForm;
import org.ricardo.school_system.services.LoginService;
import org.ricardo.school_system.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/students")
public class StudentController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private StudentService studentService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginForm loginInfo) {
		return loginService.login(request, loginInfo, "student");
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		return studentService.getAll(request);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> add(HttpServletRequest request, StudentForm studentForm) {
		return studentService.add(request, studentForm);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remove(@PathVariable("id") int id, HttpServletRequest request){
		return studentService.delete(request, id);
	}

}
