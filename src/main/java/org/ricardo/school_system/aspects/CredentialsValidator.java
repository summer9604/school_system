package org.ricardo.school_system.aspects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class CredentialsValidator {

	@Autowired
	private JwtHandler jwtHandler;

	@Before("org.ricardo.school_system.aspects.PointCutDeclarations.getGeneralAdminEndPoints()")
	public void validateTokens(JoinPoint joinPoint) {

		HttpServletRequest request = getHttpServletRequest(joinPoint);

		if(request.getCookies() != null) {

			for(Cookie cookie : request.getCookies()) {

				if (cookie.getName().equals("jwtToken")) {

					String token = cookie.getValue();

					JwtUserPermissions permissions = jwtHandler.getUserPermissions(token);

					if (!permissions.getPermissions().equals("ROLE_GENERAL_ADMIN"))
						throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");
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
