package org.ricardo.school_system.aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ricardo.school_system.exceptions.SessionNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class BeforeSessionValidator {

	@Before("org.ricardo.school_system.aspects.PointCutDeclarations.validateSession()")
	public void cenas(JoinPoint joinPoint) {

		for (Object arg : joinPoint.getArgs()) {

			if (arg instanceof HttpServletRequest) {

				HttpServletRequest request = (HttpServletRequest) arg;

				HttpSession session = request.getSession(false);

				if (session == null)
					throw new SessionNotFoundException("AOP VALIDATOR - You are not logged in.");
			}
		}
	}

}
