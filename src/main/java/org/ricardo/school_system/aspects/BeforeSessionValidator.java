package org.ricardo.school_system.aspects;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.exceptions.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class BeforeSessionValidator {

	@Autowired
	private JwtHandler jwtHandler;

	@Before("org.ricardo.school_system.aspects.PointCutDeclarations.validateSession()")
	public void cenas(JoinPoint joinPoint) {

		for (Object arg : joinPoint.getArgs()) {

			if (arg instanceof HttpServletRequest) {

				HttpServletRequest request = (HttpServletRequest) arg;

				String token = (String) request.getHeader("Authorization");
				
				if (token == null)
					throw new SessionNotFoundException("AOP VALIDATOR - You are not logged in.");

				if (jwtHandler.getUserPermissions(token) == null)
					throw new SessionNotFoundException("AOP VALIDATOR - Invalid token.");
			}
		}
	}

}
