package org.ricardo.school_system.services;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.ricardo.school_system.assemblers.RegistrationStudentForm;
import org.ricardo.school_system.assemblers.StudentGradeForm;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.ClassDao;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.daos.StudentSubjectDao;
import org.ricardo.school_system.entities.Class;
import org.ricardo.school_system.entities.Student;
import org.ricardo.school_system.entities.StudentSubject;
import org.ricardo.school_system.exceptions.GradeNotFoundException;
import org.ricardo.school_system.exceptions.StudentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

	@Autowired
	private ClassDao classDao;

	@Autowired
	private JwtHandler jwtHandler;

	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private StudentSubjectDao studentSubjectDao;
	
	@Transactional
	public ResponseEntity<?> add(HttpServletRequest request, RegistrationStudentForm studentForm) {		

		Student student = new Student();

		Class schoolClass = classDao.getById(studentForm.getClassId());

		student.setName(studentForm.getName());
		student.setAddress(studentForm.getAddress());
		student.setPhonenumber(studentForm.getPhonenumber());
		student.setEmail(studentForm.getEmail());
		student.setStudentClass(schoolClass);
		student.setPassword(studentForm.getPassword());

		studentDao.add(student);

		return new ResponseEntity<>("Student '" + student.getName() + "' added to class with id " + schoolClass.getId() + ".", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getGradesByStudentId(HttpServletRequest request){

		JwtUserPermissions uerPermissions = retrievePermissions(request);

		int studentId = uerPermissions.getId();
		
		List<StudentSubject> grades = studentSubjectDao.getByStudentId(studentId);
		
		if (grades.isEmpty())
			throw new GradeNotFoundException("You have no grades.");

		return new ResponseEntity<>(grades, HttpStatus.OK);
	}

	//FOR ADMINS, TEACHERS...
	@Transactional
	public ResponseEntity<?> getGradesByStudentId(HttpServletRequest request, int studentId){
		
		List<StudentSubject> grades = studentSubjectDao.getByStudentId(studentId);
		
		if (grades.isEmpty())
			throw new GradeNotFoundException("Student with id " + studentId + " has no grades attached");
		
		return new ResponseEntity<>(grades, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {

		JwtUserPermissions userPermissions = retrievePermissions(request);
		
		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN")) {
			
			List<Student> students = studentDao.getStudentsBySchoolId(userPermissions.getSchoolId());
			
			if (students.isEmpty())
				throw new StudentNotFoundException("There are no student at school with id " + userPermissions.getSchoolId() + ".");
			
			return new ResponseEntity<>(students, HttpStatus.OK);
		}
		
		List<Student> students = studentDao.getAll();
		
		if (students.isEmpty())
			throw new StudentNotFoundException("No students were found.");
		
		return new ResponseEntity<>(studentDao.getAll(), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getById(HttpServletRequest request) {		

		JwtUserPermissions uerPermissions = retrievePermissions(request);

		int studentId = uerPermissions.getId();

		return new ResponseEntity<>(studentDao.getById(studentId), HttpStatus.OK);
	}

	//FOR ADMINS, TEACHERS...
	@Transactional
	public ResponseEntity<?> getById(HttpServletRequest request, int studentId) {	
		
		Student student = studentDao.getById(studentId);
		
		if (student == null)
			throw new StudentNotFoundException("Student with id " + studentId + " not found.");
		
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	//PROFESSORES ONLY
	@Transactional
	public ResponseEntity<?> giveGradeToStudent(HttpServletRequest request, StudentGradeForm studentGradeForm) {

		JwtUserPermissions userPermissions = retrievePermissions(request);

		StudentSubject studentSubject = studentSubjectDao.getByStudentIdAndTeacherId(userPermissions.getId(), studentGradeForm.getStudentId());

		studentSubject.setGrade(studentGradeForm.getGrade());

		return new ResponseEntity<>(studentSubjectDao.update(studentSubject), HttpStatus.OK);
	}

	@Transactional     //local admin && general admin
	public ResponseEntity<?> getByEmail(HttpServletRequest request, String email) {		
		
		Student student = studentDao.getByEmail(email);
		
		if (student == null)
			throw new StudentNotFoundException("Student with email " + email + " not found.");
		
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getByName(HttpServletRequest request, String name) {
		
		List<Student> students = studentDao.getByName(name);
		
		if (students.isEmpty())
			throw new StudentNotFoundException("No students found named '" + name + "'.");
		
		return new ResponseEntity<>(students, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> delete(HttpServletRequest request, int id) {

		studentDao.delete(id);	

		return new ResponseEntity<>("Student with id " + id + " removed.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> update(HttpServletRequest request, Student student) {		
		return new ResponseEntity<>(studentDao.update(student), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getStudentsByTeacherId(HttpServletRequest request) {

		JwtUserPermissions userPermissions = retrievePermissions(request);
				
		List<Student> students = studentDao.getStudentsByTeacherId(userPermissions.getId());
		
		if (students.isEmpty())
			throw new StudentNotFoundException("Teacher with id " + userPermissions.getId() + " has no students.");

		return new ResponseEntity<>(students, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> expelStudent(HttpServletRequest request, int id) {
		
		studentDao.expelStudent(id);
		
		return new ResponseEntity<>("Student with id " + id + " was expelled!", HttpStatus.OK);
	}
	
	private JwtUserPermissions retrievePermissions(HttpServletRequest request) {

		for(Cookie cookie : request.getCookies()) {

			if(cookie.getName().equals("jwtToken"))
				return jwtHandler.getUserPermissions(cookie.getValue());
		}

		return null;
	}

}















