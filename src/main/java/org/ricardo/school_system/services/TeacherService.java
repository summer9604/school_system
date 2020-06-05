package org.ricardo.school_system.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.RegistrationTeacherForm;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

	@Autowired
	private TeacherDao teacherDao;

	@Autowired
	private SubjectDao subjectDao;

	@Autowired
	private JwtHandler jwtHandler;

	@Transactional
	public ResponseEntity<?> add(HttpServletRequest request, RegistrationTeacherForm teacherInfo) {	

		Subject subject = subjectDao.getById(teacherInfo.getSubjectId());

		Teacher teacher = new Teacher(teacherInfo.getName(), teacherInfo.getAddress(), 
				teacherInfo.getPhonenumber(), teacherInfo.getEmail(), 
				teacherInfo.getPassword(), subject);

		return new ResponseEntity<>(teacherDao.add(teacher), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		
		JwtUserPermissions userPermissions = retrievePermissions(request);
		
		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN"))
			return new ResponseEntity<>(teacherDao.getTeachersBySchoolId(userPermissions.getSchoolId()), HttpStatus.OK);
		
		return new ResponseEntity<>(teacherDao.getAll(), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getById(HttpServletRequest request) {		

		JwtUserPermissions userPermissions = retrievePermissions(request);

		return new ResponseEntity<>(teacherDao.getById(userPermissions.getId()), HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<?> getByIdForAdmin(int id, HttpServletRequest request) {		
		return new ResponseEntity<>(teacherDao.getById(id), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getByEmail(HttpServletRequest request, String email) {				
		return new ResponseEntity<>(teacherDao.getByEmail(email), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getByName(HttpServletRequest request, String name) {				
		return new ResponseEntity<>(teacherDao.getByName(name), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> delete(HttpServletRequest request, int id) {
		
		teacherDao.delete(id);
		
		return new ResponseEntity<>("Teacher with id " + id + " removed.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> update(HttpServletRequest request, Teacher teacher) {				
		return new ResponseEntity<>(teacherDao.update(teacher), HttpStatus.OK);
	}

	private JwtUserPermissions retrievePermissions(HttpServletRequest request) {

		for(Cookie cookie : request.getCookies()) {
			
			if (cookie.getName().equals("jwtToken"))
				return jwtHandler.getUserPermissions(cookie.getValue());
		}
		
		return null;
	}

}




















