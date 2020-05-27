package org.ricardo.school_system.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Student;
import org.ricardo.school_system.entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	@Autowired
	private TeacherDao teacherDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Transactional
	public ResponseEntity<?> login(HttpServletRequest request, LoginForm loginInfo, String role){

		HttpSession session = request.getSession(false);
		
		if (session == null) {
			
			String[] permissions = null;
			
			if (role.equals("teacher")) {
								
				Teacher teacher = teacherDao.getByEmailAndPassword(loginInfo);
				
				if (teacher != null) {
					
					session = request.getSession();
					
					session.setAttribute("user-credentials", teacher);
					
					permissions = new String[] {teacher.getTeacherRole()};
					
					session.setAttribute("user-permissions", permissions);
					
					return new ResponseEntity<>(teacher, HttpStatus.OK);
				}				
			}
			
			if (role.equals("student")) {
				
				Student student = studentDao.getByEmailAndPassword(loginInfo);
				
				if (student != null) {
					
					session = request.getSession();
					
					session.setAttribute("user-credentials", student);
					
					permissions = new String[] {student.getStudentRole()};
					
					session.setAttribute("user-permissions", permissions);
					
					return new ResponseEntity<>(student, HttpStatus.OK);
				}				
			}	
			
			return new ResponseEntity<>("Wrong credentials", HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<>("You have a session already.", HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<?> logout(HttpServletRequest request){

		HttpSession session = request.getSession(false);
		
		if (session == null) return new ResponseEntity<>("You have no current session.", HttpStatus.METHOD_NOT_ALLOWED);
		
		Teacher teacher = (Teacher) session.getAttribute("user-credentials");
		
		session.invalidate();
		
		return new ResponseEntity<>("User '" +  teacher.getName() + "' logged out.", HttpStatus.ACCEPTED);
	}
	
}

