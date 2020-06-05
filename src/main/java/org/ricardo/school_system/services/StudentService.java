package org.ricardo.school_system.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.RegistrationStudentForm;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private JwtHandler jwtHandler;

	@Transactional
	public ResponseEntity<?> add(HttpServletRequest request, RegistrationStudentForm studentForm) {		

		Student student = new Student(studentForm.getName(), studentForm.getAddress(), 
				917917917, studentForm.getEmail(), 
				studentForm.getPassword());

		studentDao.add(student);

		return new ResponseEntity<>("Student ´" + student.getName() + "' added.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		return new ResponseEntity<>(studentDao.getAll(), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getById(HttpServletRequest request, int id) {			
		return new ResponseEntity<>(studentDao.getById(id), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getByEmail(HttpServletRequest request, String email) {		
		return new ResponseEntity<>(studentDao.getByEmail(email), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getByName(HttpServletRequest request, String name) {
		return new ResponseEntity<>(studentDao.getByName(name), HttpStatus.OK);
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
		
		return new ResponseEntity<>(studentDao.getStudentsByTeacherId(userPermissions.getId()), HttpStatus.OK);
	}
	
	private JwtUserPermissions retrievePermissions(HttpServletRequest request) {

		for(Cookie cookie : request.getCookies()) {

			if(cookie.getName().equals("jwtToken"))
				return jwtHandler.getUserPermissions(cookie.getValue());
		}

		return null;
	}

}















