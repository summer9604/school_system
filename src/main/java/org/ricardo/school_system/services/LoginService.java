package org.ricardo.school_system.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.AdminDao;
import org.ricardo.school_system.daos.StudentDao;
import org.ricardo.school_system.daos.TeacherDao;
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

		switch (role) {

		case "teacher":

			Teacher teacher = teacherDao.getByEmailAndPassword(loginInfo);

			if (teacher == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			generatedToken = jwtHandler.generateJwtToken(teacher.getId(), -1, teacher.getTeacherRole());

			Cookie teacherCookie = new Cookie("jwtToken", generatedToken);
			teacherCookie.setPath("/");
			teacherCookie.setMaxAge(0);

			response.addCookie(teacherCookie);
			response.addHeader("Access-Control-Allow-Credentials", "true");
			response.addHeader("Access-Control-Allow-Origin", "*");

			return new ResponseEntity<>("Hello, teacher '" + teacher.getName() + "'", HttpStatus.OK);

		case "student":

			Student student = studentDao.getByEmailAndPassword(loginInfo);

			if (student == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			generatedToken = jwtHandler.generateJwtToken(student.getId(), -1, student.getStudentRole());

			Cookie studentCookie = new Cookie("jwtToken", generatedToken);
			studentCookie.setPath("/");
			studentCookie.setMaxAge(0);

			response.addCookie(studentCookie);
			response.addHeader("Access-Control-Allow-Credentials", "true");
			response.addHeader("Access-Control-Allow-Origin", "*");

			return new ResponseEntity<>("Hello, student '" + student.getName() + "'", HttpStatus.OK);

		default:

			Admin admin = adminDao.getByEmailAndPassword(loginInfo);

			if (admin == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			String adminRole = admin.getRole();

			switch (adminRole) {

			case "ROLE_LOCAL_ADMIN":

				int schoolId = adminDao.getSchoolIdByLocalAdminId(admin.getId());

				generatedToken = jwtHandler.generateJwtToken(admin.getId(), schoolId, admin.getRole());

				Cookie localAdminCookie = new Cookie("jwtToken", generatedToken);
				localAdminCookie.setPath("/");
				localAdminCookie.setMaxAge(0);

				response.addCookie(localAdminCookie);
				response.addHeader("Access-Control-Allow-Credentials", "true");
				response.addHeader("Access-Control-Allow-Origin", "*");

				return new ResponseEntity<>("Hello, Local admin '" + admin.getName() + "'", HttpStatus.OK);

			default:

				generatedToken = jwtHandler.generateJwtToken(admin.getId(), -1, admin.getRole());

				Cookie generalAdminCookie = new Cookie("jwtToken", generatedToken);
				generalAdminCookie.setPath("/");
				generalAdminCookie.setMaxAge(0);

				response.addCookie(generalAdminCookie);
				response.addHeader("Access-Control-Allow-Credentials", "true");
				response.addHeader("Access-Control-Allow-Origin", "*");

				return new ResponseEntity<>("Hello, General admin '" + admin.getName() + "'", HttpStatus.OK);
			}
		}
	}

	private ResponseEntity<?> generateLogoutEnvironment(HttpServletRequest request, HttpServletResponse response) {

		Cookie cookie = null;

		for(Cookie requestCookie : request.getCookies()) {
			if(requestCookie.getName().equals("jwtToken")) {
				cookie = requestCookie;
				break;
			}
		}

		JwtUserPermissions jwtUserPermissions = jwtHandler.getUserPermissions(cookie.getValue());

		switch (jwtUserPermissions.getPermissions()) {

		case "ROLE_TEACHER":

			Teacher teacher = teacherDao.getById(jwtUserPermissions.getId());

			response.addCookie(new Cookie(cookie.getName(), ""));
			response.addHeader("Access-Control-Allow-Credentials", "false");

			return new ResponseEntity<>("Teacher '" + teacher.getName() + "' logged out.", HttpStatus.OK);	

		case "ROLE_STUDENT":

			Student student = studentDao.getById(jwtUserPermissions.getId());

			response.addCookie(new Cookie(cookie.getName(), ""));
			response.addHeader("Access-Control-Allow-Credentials", "false");

			return new ResponseEntity<>("Student '" + student.getName() + "' logged out.", HttpStatus.OK);

		case "ROLE_LOCAL_ADMIN":

			Admin localAdmin = adminDao.getById(jwtUserPermissions.getId());

			response.addCookie(new Cookie(cookie.getName(), ""));
			response.addHeader("Access-Control-Allow-Credentials", "false");

			return new ResponseEntity<>("Local admin '" + localAdmin.getName() + "' logged out.", HttpStatus.OK);

		default:

			Admin generalAdmin = adminDao.getById(jwtUserPermissions.getId());

			response.addCookie(new Cookie(cookie.getName(), ""));
			response.addHeader("Access-Control-Allow-Credentials", "false");

			return new ResponseEntity<>("General admin '" + generalAdmin.getName() + "' logged out.", HttpStatus.OK);
		}
	}

}
