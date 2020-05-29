package org.ricardo.school_system.aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.exceptions.OperationNotAuthorizedException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class BeforeAuthorizationValidator {

	@Before("org.ricardo.school_system.aspects.PointCutDeclarations.getDegreesPerSchoolEnpoint()")
	public void validateTokens(JoinPoint joinPoint) {
		
		for (Object arg : joinPoint.getArgs()) {

			if (arg instanceof HttpServletRequest) {

				HttpServletRequest request = (HttpServletRequest) arg;

				HttpSession session = request.getSession(false);

				String permissions = (String) session.getAttribute("user-permissions");

				if (!permissions.equals("ROLE_LOCAL_ADMIN"))
					throw new OperationNotAuthorizedException("AOP VALIDATOR - You don't have enough permissions.");
			}
		}
	}

}
