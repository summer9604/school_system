package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.assemblers.RegistrationLocalAdminForm;
import org.ricardo.school_system.assemblers.RegistrationStudentForm;
import org.ricardo.school_system.assemblers.RegistrationTeacherForm;
import org.ricardo.school_system.assemblers.TeacherClassForm;
import org.ricardo.school_system.services.CourseService;
import org.ricardo.school_system.services.LoginService;
import org.ricardo.school_system.services.SchoolService;
import org.ricardo.school_system.services.StudentService;
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
@CrossOrigin("*")
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
	
	//USAR AROUND ADIVCE PARA CONTROLAR O RETORNO (TUDO A FUNCIONAR AQUI)
	@GetMapping("/all")
	public ResponseEntity<?> getAllSchools(HttpServletRequest request){	
		return schoolService.getAllSchools();
	}
	
	@PostMapping("/admin/login")
	public ResponseEntity<?> loginGeneralAdmin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginForm loginForm){
		return loginService.login(response, loginForm, "admin");
	}
		
	@GetMapping("/teachers")
	public ResponseEntity<?> getTeachers(HttpServletRequest request) {	
		return teacherService.getAll(request);
	}
	
	@PostMapping("/teachers")
	public ResponseEntity<?> adminAddTeacher(HttpServletRequest request, @RequestBody RegistrationTeacherForm teacherInfo) {
		return teacherService.add(request, teacherInfo);
	}
	
	@GetMapping("/teachers/{id}")
	public ResponseEntity<?> getTeacherById(@PathVariable("id") int id, HttpServletRequest request) {	
		return teacherService.getByIdForAdmin(id, request);
	}
		
	@PostMapping("/teachers/{id}")
	public ResponseEntity<?> localAdminHireTeacher(@PathVariable("id") int id, HttpServletRequest request) {
		return schoolService.hireTeacher(request, id);
	}
	
	@PostMapping("/teachers/placement")
	public ResponseEntity<?> localAdminPlaceTeachersIntoClasses(@RequestBody TeacherClassForm teacherClassForm, HttpServletRequest request){
		return teacherService.placeIntoClasses(teacherClassForm, request);
	}
	
	@DeleteMapping("/teachers/{id}")
	public ResponseEntity<?> adminRemoveTeacher(@PathVariable("id") int id, HttpServletRequest request) {
		return teacherService.delete(request, id);
	}
	
	@GetMapping("/{id}/degrees")
	public ResponseEntity<?> getDegreesBySchool(@PathVariable("id") int id, HttpServletRequest request){
		return courseService.getDegreesBySchool(id, request);
	}
	
	@GetMapping("/{id}/classes")
	public ResponseEntity<?> getClassesBySchool(@PathVariable("id") int id, HttpServletRequest request) {
		return schoolService.getClassesBySchool(id, request);
	}
	
	@PostMapping("/localadmins")
	public ResponseEntity<?> adminAddLocalAdmin(HttpServletRequest request, @RequestBody RegistrationLocalAdminForm registrationLocalAdminForm) {
		return schoolService.addLocalAdmin(request, registrationLocalAdminForm);
	}
	
	@PostMapping("/students")
	public ResponseEntity<?> localAdminAdd(HttpServletRequest request, @RequestBody RegistrationStudentForm studentForm) {		
		return studentService.add(request, studentForm);
	}
	
									//IMPLEMENTAR
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	@GetMapping("/all")
//	public ResponseEntity<?> adminGetAll(HttpServletRequest request) {
//		return studentService.getAll(request);
//	}
//	
//	@DeleteMapping("/{id}")
//	public ResponseEntity<?> adminRemove(@PathVariable("id") int id, HttpServletRequest request){
//		return studentService.delete(request, id);
//	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
}












