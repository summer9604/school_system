package org.ricardo.school_system.aspects;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.ricardo.school_system.assemblers.DataAndPermissions;
import org.ricardo.school_system.auth.JwtHandler;
import org.ricardo.school_system.auth.JwtUserPermissions;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericAspect {

	@Autowired
	public JwtHandler jwtHandler;
	
	public String getToken(JoinPoint joinPoint) {
		
		for(Object arg : joinPoint.getArgs()) {

			if (arg instanceof HttpServletRequest) {

				HttpServletRequest request = (HttpServletRequest) arg;
				
				if (request.getHeader("jwttoken") != null)
					return request.getHeader("jwttoken");
			}			
		}
		
		return null;
	}
	
	public JwtUserPermissions getUserPermissions(JoinPoint joinPoint) {
		
		String token = getToken(joinPoint);

		JwtUserPermissions userPermissions = jwtHandler.getUserPermissions(token);
		
		return userPermissions;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	public DataAndPermissions getDataAndPermissions(JoinPoint joinPoint, Object obj){

		JwtUserPermissions userPermissions = getUserPermissions(joinPoint);
		
		for(Object arg : joinPoint.getArgs()) {
			if (arg.getClass().equals(obj.getClass())) obj = arg;
		}

		return new DataAndPermissions(userPermissions, obj);
	}
	
}
