package org.ricardo.school_system.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.daos.AdminDao;
import org.ricardo.school_system.daos.ClassDao;
import org.ricardo.school_system.entities.Class;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.ClassNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class CredentialsValidator extends GenericAspect {

	@Autowired
	private ClassDao classDao;

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private JwtHandler jwtHandler;

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getGeneralAdminEndPoints()")
	public void validateGeneralAdminTokens(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);

		if (!permissions.getPermissions().equals("ROLE_GENERAL_ADMIN"))
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.getTeachersEndPoint()")
	public void validateAdminTokensForGetTeachers(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);

		if (!permissions.getPermissions().equals("ROLE_GENERAL_ADMIN") && !permissions.getPermissions().equals("ROLE_LOCAL_ADMIN"))
			throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");		
	}

	@Before("org.ricardo.school_system.aspects.ServicePointCutDeclarations.checkGetClassByIdPermissions()")
	public void checkGetClassByIdPermissions(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		JwtUserPermissions userPermissions = jwtHandler.getUserPermissions(token);

		int classId = 0;

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof Integer) classId = (int) arg;
		}

		if (classId <= 0) 
			throw new ClassNotFoundException("Class not found.");

		Class schoolClass = classDao.getById(classId);

		if (schoolClass == null) 
			throw new ClassNotFoundException("Class not found.");

		if (userPermissions.getPermissions().equals("ROLE_STUDENT"))
			throw new OperationNotAuthorizedException("You dont' have enough permissions");	

		if (userPermissions.getPermissions().equals("ROLE_TEACHER") &&
				classDao.getClassTeacherRelation(userPermissions.getId(), schoolClass.getId()) == null)
			throw new OperationNotAuthorizedException("You dont' have enough permissions");	

		if (userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN") && 
				adminDao.getSchoolIdByLocalAdminId(userPermissions.getId()) != classDao.getSchoolIdByClassId(schoolClass.getId())) 
			throw new OperationNotAuthorizedException("You dont' have enough permissions");			
	}

}













