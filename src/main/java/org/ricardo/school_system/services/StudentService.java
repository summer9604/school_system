package org.ricardo.school_system.services;

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
import org.ricardo.school_system.entities.Student;
import org.ricardo.school_system.entities.Class;
import org.ricardo.school_system.entities.StudentSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private StudentSubjectDao studentSubjectDao;

	@Autowired
	private ClassDao classDao;

	@Autowired
	private JwtHandler jwtHandler;

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

		return new ResponseEntity<>("Student ´" + student.getName() + "' added to class with id " + schoolClass.getId(), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getGradesByStudentId(HttpServletRequest request){

		JwtUserPermissions uerPermissions = retrievePermissions(request);

		int studentId = uerPermissions.getId();

		return new ResponseEntity<>(studentSubjectDao.getByStudentId(studentId), HttpStatus.OK);
	}

	//FOR ADMINS, TEACHERS...
	@Transactional
	public ResponseEntity<?> getGradesByStudentId(HttpServletRequest request, int studentId){
		return new ResponseEntity<>(studentSubjectDao.getByStudentId(studentId), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {
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
		return new ResponseEntity<>(studentDao.getById(studentId), HttpStatus.OK);
	}

	//PROFESSORES ONLY
	@Transactional
	public ResponseEntity<?> giveGradeToStudent(HttpServletRequest request, StudentGradeForm studentGradeForm) {

		JwtUserPermissions userPermissions = retrievePermissions(request);

		StudentSubject studentSubject = studentSubjectDao.getByStudentIdAndTeacherId(userPermissions.getId(), studentGradeForm.getStudentId());

		studentSubject.setGrade(studentGradeForm.getGrade());

		return new ResponseEntity<>(studentSubjectDao.update(studentSubject), HttpStatus.OK);
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















