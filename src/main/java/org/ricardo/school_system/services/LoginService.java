package org.ricardo.school_system.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.AdminDao;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.dto.GreetingsHolder;
import org.ricardo.school_system.dto.LoginForm;
import org.ricardo.school_system.entities.Admin;
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
	private AdminDao adminDao;

	@Autowired
	private TeacherDao teacherDao;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private JwtHandler jwtHandler;

	@Transactional
	public ResponseEntity<?> login(HttpServletResponse response, LoginForm loginInfo, String role) {
		return generateLoginSession(response, loginInfo, role);
	}

	@Transactional
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		return generateLogoutEnvironment(request, response);
	}

	private ResponseEntity<?> generateLoginSession(HttpServletResponse response, LoginForm loginInfo, String role) {

		String generatedToken;
		String greetings;

		switch (role) {

		case "teacher":

			Teacher teacher = teacherDao.getByEmailAndPassword(loginInfo);

			if (teacher == null)
				throw new OperationNotAuthorizedException("Wrong credentials.");

			generatedToken = jwtHandler.generateJwtToken(teacher.getId(), -1, teacher.getTeacherRole());

			setHeaders(response, generatedToken);
			
			greetings = "Hello, teacher '" + teacher.getName() + "'.";
			
			return new ResponseEntity<>(generateGreetings(greetings, teacher.getId(), teacher.getTeacherRole()), HttpStatus.OK);

		case "student":

			Student student = studentDao.getByEmailAndPassword(loginInfo);

			if (student == null)
				throw new OperationNotAuthorizedException("Wrong credentials.");

			generatedToken = jwtHandler.generateJwtToken(student.getId(), -1, student.getStudentRole());

			setHeaders(response, generatedToken);

			greetings = "Hello, student '" + student.getName() + "'.";
			
			return new ResponseEntity<>(generateGreetings(greetings, student.getId(), student.getStudentRole()), HttpStatus.OK);

		default:

			Admin admin = adminDao.getByEmailAndPassword(loginInfo);

			if (admin == null)
				throw new OperationNotAuthorizedException("Wrong credentials.");

			String adminRole = admin.getRole();

			switch (adminRole) {

			case "ROLE_LOCAL_ADMIN":

				int schoolId = adminDao.getSchoolIdByLocalAdminId(admin.getId());

				generatedToken = jwtHandler.generateJwtToken(admin.getId(), schoolId, admin.getRole());

				setHeaders(response, generatedToken);
				
				greetings = "Hello, Local admin '" + admin.getName() + "'.";

				return new ResponseEntity<>(generateGreetings(greetings, admin.getId(), admin.getRole()), HttpStatus.OK);

			default:

				generatedToken = jwtHandler.generateJwtToken(admin.getId(), -1, admin.getRole());

				setHeaders(response, generatedToken);
				
				greetings = "Hello, General admin '" + admin.getName() + "'.";

				return new ResponseEntity<>(generateGreetings(greetings, admin.getId(), admin.getRole()), HttpStatus.OK);
			}
		}
	}

	private ResponseEntity<?> generateLogoutEnvironment(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getHeader("jwttoken");

		JwtUserPermissions jwtUserPermissions = jwtHandler.getUserPermissions(token);

		switch (jwtUserPermissions.getPermissions()) {

		case "ROLE_TEACHER":

			Teacher teacher = teacherDao.getById(jwtUserPermissions.getId());

			return new ResponseEntity<>("Teacher '" + teacher.getName() + "' logged out.", HttpStatus.OK);	

		case "ROLE_STUDENT":

			Student student = studentDao.getById(jwtUserPermissions.getId());

			return new ResponseEntity<>("Student '" + student.getName() + "' logged out.", HttpStatus.OK);

		case "ROLE_LOCAL_ADMIN":

			Admin localAdmin = adminDao.getById(jwtUserPermissions.getId());

			return new ResponseEntity<>("Local admin '" + localAdmin.getName() + "' logged out.", HttpStatus.OK);

		default:

			Admin generalAdmin = adminDao.getById(jwtUserPermissions.getId());

			return new ResponseEntity<>("General admin '" + generalAdmin.getName() + "' logged out.", HttpStatus.OK);
		}
	}
	
	private GreetingsHolder generateGreetings(String greetings, int userId, String userRole) {
		return new GreetingsHolder(greetings, userId, userRole);
	}
	
	private void setHeaders(HttpServletResponse response, String generatedToken) {
		
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Authorization", generatedToken);
		response.addHeader("Access-Control-Expose-Headers", "*");
	}

}
















