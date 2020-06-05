package org.ricardo.school_system.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.ricardo.school_system.exceptions.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class BeforeSessionValidator extends GenericAspect {

	@Autowired
	private JwtHandler jwtHandler;

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.validateSession()")
	public void verifyToken(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		if (token == null || token.length() == 0)
			throw new SessionNotFoundException("AOP VALIDATOR - You are not logged in.");

		if (jwtHandler.getUserPermissions(token) == null)
			throw new SessionNotFoundException("AOP VALIDATOR - Invalid token.");			
	}

	@Before("org.ricardo.school_system.aspects.ControllerPointCutDeclarations.loginEndPoint()")
	public void verifyLoginEndpoint(JoinPoint joinPoint) {

		String token = getToken(joinPoint);

		if (jwtHandler.getUserPermissions(token) != null)
			throw new OperationNotAuthorizedException("You have a session already.");
	}

}


















