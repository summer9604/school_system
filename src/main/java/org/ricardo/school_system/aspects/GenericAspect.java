package org.ricardo.school_system.aspects;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.ricardo.school_system.auth.JwtHandler;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericAspect {

	@Autowired
	public JwtHandler jwtHandler;
	
	public String getToken(JoinPoint joinPoint) {
		
		for(Object arg : joinPoint.getArgs()) {

			if (arg instanceof HttpServletRequest) {

				HttpServletRequest request = (HttpServletRequest) arg;

				System.out.println("Token: " + request.getHeader("jwttoken"));
				
				if (request.getHeader("jwttoken") != null)
					return request.getHeader("jwttoken");
			}			
		}
		
		return null;
	}
	
}
