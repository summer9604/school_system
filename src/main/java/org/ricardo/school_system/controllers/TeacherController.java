package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.assemblers.StudentGradeForm;
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
@CrossOrigin("*")
@RequestMapping("/teachers")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private StudentService studentService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm loginInfo){
		return loginService.login(response, loginInfo, "teacher");
	}
	
	@GetMapping("/me")
	public ResponseEntity<?> getInfo(HttpServletRequest request){
		return teacherService.getById(request);
	}
	
	@GetMapping("/classes")
	public ResponseEntity<?> getClasses(HttpServletRequest request){
		return schoolService.getClassesByTeacherId(request);
	}
	
	@GetMapping("/classes/{id}")
	public ResponseEntity<?> getClassById(@PathVariable("id") int id, HttpServletRequest request){
		return schoolService.getClassById(request, id);
	}
	
	@GetMapping("/students")
	public ResponseEntity<?> getStudents(HttpServletRequest request){
		return studentService.getStudentsByTeacherId(request);
	}
 	
	@GetMapping("/students/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable("id") int id, HttpServletRequest request){
		return studentService.getById(request, id);
	}
	
	@PostMapping("/students/grades")
	public ResponseEntity<?> giveStudentsGrades(HttpServletRequest request, @RequestBody StudentGradeForm studentGradeForm) {
		return studentService.giveGradeToStudent(request, studentGradeForm);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
															//IMPLEMENTAR//
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

													//	Procurar notas do aluno;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
															//IMPLEMENTAR//
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
}

















