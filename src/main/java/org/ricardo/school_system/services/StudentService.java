package org.ricardo.school_system.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.RegistrationStudentForm;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.entities.Student;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

	@Autowired
	private StudentDao studentDao;
	
	@Transactional
	public ResponseEntity<?> add(HttpServletRequest request, RegistrationStudentForm studentForm) {		
		
		HttpSession session = request.getSession(false);		
		
		String[] permissions = (String[]) session.getAttribute("user-permissions");
				
		for(String permission : permissions) {
			if(permission.equals("ROLE_TEACHER")) {
				
				Student student = new Student(studentForm.getName(), studentForm.getAddress(), 
											  917917917, studentForm.getEmail(), 
											  studentForm.getPassword());

				studentDao.add(student);
				
				return new ResponseEntity<>("Student ´" + student.getName() + "' added.", HttpStatus.OK);
			}					
		}
		
		throw new OperationNotAuthorizedException("You dont´t have enough permissions.");
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);		
						
		String[] permissions = (String[]) session.getAttribute("user-permissions");
		
		//hasPermissions(permissions); under development
		
		for(String permission : permissions) {
			if(permission.equals("ROLE_STUDENT"))
				return new ResponseEntity<>(studentDao.getAll(), HttpStatus.OK);
		}
		
		throw new OperationNotAuthorizedException("You dont´t have enough permissions.");
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
		//Nao vai ser o estudante a remover-se....quanto muito vai poder ver o seu perfil. Para já fica assim....
		HttpSession session = request.getSession(false);		
						
		studentDao.delete(id);	
		
		Student sessionStudent = (Student) session.getAttribute("user-credentials");
		
		if (sessionStudent.getId() == id) session.invalidate();
			
		return new ResponseEntity<>("Student with id " + id + " removed.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> update(HttpServletRequest request, Student student) {		
		return new ResponseEntity<>(studentDao.update(student), HttpStatus.OK);
	}
	
	private boolean hasPermissions(String[] permissions) {
		//blablablablabla
		return false;
	}
	
}















