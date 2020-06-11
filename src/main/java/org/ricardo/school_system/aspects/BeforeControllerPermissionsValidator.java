package org.ricardo.school_system.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class BeforeControllerPermissionsValidator extends GenericAspect {

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getAdminController()")
	public void checkAdminControllerPermissions(JoinPoint joinPoint) {
		
		String token = getToken(joinPoint);

		JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);
		
		if (!permissions.getPermissions().equals("ROLE_GENERAL_ADMIN") && !permissions.getPermissions().equals("ROLE_LOCAL_ADMIN"))
			throw new OperationNotAuthorizedException("Access denied. You are not an admin.");
	}
	
	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getTeacherController()")
	public void checkTeacherControllerPermissions(JoinPoint joinPoint) {
		
		String token = getToken(joinPoint);

		JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);
		
		if (!permissions.getPermissions().equals("ROLE_TEACHER"))
			throw new OperationNotAuthorizedException("Access denied. You are not a teacher.");
	} 
	
	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getStudentController()")
	public void checkStudentControllerPermissions(JoinPoint joinPoint) {
		
		String token = getToken(joinPoint);

		JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);
		
		if (!permissions.getPermissions().equals("ROLE_STUDENT"))
			throw new OperationNotAuthorizedException("Access denied. You are not a student.");
	}
}
