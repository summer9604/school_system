package org.ricardo.school_system.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.School;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.TeacherNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(4)
public class BeforeCredentials extends GenericAspect {

	@Autowired
	private SchoolDao schoolDao;

	@Autowired
	private TeacherDao teacherDao;

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetTeacherByIdForAdmin()")
	public void validateAdminTokensForGetTeacherById(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);

		int teacherId = 0;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof Integer) teacherId = (int) arg;
		}

		Teacher teacher = teacherDao.getById(teacherId);

		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with id " + teacherId + " not found.");

		School schoolTeacher = schoolDao.getSchoolByTeacherId(teacher.getId());

		if (permissions.getPermissions().equals("ROLE_LOCAL_ADMIN") && permissions.getSchoolId() != schoolTeacher.getId())
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");		
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkHireTeacher()")
	public void checkIfTeacherIsEmployedAlready(JoinPoint joinPoint) {

		int teacherId = 0;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof Integer) teacherId = (int) arg;
		}
		
		Teacher teacher = teacherDao.getById(teacherId); 

		if (teacher == null)
			throw new TeacherNotFoundException("Teacher with id " + teacherId + " not found.");

		School schoolTeacher = schoolDao.getSchoolByTeacherId(teacher.getId());

		if (schoolTeacher.getId() > 0) 
			throw new OperationNotAuthorizedException("Teacher is currently employed.");
	}

}


















