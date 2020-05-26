package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ricardo.school_system.assemblers.DegreeSubjectBundle;
import org.ricardo.school_system.assemblers.LoginInfo;
import org.ricardo.school_system.assemblers.SchoolInfo;
import org.ricardo.school_system.assemblers.TeacherInfo;
import org.ricardo.school_system.assemblers.TeacherSchool;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.services.CourseService;
import org.ricardo.school_system.services.SchoolService;
import org.ricardo.school_system.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private SchoolService schoolService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginInfo loginInfo){
				
		HttpSession session = request.getSession(false);
				
		if (loginInfo.getUsername().equals("Ricardo") &&  //adicionar roles a cada tipo de user, bora bugar tudooooo!!
		   (session == null || session.getAttribute("user-credentials") == null)) {
			
			session = request.getSession();
			
			Teacher teacher = teacherService.getById(32);
			
			session.setAttribute("user-credentials", teacher);
			
			String[] permissions = new String[] {"EMPLOYEE", "MANAGER"};
			
			session.setAttribute("user-permissions", permissions);
		
			return new ResponseEntity<>(teacher, HttpStatus.OK);			
		}
		
		return !(loginInfo.getUsername().equals("Ricardo")) ? 
				new ResponseEntity<>("Wrong credentials", HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>("You have a session already.", HttpStatus.BAD_REQUEST);
	}
		
	@GetMapping("/teachers")
	public ResponseEntity<?> getTeachers(HttpServletRequest request) {	
				
		HttpSession session = request.getSession(false);		
				
		if (session == null) return new ResponseEntity<>("You are not logged in.", HttpStatus.FORBIDDEN);
		
		String[] permissions = (String[]) session.getAttribute("user-permissions");
		
		for(String permission : permissions) {
			if(permission.equals("ADMIN"))
				return new ResponseEntity<>(teacherService.getAll(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>("You dont´t have enough permissions.", HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("user-credentials") == null) return new ResponseEntity<>("You have no current session.", HttpStatus.METHOD_NOT_ALLOWED);
		
		Teacher teacher = (Teacher) session.getAttribute("user-credentials");
		
		session.invalidate();
		
		return new ResponseEntity<>("User '" +  teacher.getName() + "' logged out.", HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/subjects/{id}")
	public ResponseEntity<?> getSubject(HttpServletRequest request, @PathVariable("id") int id){
		
		HttpSession session = request.getSession(false);

		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>("You are not authorized.", HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(courseService.getSubjectById(id), HttpStatus.OK);
	}
	
	@GetMapping("/subjects")
	public ResponseEntity<?> getSubjects(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);

		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>("You are not authorized.", HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(courseService.getAllSubjects(), HttpStatus.OK);
	}
	
	@GetMapping("/teachers/{id}")
	public ResponseEntity<?> removeTeacher(HttpServletRequest request, @PathVariable("id") int id) {
		
		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute("user-credentials") != null) {
			teacherService.delete(id);	
			return new ResponseEntity<>("Teacher with id " + id + " removed.", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("You are not authorized.", HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/degrees")
	public ResponseEntity<?> getDegrees(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
			
		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>("You are not authorized.", HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(courseService.getAllDegrees(), HttpStatus.OK);
	}
	
	@PostMapping("/teachers")
	public ResponseEntity<?> addTeacher(HttpServletRequest request, @RequestBody TeacherInfo teacherInfo) {
				
		HttpSession session = request.getSession(false);
		
		Subject subject = courseService.getSubjectById(teacherInfo.getSubjectId());
		
		Teacher teacher = new Teacher(teacherInfo.getName(), teacherInfo.getAddress(), 
						              teacherInfo.getPhonenumber(), teacherInfo.getEmail(), subject);
		
		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>("You are not authorized.", HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(teacherService.add(teacher), HttpStatus.OK);
	}
	
	@PostMapping("/degrees")
	public ResponseEntity<?> addDegree(HttpServletRequest request, @RequestBody DegreeSubjectBundle degreeSubjectBundle) {	
		
		HttpSession session = request.getSession(false);

		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>("You are not authorized.", HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(courseService.addDegreeWithSubjects(degreeSubjectBundle), HttpStatus.OK);
	}
	
	@PostMapping("/teachers/{id}")
	public ResponseEntity<?> addSchoolToTeacher(HttpServletRequest request, @PathVariable("id") int id, @RequestBody SchoolInfo schoolInfo) {
		
		HttpSession session = request.getSession(false); //convem fazer a verificacao primeiro, assim poupa se tempo!
		
		TeacherSchool teacherSchool = schoolService.addSchoolToTeacher(id,  schoolInfo);
		
		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>("You are not authorized.", HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>("Teacher '" + teacherSchool.getTeacherName() + 
				"' is now teaching in '" + teacherSchool.getSchoolName() + "'", HttpStatus.OK);
	}
	
}





