package org.ricardo.school_system.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.ricardo.school_system.assemblers.LoginForm;
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

	@Transactional
	public ResponseEntity<?> login(HttpServletRequest request, LoginForm loginInfo, String role) {

		HttpSession session = request.getSession(false);

		if (session == null)
			return generateLoginSession(request, loginInfo, role, session);

		throw new OperationNotAuthorizedException("You have a session already.");
	}

	public ResponseEntity<?> logout(HttpServletRequest request) {
		return generateLogoutEnvironment(request);
	}

	private ResponseEntity<?> generateLoginSession(HttpServletRequest request, LoginForm loginInfo, String role,
			HttpSession session) {

		String[] permissions = null;

		switch (role) {

		case "teacher":

			Teacher teacher = teacherDao.getByEmailAndPassword(loginInfo);

			if (teacher == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			session = request.getSession();

			session.setAttribute("user-credentials", teacher);

			session.setAttribute("user-permissions", "ROLE_TEACHER");

			return new ResponseEntity<>(teacher, HttpStatus.OK);

		case "student":

			Student student = studentDao.getByEmailAndPassword(loginInfo);

			if (student == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			session = request.getSession();

			session.setAttribute("user-credentials", student);

			session.setAttribute("user-permissions", "ROLE_STUDENT");

			return new ResponseEntity<>(student, HttpStatus.OK);

		default:

			Admin admin = adminDao.getByEmailAndPassword(loginInfo);

			if (admin == null)
				throw new OperationNotAuthorizedException("Wrong credentials");

			String adminRole = admin.getRole();

			switch (adminRole) {

			case "local_admin":

				session = request.getSession();

				int schoolId = adminDao.getSchoolIdByLocalAdminId(admin.getId());

				session.setAttribute("user-credentials", admin);

				session.setAttribute("school-credentials", schoolId);

				session.setAttribute("user-permissions", "ROLE_LOCAL_ADMIN");

				return new ResponseEntity<>(admin, HttpStatus.OK);

			default:

				session = request.getSession();

				session.setAttribute("user-credentials", admin);

				session.setAttribute("user-permissions", "ROLE_GENERAL_ADMIN");

				return new ResponseEntity<>(admin, HttpStatus.OK);
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
