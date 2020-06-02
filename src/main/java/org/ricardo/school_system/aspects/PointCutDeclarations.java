package org.ricardo.school_system.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class PointCutDeclarations {

	@Pointcut("execution(* org.ricardo.school_system.controllers.*.*(..))")
	public void getControllerPackage() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.login*(..))")
	public void loginEndPoint() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.homePage(..))")
	public void getEntryPoint() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.admin*(..))")
	public void getGeneralAdminEndPoints() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.localAdmin*(..))")
	public void getLocalAdminEndPoints() {}
	
	@Pointcut("getControllerPackage() && !(getEntryPoint() || loginEndPoint())")
	public void validateSession() {}
	
}
