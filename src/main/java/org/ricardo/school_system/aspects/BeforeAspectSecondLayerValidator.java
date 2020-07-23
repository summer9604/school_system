package org.ricardo.school_system.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.dto.JwtUserPermissions;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class BeforeAspectSecondLayerValidator extends GenericAspect {

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getAdminController()")
	public void checkAdminControllerPermissions(JoinPoint joinPoint) {
		
		JwtUserPermissions userPermissions = getUserPermissions(joinPoint);
		
		if (!userPermissions.getPermissions().equals("ROLE_GENERAL_ADMIN") && !userPermissions.getPermissions().equals("ROLE_LOCAL_ADMIN"))
			throw new OperationNotAuthorizedException("Access denied. You are not an admin.");		
	}
	
	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getTeacherController()")
	public void checkTeacherControllerPermissions(JoinPoint joinPoint) {
		
		JwtUserPermissions userPermissions = getUserPermissions(joinPoint);
		
		if (!userPermissions.getPermissions().equals("ROLE_TEACHER"))
			throw new OperationNotAuthorizedException("Access denied. You are not a teacher.");
	} 
	
	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.getStudentController()")
	public void checkStudentControllerPermissions(JoinPoint joinPoint) {
		
		JwtUserPermissions userPermissions = getUserPermissions(joinPoint);
		
		if (!userPermissions.getPermissions().equals("ROLE_STUDENT"))
			throw new OperationNotAuthorizedException("Access denied. You are not a student.");
	}
	
}
