package org.ricardo.school_system.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.TeacherForm;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Subject;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
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
				
		Subject subject = subjectDao.getById(teacherInfo.getSubjectId()); //pode dar exceções, trabalhar no exceptionControllerHandler.
		
		Teacher teacher = new Teacher(teacherInfo.getName(), teacherInfo.getAddress(), 
						              teacherInfo.getPhonenumber(), teacherInfo.getEmail(), 
						              teacherInfo.getPassword(), subject);
		
		return new ResponseEntity<>(teacherDao.add(teacher), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);		
				
		String[] permissions = (String[]) session.getAttribute("user-permissions");
		
		//hasPermissions(permissions); under development
				
		for(String permission : permissions) {
			if(permission.equals("ROLE_TEACHER"))
				return new ResponseEntity<>(teacherDao.getAll(), HttpStatus.OK);
		}
		
		throw new OperationNotAuthorizedException("You dont´t have enough permissions.");
	}

	@Transactional
	public ResponseEntity<?> getById(HttpServletRequest request, int id) {			
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
		
		HttpSession session = request.getSession(false);		
				
		teacherDao.delete(id);	
		
		//o professor nao se vai elminar a si mesmo, ADMIN work.
		Teacher sessionTeacher = (Teacher) session.getAttribute("user-credentials");
		
		if (sessionTeacher.getId() == id) session.invalidate();
		
		return new ResponseEntity<>("Teacher with id " + id + " removed.", HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> update(HttpServletRequest request, Teacher teacher) {				
		return new ResponseEntity<>(teacherDao.update(teacher), HttpStatus.OK);
	}
	
	private boolean hasPermissions(String[] permissions) {
		//blablablablabla
		return false;
	}
	
}
