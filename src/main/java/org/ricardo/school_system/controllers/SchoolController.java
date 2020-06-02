package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.services.CourseService;
import org.ricardo.school_system.services.LoginService;
import org.ricardo.school_system.services.SchoolService;
import org.ricardo.school_system.services.StudentService;
import org.ricardo.school_system.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/schools")
public class SchoolController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TeacherService teacherService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllSchools(){	
		return null;
	}
	
	@PostMapping("/admin/login")
	public ResponseEntity<?> loginGeneralAdmin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm loginForm){
		return loginService.login(response, loginForm, "general_admin");
	}
	
	@PostMapping("/localadmin/login")
	public ResponseEntity<?> loginLocalAdmin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm loginForm){
		return loginService.login(response, loginForm, "local_admin");
	}
	
	@GetMapping("/{id}/degrees")
	public ResponseEntity<?> getDegreesBySchool(@PathVariable("id") int id, HttpServletRequest request){
		return courseService.getDegreesBySchool(id, request);
	}
	
}












