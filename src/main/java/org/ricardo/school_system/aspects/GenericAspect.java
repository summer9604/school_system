package org.ricardo.school_system.aspects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;

public abstract class GenericAspect {

	public String getToken(JoinPoint joinPoint) {

		for(Object arg : joinPoint.getArgs()) {

			if (arg instanceof HttpServletRequest) {

				HttpServletRequest request = (HttpServletRequest) arg;

				if (request.getCookies() != null) {

					for(Cookie cookie : request.getCookies()) {

						if (cookie.getName().equals("jwtToken")) {														
							return cookie.getValue();
						}
					}
				}
			}			
		}
		return null;
	}
	
}
