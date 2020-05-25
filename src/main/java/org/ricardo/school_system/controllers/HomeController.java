package org.ricardo.school_system.controllers;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ricardo.school_system.assemblers.CookieHandler;
import org.ricardo.school_system.assemblers.DegreeSubjectBundle;
import org.ricardo.school_system.assemblers.LoginInfo;
import org.ricardo.school_system.assemblers.SchoolInfo;
import org.ricardo.school_system.assemblers.TeacherInfo;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Degree;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.services.DegreeService;
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
	private DegreeService degreeService;
	
	@Autowired
	private TeacherDao teacherDao;
	
	@Autowired
	private SubjectDao subjectDao;
	
	@Autowired
	private SchoolDao schoolDao;
	
	@PostMapping("/login")
	public ResponseEntity<Teacher> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginInfo loginInfo){
				
		HttpSession session = request.getSession(false);
				
		if (loginInfo.getUsername().equals("Ricardo") && 
		   (session == null || session.getAttribute("user-credentials") == null)) {
			
			session = request.getSession();
			
			Teacher teacher = teacherDao.getById(30);
			
			session.setAttribute("user-credentials", teacher);
			
			System.out.println("Session id : " + session.getId());
			
			return new ResponseEntity<>(teacher, HttpStatus.OK);			
		}
		
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
		
	@GetMapping("/teachers")
	public ResponseEntity<List<Teacher>> getTeachers(HttpServletRequest request, HttpServletResponse response) {	
		
//		response.addCookie(new CookieHandler("name", "jose_semedo"));
//		response.addCookie(new CookieHandler("id", "1904"));
//		response.addCookie(new CookieHandler("token", "ctcrcfexeexnn$$%buwishwiuhswihhi.unuinn"));
				
		HttpSession session = request.getSession(false);
				
		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(teacherDao.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<Boolean> logout(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("user-credentials") == null) return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		
		session.invalidate();
		
		return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/subjects/{id}")
	public ResponseEntity<Subject> getSubject(HttpServletRequest request, @PathVariable("id") int id){
		
		HttpSession session = request.getSession(false);

		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(teacherDao.getSubject(id), HttpStatus.OK);
	}
	
	@GetMapping("/subjects")
	public ResponseEntity<List<Subject>> getSubjects(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);

		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(subjectDao.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/teachers/{id}")
	public ResponseEntity<String> removeTeacher(HttpServletRequest request, @PathVariable("id") int id) {
		
		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute("user-credentials") != null) {
			teacherDao.delete(id);	
			return new ResponseEntity<>("Teacher with id " + id + " deleted.", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("Operation not completed due to no authorization.", HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/degrees")
	public ResponseEntity<List<Degree>> getDegrees(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
			
		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(degreeService.getAll(), HttpStatus.OK);
	}
	
	@PostMapping("/teachers")
	public ResponseEntity<Teacher> addTeacher(HttpServletRequest request, @RequestBody TeacherInfo teacherInfo) {
				
		HttpSession session = request.getSession(false);
		
		Subject subject = subjectDao.getById(teacherInfo.getSubjectId());
		
		Teacher teacher = new Teacher(teacherInfo.getName(), teacherInfo.getAddress(), 
						              teacherInfo.getPhonenumber(), teacherInfo.getEmail(), subject);
		
		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(teacherDao.add(teacher), HttpStatus.OK);
	}
	
	@PostMapping("/degrees")
	public ResponseEntity<Degree> addDegree(HttpServletRequest request, @RequestBody DegreeSubjectBundle degreeSubjectBundle) {	
		
		HttpSession session = request.getSession(false);

		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(degreeService.addDegreeWithSubjects(degreeSubjectBundle), HttpStatus.OK);
	}
	
	@PostMapping("/teachers/{id}")
	public ResponseEntity<School> addSchoolToTeacher(HttpServletRequest request, @PathVariable("id") int id, @RequestBody SchoolInfo schoolInfo) {
		
		HttpSession session = request.getSession(false); //convem fazer a verificacao primeiro, assim poupa se tempo!
		
		Teacher teacher = teacherDao.getById(id);
		
		School school = schoolDao.getSchoolByName(schoolInfo.getName());
		
		teacher.setSchool(school);
		
		teacherDao.update(teacher);
		
		return (session == null || session.getAttribute("user-credentials") == null) ?
				new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED):
				new ResponseEntity<>(school, HttpStatus.OK);
	}
	
}





