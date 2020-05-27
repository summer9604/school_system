package org.ricardo.school_system.services;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.StudentForm;
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
	
	@Transactional
	public ResponseEntity<?> add(HttpServletRequest request, StudentForm studentForm) {		
		
		HttpSession session = request.getSession(false);		
		
		if (session == null) return new ResponseEntity<>("You are not logged in.", HttpStatus.FORBIDDEN);
		
		String[] permissions = (String[]) session.getAttribute("user-permissions");
		
		System.out.println("\nCHEGUEI AQUI!\n");
		
		for(String permission : permissions) {
			if(permission.equals("ROLE_TEACHER")) {
				
				Student student = new Student(studentForm.getName(), studentForm.getAddress(), 
											  studentForm.getPhonenumber(), studentForm.getEmail(), 
											  studentForm.getPassword());
				
				studentDao.add(student);
				
				return new ResponseEntity<>("Student ´" + student.getName() + "' added.", HttpStatus.OK);
			}					
		}
		
		return new ResponseEntity<>("You dont´t have enough permissions.", HttpStatus.UNAUTHORIZED);		
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);		
		
		if (session == null) return new ResponseEntity<>("You are not logged in.", HttpStatus.FORBIDDEN);
		
		String[] permissions = (String[]) session.getAttribute("user-permissions");
		
		for(String permission : permissions) {
			if(permission.equals("STUDENT"))
				return new ResponseEntity<>(studentDao.getAll(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>("You dont´t have enough permissions.", HttpStatus.UNAUTHORIZED);	
	}

	@Transactional
	public Student getById(int id) {
		return studentDao.getById(id);
	}

	@Transactional
	public Student getByEmail(String email) {
		return studentDao.getByEmail(email);
	}

	@Transactional
	public List<Student> getByName(String name) {
		return studentDao.getByName(name);
	}

	@Transactional
	public ResponseEntity<?> delete(HttpServletRequest request, int id) {
		//Nao vai ser o estudante a remover-se....quanto muito vai poder ver o seu perfil. Para já fica assim....
		HttpSession session = request.getSession(false); 
		
		Student sessionStudent = (Student) session.getAttribute("user-credentials");
		
		if (session != null && sessionStudent != null) {
			
			studentDao.delete(id);	
	
			if (sessionStudent.getId() == id) session.invalidate();
			
			return new ResponseEntity<>("Student with id " + id + " removed.", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("You are not logged in.", HttpStatus.FORBIDDEN);
	}

	@Transactional
	public Student update(Student student) {
		return studentDao.update(student);
	}
}
