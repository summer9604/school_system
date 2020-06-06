package org.ricardo.school_system.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class ControllerPointCutDeclarations {
		
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.*(..))")
	public void getControllerPackage() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.login*(..))")
	public void loginEndPoint() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.admin*(..))")
	public void getGeneralAdminEndPoints() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.homePage(..))")
	public void getEntryPoint() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.*.localAdmin*(..))")
	public void getLocalAdminEndPoints() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.ExceptionHandlerController.*(..))")
	public void getExceptionHandlerControllerClass() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.SchoolController.getTeacher*(..))")
	public void getTeachersEndPoint() {}
	
	@Pointcut("execution(* org.ricardo.school_system.controllers.SchoolController.getTeacherById(..))")
	public void getTeacherByIdEndPoint() {}
	
	@Pointcut("getControllerPackage() && !(getEntryPoint() || loginEndPoint() || getExceptionHandlerControllerClass())")
	public void validateSession() {}
	
}












