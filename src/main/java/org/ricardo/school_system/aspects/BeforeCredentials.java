package org.ricardo.school_system.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.SchoolDao;
import org.ricardo.school_system.daos.TeacherDao;
import org.ricardo.school_system.entities.Teacher;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.TeacherNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(3)
public class BeforeCredentials extends GenericAspect {

	@Autowired
	private JwtHandler jwtHandler;

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
			throw new TeacherNotFoundException("Teacher not Found");

		int schoolId = schoolDao.getSchoolIdByTeacherId(teacher.getId());

		if (permissions.getPermissions().equals("ROLE_LOCAL_ADMIN") && permissions.getSchoolId() != schoolId)
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");		
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkHireTeacher()")
	public void checkIfTeacherIsEmployedAlready(JoinPoint joinPoint) {

		for(Object arg : joinPoint.getArgs()) {

			if (arg instanceof Integer) {

				Teacher teacher = teacherDao.getById((int) arg); 

				if (teacher == null)
					throw new TeacherNotFoundException("Teacher Not Found");

				int schoolId = schoolDao.getSchoolIdByTeacherId(teacher.getId());

				if (schoolId > 0) 
					throw new OperationNotAuthorizedException("Teacher is currently employed.");
			}
		}
	}

}
