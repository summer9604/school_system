package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.assemblers.SchoolForm;
import org.ricardo.school_system.assemblers.TeacherForm;
import org.ricardo.school_system.services.LoginService;
import org.ricardo.school_system.services.SchoolService;
import org.ricardo.school_system.services.TeacherService;
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
@RequestMapping("/teachers")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private LoginService loginService;
	
	@PostMapping("/login")
	public ResponseEntity<?> loginTeacher(HttpServletRequest request, @RequestBody LoginForm loginInfo){
		return loginService.login(request, loginInfo, "teacher");
	}
		
	@GetMapping("/list")
	public ResponseEntity<?> getTeachers(HttpServletRequest request) {	
		return teacherService.getAll(request);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addTeacher(HttpServletRequest request, @RequestBody TeacherForm teacherInfo) {
		return teacherService.add(request, teacherInfo);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<?> addSchoolToTeacher(HttpServletRequest request, @PathVariable("id") int id, @RequestBody SchoolForm schoolInfo) {
		return schoolService.addSchoolToTeacher(request, id, schoolInfo);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeTeacher(HttpServletRequest request, @PathVariable("id") int id) {
		return teacherService.delete(request, id);
	}
	
}
