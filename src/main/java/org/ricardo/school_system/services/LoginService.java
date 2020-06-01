package org.ricardo.school_system.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.LoginForm;
import org.ricardo.school_system.auth.JwtHandler;
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
	public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, LoginForm loginInfo, String role) {

		HttpSession session = request.getSession(false);
			//CHECKAR AQUI, RETIRAR A SESSAO E FAZER VERIFICAÇAO NO AOP.
		if (session == null)
			return generateLoginSession(request, response, loginInfo, role, session);

		throw new OperationNotAuthorizedException("You have a session already.");
	}

	public ResponseEntity<?> logout(HttpServletRequest request) {
		return generateLogoutEnvironment(request);
	}

	private ResponseEntity<?> generateLoginSession(HttpServletRequest request, HttpServletResponse response, LoginForm loginInfo, String role,
			HttpSession session) {

		String generatedToken;

		switch (role) {

		case "teacher":

			Teacher teacher = teacherDao.getByEmailAndPassword(loginInfo);

			if (teacher == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			generatedToken = jwtHandler.generateJwtToken(teacher.getId(), teacher.getTeacherRole());

			response.addHeader("Authorization", generatedToken);

			return new ResponseEntity<>("Hello, teacher '" + teacher.getName() + "'", HttpStatus.OK);

		case "student":

			Student student = studentDao.getByEmailAndPassword(loginInfo);

			if (student == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			generatedToken = jwtHandler.generateJwtToken(student.getId(), student.getStudentRole());

			response.addHeader("Authorization", generatedToken);

			return new ResponseEntity<>("Hello, student '" + student.getName() + "'", HttpStatus.OK);

		default:

			Admin admin = adminDao.getByEmailAndPassword(loginInfo);

			if (admin == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			String adminRole = admin.getRole();

			switch (adminRole) {

			case "local_admin":

				int schoolId = adminDao.getSchoolIdByLocalAdminId(admin.getId());

				generatedToken = jwtHandler.generateJwtToken(admin.getId(), admin.getRole());

				response.addHeader("Authorization", generatedToken);

				response.addIntHeader("school-id", schoolId);

				return new ResponseEntity<>("Hello, Local admin '" + admin.getName() + "'", HttpStatus.OK);

			default:

				generatedToken = jwtHandler.generateJwtToken(admin.getId(), admin.getRole());

				response.addHeader("Authorization", generatedToken);

				response.addIntHeader("school-id", -1);

				return new ResponseEntity<>("Hello, General admin '" + admin.getName() + "'", HttpStatus.OK);
			}
		}
	}

	private ResponseEntity<?> generateLogoutEnvironment(HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		String permissions = (String) session.getAttribute("user-permissions");

		switch (permissions) {

		case "ROLE_TEACHER":

			Teacher teacher = (Teacher) session.getAttribute("user-credentials");

			session.invalidate();

			return new ResponseEntity<>("Teacher '" + teacher.getName() + "' logged out.", HttpStatus.OK);

		case "ROLE_STUDENT":

			Student student = (Student) session.getAttribute("user-credentials");

			session.invalidate();

			return new ResponseEntity<>("Student '" + student.getName() + "' logged out.", HttpStatus.OK);

		case "ROLE_LOCAL_ADMIN":

			Admin localAdmin = (Admin) session.getAttribute("user-credentials");

			session.invalidate();

			return new ResponseEntity<>("Local admin '" + localAdmin.getName() + "' logged out.", HttpStatus.OK);

		default:

			Admin generalAdmin = (Admin) session.getAttribute("user-credentials");

			session.invalidate();

			return new ResponseEntity<>("General admin '" + generalAdmin.getName() + "' logged out.", HttpStatus.OK);
		}
	}

}
