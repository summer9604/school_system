package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import org.ricardo.school_system.dto.DegreeSubjectBundle;
import org.ricardo.school_system.services.CourseService;
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
@CrossOrigin("http://localhost:4200")
@RequestMapping("/courses")
public class CourseController {
	
	@Autowired
	private CourseService courseService;
		
	@GetMapping("/subjects")
	public ResponseEntity<?> getSubjects(HttpServletRequest request){
		return courseService.getAllSubjects(request);
	}
	
	@GetMapping("/subjects/{id}")
	public ResponseEntity<?> getSubject(HttpServletRequest request, @PathVariable("id") int id){
		return courseService.getSubjectById(request, id);
	}
	
	@PostMapping("/subjects")
	public ResponseEntity<?> adminAddSubject(HttpServletRequest request, @RequestBody String name) {	
		return courseService.addSubject(name);
	}
	
	@GetMapping("/degrees")
	public ResponseEntity<?> getDegrees(HttpServletRequest request){
		return courseService.getAllDegrees(request);
	}
	
	@GetMapping("/degrees/{id}")
	public ResponseEntity<?> getDegreeById(HttpServletRequest request, @PathVariable("id") int id){
		return courseService.getDegreeById(request, id);
	}
	
	@PostMapping("/degrees")
	public ResponseEntity<?> adminAddDegree(HttpServletRequest request, @RequestBody DegreeSubjectBundle degreeSubjectBundle) {	
		return courseService.addDegree(request, degreeSubjectBundle);
	}
}
