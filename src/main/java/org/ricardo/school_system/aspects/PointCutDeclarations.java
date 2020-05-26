package org.ricardo.school_system.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class PointCutDeclarations {

	@Pointcut("execution(* org.ricardo.school_system.controllers.*.*(..))")
	public void getControllerPackage() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.login(..))")
	public void loginEndPoint() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.addTeacher(..))")
	public void addTeacherEndPoint() {}
	
	@Pointcut("getControllerPackage() && !(loginEndPoint() || addTeacherEndPoint())")
	public void validateToken() {}
}
