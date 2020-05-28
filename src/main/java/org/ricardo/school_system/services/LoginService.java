package org.ricardo.school_system.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Student;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
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
		
		if (session == null) return generateLoginSession(request, loginInfo, role, session);
		
		throw new OperationNotAuthorizedException("You have a session already.");
	}
	
	public ResponseEntity<?> logout(HttpServletRequest request){				
		return generateLogoutEnvironment(request);
	}
	
	private ResponseEntity<?> generateLoginSession(HttpServletRequest request, LoginForm loginInfo, 
									           String role, HttpSession session) {

		String[] permissions = null;
		
		if (role.equals("teacher")) {
							
			Teacher teacher = teacherDao.getByEmailAndPassword(loginInfo);
			
			if (teacher != null) {
				
				session = request.getSession();
				
				session.setAttribute("user-credentials", teacher);
				
				permissions = new String[] {"ROLE_TEACHER", "ROLE_STUDENT"};
				
				session.setAttribute("user-permissions", permissions);
				
				return new ResponseEntity<>(teacher, HttpStatus.OK);
			}				
		}
		
		if (role.equals("student")) {
			
			Student student = studentDao.getByEmailAndPassword(loginInfo);
			
			if (student != null) {
				
				session = request.getSession();
				
				session.setAttribute("user-credentials", student);
				
				permissions = new String[] {"ROLE_STUDENT"};
				
				session.setAttribute("user-permissions", permissions);
				
				return new ResponseEntity<>(student, HttpStatus.OK);
			}				
		}	
				
		throw new OperationNotAuthorizedException("Wrong credentials");
	}
	
	private ResponseEntity<?> generateLogoutEnvironment(HttpServletRequest request){
		
		HttpSession session = request.getSession(false);
		
	    String[] permissions = (String[]) session.getAttribute("user-permissions");
		
		for(String permission : permissions) {
			
			if(permission.equals("ROLE_TEACHER")) {
				
				Teacher teacher = (Teacher) session.getAttribute("user-credentials");
				
				session.invalidate();
				
				return new ResponseEntity<>("Teacher '" +  teacher.getName() + "' logged out.", HttpStatus.OK);
			}
		}
				
		Student student = (Student) session.getAttribute("user-credentials");
		
		session.invalidate();
				
		return new ResponseEntity<>("Student '" +  student.getName() + "' logged out.", HttpStatus.OK);
	}
	
}

