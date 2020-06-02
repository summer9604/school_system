package org.ricardo.school_system.aspects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
public class BeforeSessionValidator {

	@Autowired
	private JwtHandler jwtHandler;

	@Before("org.ricardo.school_system.aspects.PointCutDeclarations.validateSession()")
	public void verifyToken(JoinPoint joinPoint) {

		HttpServletRequest request = getHttpServletRequest(joinPoint);

		if (request.getCookies() != null) {

			for(Cookie cookie : request.getCookies()) {

				if (cookie.getName().equals("jwtToken")) {

					String token = cookie.getValue();
					//REFORMULAR EXCEPTIONS
					if (token == null || token.length() == 0)
						throw new SessionNotFoundException("AOP VALIDATOR - You are not logged in.");

					if (jwtHandler.getUserPermissions(token) == null)
						throw new SessionNotFoundException("AOP VALIDATOR - Invalid token.");
				}
			}
		}
		else {
			throw new SessionNotFoundException("AOP VALIDATOR - You are not logged in.");
		}
	}

	@Before("org.ricardo.school_system.aspects.PointCutDeclarations.loginEndPoint()")
	public void checkAlreadyLoggedUser(JoinPoint joinPoint) {

		HttpServletRequest request = getHttpServletRequest(joinPoint);

		if(request.getCookies() != null) {

			for(Cookie cookie : request.getCookies()) {

				if (cookie.getName().equals("jwtToken")) {

					String token = cookie.getValue();

					if (jwtHandler.getUserPermissions(token) != null)
						throw new OperationNotAuthorizedException("You have a session already.");
				}
			} 	
		}
	}

	private HttpServletRequest getHttpServletRequest(JoinPoint joinPoint) {

		for(Object arg : joinPoint.getArgs()) {
			if (arg instanceof HttpServletRequest) return (HttpServletRequest) arg;
		}
		return null;
	}

}





















