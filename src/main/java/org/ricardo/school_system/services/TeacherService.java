package org.ricardo.school_system.services;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.TeacherForm;
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
	
	@Transactional
	public ResponseEntity<?> add(HttpServletRequest request, TeacherForm teacherInfo) {	

		HttpSession session = request.getSession(false); //controlar roles ex.: ADMIN, TEACHER, STUDENT.
		
		if (session == null) return new ResponseEntity<>("You are not logged in.", HttpStatus.FORBIDDEN);
		
		Subject subject = subjectDao.getById(teacherInfo.getSubjectId()); //pode dar exceções, trabalhar no exceptionControllerHandler.
		
		Teacher teacher = new Teacher(teacherInfo.getName(), teacherInfo.getAddress(), 
						              teacherInfo.getPhonenumber(), teacherInfo.getEmail(), 
						              teacherInfo.getPassword(), subject);
		
		return new ResponseEntity<>(teacherDao.add(teacher), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);		
		
		if (session == null) return new ResponseEntity<>("You are not logged in.", HttpStatus.FORBIDDEN);
		
		String[] permissions = (String[]) session.getAttribute("user-permissions");
		
		if (permissions != null) System.out.println("\nNAO E NULO.\n");		
		
		for(String permission : permissions) {
			if(permission.equals("ROLE_TEACHER"))
				return new ResponseEntity<>(teacherDao.getAll(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>("You dont´t have enough permissions.", HttpStatus.UNAUTHORIZED);		
	}

	@Transactional
	public Teacher getById(int id) {
		return teacherDao.getById(id);
	}

	@Transactional
	public Teacher getByEmail(String email) {
		return teacherDao.getByEmail(email);
	}

	@Transactional
	public List<Teacher> getByName(String name) {
		return teacherDao.getByName(name);
	}

	@Transactional
	public ResponseEntity<?> delete(HttpServletRequest request, int id) {
		
		HttpSession session = request.getSession(false); 
		//o professor nao se vai elminar a si mesmo, ADMIN work.
		Teacher sessionTeacher = (Teacher) session.getAttribute("user-credentials");
		
		if (session != null && sessionTeacher != null) {
			
			teacherDao.delete(id);	
	
			if (sessionTeacher.getId() == id) session.invalidate();
			
			return new ResponseEntity<>("Teacher with id " + id + " removed.", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("You are not logged in.", HttpStatus.FORBIDDEN);
	}

	@Transactional
	public Teacher update(Teacher teacher) {
		return teacherDao.update(teacher);
	}
	
}
