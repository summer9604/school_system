package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import org.ricardo.school_system.assemblers.DegreeSubjectBundle;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
	
	@PostMapping("/subjects/add")
	public ResponseEntity<?> addSubject(HttpServletRequest request, @RequestBody String name) {	
		return courseService.addSubject(name);
	}
	
	@GetMapping("/degrees")
	public ResponseEntity<?> getDegrees(HttpServletRequest request){
		return courseService.getAllDegrees(request);
	}
	
	@GetMapping("/degrees/{id}")
	public ResponseEntity<?> getDegree(HttpServletRequest request, @PathVariable("id") int id){
		return courseService.getDegreeById(request, id);
	}
	
	@PostMapping("/degrees/add")
	public ResponseEntity<?> addDegree(HttpServletRequest request, @RequestBody DegreeSubjectBundle degreeSubjectBundle) {	
		return courseService.addDegree(request, degreeSubjectBundle);
	}
	
}
